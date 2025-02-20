package com.pet.petshop.auth.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")  
    private String SECRET_KEY;

    // Método para generar un token, incluyendo el rol del usuario
    public String getToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("email", user.getUsername());  
        
        // Agregar roles con el prefijo "ROLE_"
        String roles = user.getAuthorities().stream()
                            .map(authority -> authority.getAuthority())
                            .collect(Collectors.joining(","));
        claims.put("roles", roles);  // Almacenar los roles como una cadena separada por comas

        return getToken(claims, user);
    }

    // Método para construir el token con claims extra y el usuario
    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(user.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 10))  // Expiración en 10 años
            .signWith(getKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    // Obtener la clave secreta para firmar el token
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);  // Decodifica la clave secreta
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Obtener el nombre de usuario desde el token
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    // Verificar si el token es válido para un usuario
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Obtener todos los claims desde el token
    private Claims getAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getKey())  // Verifica la firma con la clave secreta
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    // Obtener un claim específico del token
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);  // Extrae un claim específico
    }

    // Obtener la fecha de expiración del token
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    // Verificar si el token ha expirado
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());  // Verifica si el token ha expirado
    }

    // Método adicional para obtener los roles desde el token
    public String getRolesFromToken(String token) {
        return getClaim(token, claims -> claims.get("roles", String.class));
    }
}
