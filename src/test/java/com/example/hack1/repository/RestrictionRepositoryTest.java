package com.example.hack1.repository;

import com.example.hack1.model.Restriction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class RestrictionRepositoryTest {

    @Autowired
    private RestrictionRepository repo;

    @Test
    void findByCompanyId() {
        repo.save(new Restriction(null, 2L, "m", 10));
        var list = repo.findByCompanyId(2L);

        assertThat(list).hasSize(1)
                .first().extracting("modelType")
                .isEqualTo("m");
    }
}