package com.example.hack1.service;

import com.example.hack1.dto.RestrictionDto;
import com.example.hack1.model.Restriction;
import com.example.hack1.repository.RestrictionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestrictionServiceTest {

    @Mock
    private RestrictionRepository repo;

    @InjectMocks
    private RestrictionService service;

    @Test
    void create() {
        Restriction saved = new Restriction(5L,1L,"m",100);
        when(repo.save(any())).thenReturn(saved);

        RestrictionDto out = service.create(new RestrictionDto(null,1L,"m",100));
        assertThat(out.getId()).isEqualTo(5L);
        verify(repo).save(any(Restriction.class));
    }

    // Añade tests para listByCompany, update y delete...
}