package com.example.hack1.repository;

import com.example.hack1.domain.User;
import com.example.hack1.domain.UserLimit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserLimitRepository extends JpaRepository<UserLimit, Long> {
    List<UserLimit> findByUserId(Long userId);
    Optional<UserLimit> findByUserAndModelProviderAndModel(User user, String modelProvider, String model);

}