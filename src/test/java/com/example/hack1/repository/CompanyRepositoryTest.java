package com.example.hack1.repository;

import com.example.hack1.domain.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository repo;

    @Test
    void saveAndFindById() {
        Company c = new Company();
        Company saved = repo.save(c);
        var found = repo.findById(saved.getId());

        assertThat(found).isPresent()
                .get().extracting(Company::getRuc)
                .isEqualTo("999");
    }
}