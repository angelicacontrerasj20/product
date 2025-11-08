package com.codecraft.product.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Date;
import java.security.Key;

/**
 * Utilidad para generación y validación de JWT.
 */
public class JwtUtil {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora

    /**
     * Genera un token JWT para el usuario dado.
     * @param username Nombre de usuario.
     * @return Token JWT.
     */
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Extrae el nombre de usuario del token JWT.
     * @param token Token JWT.
     * @return Nombre de usuario o null si el token es inválido o expirado.
     */
    public static String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // Usar parser() para compatibilidad con versiones antiguas
            return claims.getSubject();
        } catch (ExpiredJwtException ex) {
            // Token expirado
            return null;
        } catch (Exception ex) {
            // Token inválido
            return null;
        }
    }
}
