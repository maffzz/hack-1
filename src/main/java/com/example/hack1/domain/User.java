package com.example.hack1.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "usuarios")
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private Role role;

    @OneToMany(mappedBy = "user")
    private List<UserLimit> limits = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<AIRequest> requests = new ArrayList<>();

    private Integer tokensUsed = 0; // Tokens usados en total

    public void addTokensUsed(int tokens) {
        this.tokensUsed += tokens;}

    private Boolean expired = false;

    private Boolean locked = false;

    private Boolean credentialsExpired = false;

    private Boolean enable = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));}

    @Override
    public String getUsername() {
        return email;}

    @Override
    public String getPassword() {
        return password;}

    @Override
    public boolean isAccountNonExpired() {
        return !expired;}

    @Override
    public boolean isAccountNonLocked() {
        return !locked;}

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;}

    @Override
    public boolean isEnabled() {
        return enable;}}