package com.example.hack1.controller;

import com.example.hack1.dto.CompanyDto;
import com.example.hack1.service.CompanyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminCompanyController.class)
class AdminCompanyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CompanyService companyService;

    @Test
    @DisplayName("POST /api/admin/companies devuelve 201 con el ID")
    void createCompany() throws Exception {
        CompanyDto saved = new CompanyDto(1L, "Acme Corp", "123456789", LocalDate.now(), true);
        when(companyService.createCompany(any())).thenReturn(saved);

        mvc.perform(post("/api/admin/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "Acme Corp",
                      "ruc": "123456789"
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Acme Corp"));

        verify(companyService, times(1)).createCompany(any());
    }

    @Test
    @DisplayName("GET /api/admin/companies lista todas las empresas")
    void listCompanies() throws Exception {
        when(companyService.listAll())
                .thenReturn(List.of(
                        new CompanyDto(1L, "A", "111", LocalDate.now(), true),
                        new CompanyDto(2L, "B", "222", LocalDate.now(), false)));

        mvc.perform(get("/api/admin/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(companyService).listAll();
    }

    // Puedes añadir tests para GET/{id}, PUT/{id}, PATCH/{id}/status y GET/{id}/consumption
}