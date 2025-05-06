package com.example.hack1.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_usuario", unique = true, nullable = false)
    private String nombreDeUsuario;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private Boolean expired = false;

    @Column(nullable = false)
    private Boolean locked = false;

    @Column(name = "credentials_expired", nullable = false)
    private Boolean credentialsExpired = false;

    @Column(nullable = false)
    private Boolean enabled = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LimiteUsuario> limites;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<SolicitudIA> solicitudes;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return nombreDeUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}