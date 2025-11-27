package com.example.bonusservice.service;

import com.example.bonusservice.entity.Privilege;
import com.example.bonusservice.entity.PrivilegeHistory;
import com.example.bonusservice.repository.PrivilegeRepository;
import com.example.bonusservice.repository.PrivilegeHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BonusService {

    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeHistoryRepository privilegeHistoryRepository;

    public BonusService(PrivilegeRepository privilegeRepository,
                        PrivilegeHistoryRepository privilegeHistoryRepository) {
        this.privilegeRepository = privilegeRepository;
        this.privilegeHistoryRepository = privilegeHistoryRepository;
    }

    // 获取或创建用户特权
    public Privilege getOrCreatePrivilege(String username) {
        Optional<Privilege> existingPrivilege = privilegeRepository.findByUsername(username);
        if (existingPrivilege.isPresent()) {
            return existingPrivilege.get();
        }

        Privilege newPrivilege = new Privilege(username);
        return privilegeRepository.save(newPrivilege);
    }

    // 获取用户特权信息
    public Optional<Privilege> getPrivilege(String username) {
        return privilegeRepository.findByUsername(username);
    }

    // 获取用户积分历史
    public List<PrivilegeHistory> getPrivilegeHistory(String username) {
        return privilegeHistoryRepository.findByPrivilege_UsernameOrderByDatetimeDesc(username);
        // 或者使用：
        // return privilegeHistoryRepository.findByUsernameOrderByDatetimeDesc(username);
    }
    // 处理购买时的积分操作
    @Transactional
    public BonusOperationResult processPurchase(String username, UUID ticketUid, Integer price, boolean paidFromBalance) {
        Privilege privilege = getOrCreatePrivilege(username);
        int paidByBonuses = 0;
        int paidByMoney = price;

        if (paidFromBalance && privilege.getBalance() > 0) {
            // 使用积分支付
            paidByBonuses = Math.min(privilege.getBalance(), price);
            paidByMoney = price - paidByBonuses;

            // 扣除积分
            privilege.setBalance(privilege.getBalance() - paidByBonuses);
            privilegeRepository.save(privilege);

            // 记录积分扣除历史
            PrivilegeHistory debitHistory = new PrivilegeHistory(
                    privilege, ticketUid, -paidByBonuses, "DEBIT_THE_ACCOUNT"
            );
            privilegeHistoryRepository.save(debitHistory);
        } else {
            // 不使用积分支付，获得10%积分
            int earnedBonuses = (int) (price * 0.1);
            privilege.setBalance(privilege.getBalance() + earnedBonuses);
            privilegeRepository.save(privilege);

            // 记录积分获得历史
            PrivilegeHistory fillHistory = new PrivilegeHistory(
                    privilege, ticketUid, earnedBonuses, "FILL_IN_BALANCE"
            );
            privilegeHistoryRepository.save(fillHistory);
        }

        // 更新特权状态（基于积分余额）
        updatePrivilegeStatus(privilege);

        return new BonusOperationResult(paidByMoney, paidByBonuses, privilege);
    }

    // 处理退票时的积分操作
    @Transactional
    public void processRefund(String username, UUID ticketUid) {
        Optional<Privilege> privilegeOpt = privilegeRepository.findByUsername(username);
        if (privilegeOpt.isEmpty()) {
            return;
        }

        Privilege privilege = privilegeOpt.get();

        // 查找该票对应的积分历史记录
        List<PrivilegeHistory> historyRecords = privilegeHistoryRepository
                .findByPrivilege_UsernameOrderByDatetimeDesc(username);

        for (PrivilegeHistory history : historyRecords) {
            if (history.getTicketUid().equals(ticketUid)) {
                // 撤销之前的积分操作
                int reverseAmount = -history.getBalanceDiff();
                String reverseOperationType = history.getOperationType().equals("FILL_IN_BALANCE")
                        ? "DEBIT_THE_ACCOUNT"
                        : "FILL_IN_BALANCE";

                privilege.setBalance(privilege.getBalance() + reverseAmount);
                privilegeRepository.save(privilege);

                // 记录撤销操作
                PrivilegeHistory reverseHistory = new PrivilegeHistory(
                        privilege, ticketUid, reverseAmount, reverseOperationType
                );
                privilegeHistoryRepository.save(reverseHistory);

                // 更新特权状态
                updatePrivilegeStatus(privilege);
                break;
            }
        }
    }

    // 更新特权状态（基于积分余额）
    private void updatePrivilegeStatus(Privilege privilege) {
        int balance = privilege.getBalance();
        String newStatus;

        if (balance >= 1000) {
            newStatus = "GOLD";
        } else if (balance >= 500) {
            newStatus = "SILVER";
        } else {
            newStatus = "BRONZE";
        }

        if (!privilege.getStatus().equals(newStatus)) {
            privilege.setStatus(newStatus);
            privilegeRepository.save(privilege);
        }
    }

    // 内部结果类
    public static class BonusOperationResult {
        private final int paidByMoney;
        private final int paidByBonuses;
        private final Privilege privilege;

        public BonusOperationResult(int paidByMoney, int paidByBonuses, Privilege privilege) {
            this.paidByMoney = paidByMoney;
            this.paidByBonuses = paidByBonuses;
            this.privilege = privilege;
        }

        public int getPaidByMoney() { return paidByMoney; }
        public int getPaidByBonuses() { return paidByBonuses; }
        public Privilege getPrivilege() { return privilege; }
    }
}