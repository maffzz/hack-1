package com.example.hack1.controller;

import com.example.hack1.dto.RestrictionDto;
import com.example.hack1.service.ModelRestrictionService;
import com.example.hack1.service.RestrictionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyRestrictionsController.class)
class CompanyRestrictionsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ModelRestrictionService restrictionService;

    @Test
    void createRestriction() throws Exception {
        RestrictionDto out = new RestrictionDto(5L, 1L, "gpt-4", 1000);
        when(restrictionService.create(any())).thenReturn(out);

        mvc.perform(post("/api/company/restrictions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"companyId":1,"modelType":"gpt-4","limit":1000}
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void listRestrictions() throws Exception {
        when(restrictionService.listByCompany(1L))
                .thenReturn(List.of(new RestrictionDto(1L,1L,"gpt-4",500)));

        mvc.perform(get("/api/company/restrictions?companyId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // Añade tests para PUT y DELETE si los implementas
}