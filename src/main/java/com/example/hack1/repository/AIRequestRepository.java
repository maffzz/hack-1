package com.example.hack1.repository;

public interface AIRequestRepository extends JpaRepository<AIRequest, Long> {
    // Para reportes e historial
    List<AIRequest> findByCompanyId(Long companyId);
    List<AIRequest> findByUserIdOrderByTimestampDesc(Long userId);
}