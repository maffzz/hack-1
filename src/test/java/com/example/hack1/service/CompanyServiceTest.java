package com.example.hack1.service;

import com.example.hack1.dto.CompanyDto;
import com.example.hack1.model.Company;
import com.example.hack1.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository repo;

    @InjectMocks
    private CompanyService service;

    @Test
    void createCompany() {
        Company saved = new Company(1L, "X", "000", LocalDate.now(), true);
        when(repo.save(any())).thenReturn(saved);

        CompanyDto dto = service.createCompany(new CompanyDto(null,"X","000",null,true));
        assertThat(dto.getId()).isEqualTo(1L);
        verify(repo).save(any(Company.class));
    }

    @Test
    void getById_notFound() {
        when(repo.findById(42L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getById(42L))
                .isInstanceOf(javax.persistence.EntityNotFoundException.class);
    }

    // Completa con listAll, update y toggleStatus...
}