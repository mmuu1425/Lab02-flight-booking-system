package com.example.bonusservice.controller;

import com.example.bonusservice.entity.Privilege;
import com.example.bonusservice.entity.PrivilegeHistory;
import com.example.bonusservice.service.BonusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController

@RequestMapping("/api/v1/privilege")
public class BonusController {

    private final BonusService bonusService;

    public BonusController(BonusService bonusService) {
        this.bonusService = bonusService;
    }
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("=== 测试开始 ===");

        // 测试创建用户
        Privilege privilege = bonusService.getOrCreatePrivilege("test_user");
        System.out.println("创建用户成功: " + privilege.getUsername());

        // 测试查询历史
        List<PrivilegeHistory> history = bonusService.getPrivilegeHistory("test_user");
        System.out.println("历史记录数量: " + history.size());

        return ResponseEntity.ok("测试成功 - 用户: " + privilege.getUsername() + ", 历史记录: " + history.size());
    }
    // 获取用户特权信息（包含历史）
    @GetMapping
    public ResponseEntity<PrivilegeInfoResponse> getPrivilegeInfo(
            @RequestHeader("X-User-Name") String username) {

        Optional<Privilege> privilegeOpt = bonusService.getPrivilege(username);
        if (privilegeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Privilege privilege = privilegeOpt.get();
        List<PrivilegeHistory> history = bonusService.getPrivilegeHistory(username);

        PrivilegeInfoResponse response = new PrivilegeInfoResponse(privilege, history);
        return ResponseEntity.ok(response);
    }

    // 处理购买积分操作（内部调用）
    @PostMapping("/process-purchase")
    public ResponseEntity<BonusOperationResponse> processPurchase(
            @RequestBody PurchaseRequest request) {

        BonusService.BonusOperationResult result = bonusService.processPurchase(
                request.getUsername(),
                request.getTicketUid(),
                request.getPrice(),
                request.isPaidFromBalance()
        );

        BonusOperationResponse response = new BonusOperationResponse(
                result.getPaidByMoney(),
                result.getPaidByBonuses(),
                result.getPrivilege()
        );

        return ResponseEntity.ok(response);
    }

    // 处理退票积分操作（内部调用）
    @PostMapping("/process-refund")
    public ResponseEntity<Void> processRefund(@RequestBody RefundRequest request) {
        bonusService.processRefund(request.getUsername(), request.getTicketUid());
        return ResponseEntity.ok().build();
    }

    // Health check endpoint
    @GetMapping("/manage/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    // DTO类
    public static class PrivilegeInfoResponse {
        private final Integer balance;
        private final String status;
        private final List<PrivilegeHistory> history;

        public PrivilegeInfoResponse(Privilege privilege, List<PrivilegeHistory> history) {
            this.balance = privilege.getBalance();
            this.status = privilege.getStatus();
            this.history = history;
        }

        public Integer getBalance() { return balance; }
        public String getStatus() { return status; }
        public List<PrivilegeHistory> getHistory() { return history; }
    }

    public static class BonusOperationResponse {
        private final Integer paidByMoney;
        private final Integer paidByBonuses;
        private final PrivilegeShortInfo privilege;

        public BonusOperationResponse(Integer paidByMoney, Integer paidByBonuses, Privilege privilege) {
            this.paidByMoney = paidByMoney;
            this.paidByBonuses = paidByBonuses;
            this.privilege = new PrivilegeShortInfo(privilege.getBalance(), privilege.getStatus());
        }

        public Integer getPaidByMoney() { return paidByMoney; }
        public Integer getPaidByBonuses() { return paidByBonuses; }
        public PrivilegeShortInfo getPrivilege() { return privilege; }
    }

    public static class PrivilegeShortInfo {
        private final Integer balance;
        private final String status;

        public PrivilegeShortInfo(Integer balance, String status) {
            this.balance = balance;
            this.status = status;
        }

        public Integer getBalance() { return balance; }
        public String getStatus() { return status; }
    }

    public static class PurchaseRequest {
        private String username;
        private UUID ticketUid;
        private Integer price;
        private boolean paidFromBalance;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public UUID getTicketUid() { return ticketUid; }
        public void setTicketUid(UUID ticketUid) { this.ticketUid = ticketUid; }

        public Integer getPrice() { return price; }
        public void setPrice(Integer price) { this.price = price; }

        public boolean isPaidFromBalance() { return paidFromBalance; }
        public void setPaidFromBalance(boolean paidFromBalance) { this.paidFromBalance = paidFromBalance; }
    }

    public static class RefundRequest {
        private String username;
        private UUID ticketUid;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public UUID getTicketUid() { return ticketUid; }
        public void setTicketUid(UUID ticketUid) { this.ticketUid = ticketUid; }
    }
}
