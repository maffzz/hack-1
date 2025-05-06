package com.example.hack1.domain;

import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sparky {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "sparky", cascade = CascadeType.ALL)
    private List<Empresa> empresas;}