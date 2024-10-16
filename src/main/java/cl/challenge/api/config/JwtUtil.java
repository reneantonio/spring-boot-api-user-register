package cl.challenge.api.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

@Component
public class JwtUtil {

    // Inyectar el SECRET_KEY desde application.properties
    @Value("${jwt.secret.key}")
    private String secretKey;
    
    // Método para generar la clave
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    // Método para generar el JWT
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)  // Usamos el email como sujeto del token
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // Expiración en 10 horas
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // Firma usando la clave segura
                .compact();
    }
}
