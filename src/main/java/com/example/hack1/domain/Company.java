package com.example.hack1.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String ruc;

    private String name;
    private LocalDate affiliationDate;
    private boolean active;

    @OneToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @OneToMany(mappedBy = "company")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "company")
    private List<ModelRestriction> restrictions = new ArrayList<>();}
