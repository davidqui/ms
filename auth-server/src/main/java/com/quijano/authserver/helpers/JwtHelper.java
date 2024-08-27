package com.quijano.authserver.helpers;

import com.quijano.authserver.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtHelper {

    @Value("${application.jwt.secret}")
    private String jwtSecret;

    private static final int MIN_SECRET_LENGTH = 32; // 32 bytes = 256 bits

    private SecretKey getSecretKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);

        // Verificar si la longitud es suficiente, si no, lanzar una excepción o manejarlo según la lógica de la aplicación.
        if (keyBytes.length < MIN_SECRET_LENGTH) {
            throw new IllegalArgumentException("La longitud de la clave secreta es insuficiente. Debe ser de al menos 32 caracteres.");
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String username){
        final var now = new Date();
        final var expirationDate = new Date(now.getTime() + (3600*1000));
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(this.getSecretKey())
                .compact();
    }

    public boolean validateToken(String token){
        try{
            final var expirationDate = this.getExpirationDate(token);
            return expirationDate.after(new Date());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Jwts invalid");
        }
    }

    private Date getExpirationDate(String token){
        return this.getClaimsFormToken(token, Claims::getExpiration);
    }

    private <T> T getClaimsFormToken(String token, Function<Claims, T> resolver){
        return resolver.apply(this.signToken(token));
    }

    private Claims signToken(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(this.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
