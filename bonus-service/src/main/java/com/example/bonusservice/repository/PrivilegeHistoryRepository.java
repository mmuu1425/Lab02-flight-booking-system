package com.example.bonusservice.repository;

import com.example.bonusservice.entity.PrivilegeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrivilegeHistoryRepository extends JpaRepository<PrivilegeHistory, Integer> {
    // 错误：findByPrivilegeUsernameOrderByDatetimeDesc
    // 正确：findByPrivilege_UsernameOrderByDatetimeDesc
    List<PrivilegeHistory> findByPrivilege_UsernameOrderByDatetimeDesc(String username);

    boolean existsByTicketUid(UUID ticketUid);
}