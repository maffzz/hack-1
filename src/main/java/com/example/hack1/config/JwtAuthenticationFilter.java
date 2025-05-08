package com.example.hack1.config;

import com.example.hack1.service.JwtService;
import com.example.hack1.service.UserDetailsServicelmpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsServicelmpl userService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServicelmpl userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        System.out.println("🔎 Analizando solicitud a: " + requestURI);

        if (requestURI.startsWith("/auth/")) {
            System.out.println("🚀 Ignorando filtro JWT en ruta pública: " + requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        System.out.println("📌 Cabecera Authorization recibida: " + authHeader);

        if (!StringUtils.hasText(authHeader) || !authHeader.trim().startsWith("Bearer ")) {
            System.out.println("🚨 No hay token válido, dejando pasar como ANONYMOUS.");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUserName(jwt);
        final String userRole = jwtService.extractUserRole(jwt);

        System.out.println("📌 Usuario extraído del token: " + userEmail);
        System.out.println("📌 Rol extraído del token: " + userRole);

        if (StringUtils.hasText(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.loadUserByUsername(userEmail);

            System.out.println("🔍 Buscando usuario en DB: " + userEmail);
            System.out.println("🔍 Roles asignados al usuario en DB: " + userDetails.getAuthorities());

            if (jwtService.isTokenValid(jwt, userDetails)) {
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userRole));

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities);

                System.out.println("✅ Autenticación aplicada correctamente con roles: " + authorities);

                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                System.out.println("🚨 Token inválido.");
            }
        }

        filterChain.doFilter(request, response);
    }
}
