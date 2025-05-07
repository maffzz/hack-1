package com.example.hack1.repository;

import com.example.hack1.domain.AIRequest;
import com.example.hack1.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AIRequestRepository extends JpaRepository<AIRequest, Long> {
    // Para reportes e historial
    List<AIRequest> findByCompanyId(Long companyId);
    List<AIRequest> findByUserIdOrderByTimestampDesc(Long userId);
    List<AIRequest> findByUserIdAndCompanyId(Long userId, Long companyId);

    List<AIRequest> findByUserOrderByTimestampDesc(User user);
}