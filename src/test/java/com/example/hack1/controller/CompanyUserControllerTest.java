package com.example.hack1.controller;

import com.example.hack1.dto.UserDto;
import com.example.hack1.service.UserService;
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

@WebMvcTest(CompanyUserController.class)
class CompanyUserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    void createUser() throws Exception {
        UserDto out = new UserDto(7L,1L,"juan@mail.com");
        when(userService.create(any())).thenReturn(out);

        mvc.perform(post("/api/company/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"companyId":1,"email":"juan@mail.com"}
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void listUsers() throws Exception {
        when(userService.listByCompany(1L))
                .thenReturn(List.of(new UserDto(7L,1L,"a@b.com")));

        mvc.perform(get("/api/company/users?companyId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // Completa con tests para GET/{id}, PUT, POST limits y GET consumption
}