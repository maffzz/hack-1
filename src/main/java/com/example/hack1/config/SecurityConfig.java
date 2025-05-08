package com.example.hack1.config;

import com.example.hack1.service.UserDetailsServicelmpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    UserDetailsServicelmpl userDetailsService;


    // 📌 Es como definir qué puertas son de acceso libre y cuáles necesitan tarjeta de seguridad para pasar.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable). //🚨 Desactiva la protección CSRF (innecesario en APIs REST)
               authorizeHttpRequests(autorize -> autorize
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/login/**").hasAnyAuthority("ROLE_USER", "ROLE_SPARKY_ADMIN", "ROLE_COMPANY_ADMIN") // 🔒 Restringir acceso a roles
                .requestMatchers("/users/**").hasAnyAuthority("ROLE_USER", "ROLE_SPARKY_ADMIN", "ROLE_COMPANY_ADMIN") // 🔒 Restringir acceso a roles
        ).sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(new OncePerRequestFilter() {
                    @Override
                    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                            throws ServletException, IOException {
                        System.out.println("🔎 Seguridad revisando acceso a: " + request.getRequestURI());
                        System.out.println("🔎 Authentication actual: " + SecurityContextHolder.getContext().getAuthentication());
                        filterChain.doFilter(request, response);
                    }
                }, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    // 📌 Es como un sistema de verificación biométrica, que revisa si la huella digital (credenciales del usuario) es válida antes de permitir el acceso.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService.userDetailsService());

        return authProvider;
    }

    //📌 En vez de guardar "MiContraseña123", guardará algo como "$2a$10$H4ljif3...", lo que impide que alguien vea las contraseñas reales.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //📌 Si USER solo puede ver información, pero ADMIN puede modificarla, ADMIN automáticamente tendrá ambos permisos.
    @Bean
    static RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_SPARKY_ADMIN > ROLE_COMPANY_ADMIN > ROLE_USER");

        return hierarchy;
    }


//    ✅ Permite que @PreAuthorize("hasRole('ADMIN')") funcione correctamente en métodos protegidos.
//    ✅ Elimina el prefijo ROLE_, así puedes escribir ADMIN en vez de ROLE_ADMIN.

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        expressionHandler.setDefaultRolePrefix("");

        return expressionHandler;
    }

    //📌 Cuando un usuario ingresa email y contraseña en signin(), este manager valida si son correctos antes de generar su JWT.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
