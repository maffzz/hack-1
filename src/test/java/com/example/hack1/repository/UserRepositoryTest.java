package com.example.hack1.repository;

import com.example.hack1.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    @Test
    void findByCompanyId() {
        repo.save(new User());
        var list = repo.findByCompanyId(3L);

        assertThat(list).extracting("email").containsExactly("x@y.com");
    }
}