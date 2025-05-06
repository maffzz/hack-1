package com.example.hack1.repository;

public interface UserLimitRepository extends JpaRepository<UserLimit, Long> {
    List<UserLimit> findByUserId(Long userId);
    Optional<UserLimit> findByUserAndModelProviderAndModelName(
            User user, String provider, String modelName);
}