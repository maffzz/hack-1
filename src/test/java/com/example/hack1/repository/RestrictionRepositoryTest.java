package com.example.hack1.repository;

import com.example.hack1.domain.ModelRestriction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class RestrictionRepositoryTest {

    @Autowired
    private ModelRestrictionRepository repo;

    @Test
    void findByCompanyId() {
        repo.save(new ModelRestriction());
        var list = repo.findByCompanyId(2L);

        assertThat(list).hasSize(1)
                .first().extracting("modelType")
                .isEqualTo("m");
    }
}