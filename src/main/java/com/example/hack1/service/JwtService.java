package com.example.hack1.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {
    @Value("${my.awesome.secret}")
    private String jwtSigningKey;

    // Extrae el nombre de usuario desde el token JWT
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrae el rol del usuario desde el token JWT
    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // Genera un token para un usuario autenticado
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        claims.put("role", role);
        System.out.println("📌 Generando token para usuario: " + userDetails.getUsername() + " con rol: " + role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expira en 24 horas
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }


    // Verifica si el token es válido y si pertenece al usuario correcto
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        final String userRole = extractUserRole(token);

        System.out.println("🔹 Usuario extraído del token: " + userName);
        System.out.println("🔹 Usuario esperado: " + userDetails.getUsername());
        System.out.println("🔹 Rol extraído del token: " + userRole);
        System.out.println("🔹 ¿El token está expirado?: " + isTokenExpired(token));

        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    // Extrae un claim específico del token JWT
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    // Genera un token con datos extras y una duración de 24 horas
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims) // Agrega información adicional al token
                .setSubject(userDetails.getUsername()) // Define el usuario dueño del token
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expira en 24 horas
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // 🚀 Firma el token con la clave secreta
                .compact();
    }

    // Verifica si el token ha expirado
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extrae la fecha de expiración del token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Obtiene todos los claims (datos) almacenados dentro del token JWT
    private Claims extractAllClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Usa la clave secreta para decodificar el token
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("✅ Claims extraídos correctamente: " + claims);
            return claims;
        } catch (Exception e) {
            System.out.println("🚨 Error al extraer claims: " + e.getMessage());
            throw e;
        }
        }

    // Convierte la clave secreta en un objeto `Key` para firmar y validar tokens
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);

        System.out.println("📌 Clave secreta usada para validar tokens: " + jwtSigningKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

}