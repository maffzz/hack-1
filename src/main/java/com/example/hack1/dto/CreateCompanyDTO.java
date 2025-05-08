package com.example.hack1.dto;

import lombok.Data;

@Data
public class CreateCompanyDTO {
    private String ruc;
    private String name;
    private boolean active;
    private UserDTO admin;
}
