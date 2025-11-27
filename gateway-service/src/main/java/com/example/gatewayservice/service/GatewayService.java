package com.example.gatewayservice.service;

import com.example.gatewayservice.dto.PrivilegeShortInfo;
import com.example.gatewayservice.dto.TicketResponse;
import com.example.gatewayservice.dto.UserInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GatewayService {

    private final RestTemplate restTemplate;

    @Value("${service.flight.url}")
    private String flightServiceUrl;

    @Value("${service.ticket.url}")
    private String ticketServiceUrl;

    @Value("${service.bonus.url}")
    private String bonusServiceUrl;

    public GatewayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 获取用户完整信息
    public UserInfoResponse getUserInfo(String username) {
        // 获取票务信息
        List<TicketResponse> tickets = getUserTickets(username);

        // 获取特权信息
        PrivilegeShortInfo privilege = getPrivilegeInfo(username);

        return new UserInfoResponse(tickets, privilege);
    }

    // 获取用户票务信息
    private List<TicketResponse> getUserTickets(String username) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Name", username);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<TicketResponse[]> response = restTemplate.exchange(
                    ticketServiceUrl + "/api/v1/tickets",
                    HttpMethod.GET,
                    entity,
                    TicketResponse[].class
            );

            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            // 如果服务不可用，返回空列表
            return List.of();
        }
    }

    // 获取用户特权信息
    private PrivilegeShortInfo getPrivilegeInfo(String username) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-User-Name", username);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    bonusServiceUrl + "/api/v1/privilege",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Map<String, Object> privilegeData = response.getBody();
            if (privilegeData != null) {
                Integer balance = (Integer) privilegeData.get("balance");
                String status = (String) privilegeData.get("status");
                return new PrivilegeShortInfo(balance, status);
            }
        } catch (Exception e) {
            // 如果服务不可用，返回默认值
        }
        return new PrivilegeShortInfo(0, "BRONZE");
    }
}