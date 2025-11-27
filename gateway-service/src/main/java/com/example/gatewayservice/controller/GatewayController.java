package com.example.gatewayservice.controller;

import com.example.gatewayservice.dto.UserInfoResponse;
import com.example.gatewayservice.service.FlightService;
import com.example.gatewayservice.service.GatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class GatewayController {

    private final GatewayService gatewayService;
    private final FlightService flightService;

    public GatewayController(GatewayService gatewayService, FlightService flightService) {
        this.gatewayService = gatewayService;
        this.flightService = flightService;
    }

    // 获取用户完整信息 - 修复：使请求头可选
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getUserInfo(
            @RequestHeader(value = "X-User-Name", required = false) String username) {

        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            UserInfoResponse userInfo = gatewayService.getUserInfo(username);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.status(503).body(new UserInfoResponse());
        }
    }

    // 获取航班列表 - 修复：明确指定参数名称
    @GetMapping("/flights")
    public ResponseEntity<Object> getFlights(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {

        return flightService.getFlights(page, size);
    }

    // 健康检查
    @GetMapping("/manage/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    // 测试服务状态
    @GetMapping("/test")
    public ResponseEntity<String> testServices() {
        StringBuilder result = new StringBuilder("=== Service Status ===\n");

        // 测试 Flight Service
        try {
            ResponseEntity<Object> flightResponse = flightService.getFlights(0, 1);
            result.append("✓ Flight Service: UP\n");
        } catch (Exception e) {
            result.append("✗ Flight Service: DOWN - ").append(e.getMessage()).append("\n");
        }

        // 测试其他服务
        result.append("✓ Gateway Service: UP\n");

        return ResponseEntity.ok(result.toString());
    }
}