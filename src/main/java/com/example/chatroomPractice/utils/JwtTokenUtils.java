package com.example.chatroomPractice.utils;

import com.example.chatroomPractice.model.User;
import io.jsonwebtoken.*;
import jakarta.security.auth.message.AuthException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component("jwtTokenUtils")
public class JwtTokenUtils {
    private static final long EXPIRATION = 30 * 60 * 1000;
    private static final String SECRET = "jwt-token-secret";

    public String generateToken(User user) {
        Claims claims = Jwts.claims();
        claims.put("userId", user.getId());

        return Jwts
            .builder()
            .setClaims(claims)
            .setExpiration(new Date(Instant.now().toEpochMilli() + EXPIRATION))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
    }

    public void validateToken(String token) throws AuthException {
        try {
            Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token);
        } catch (SignatureException e) {
            throw new AuthException("Invalid JWT signature.");
        }
        catch (MalformedJwtException e) {
            throw new AuthException("Invalid JWT token.");
        }
        catch (ExpiredJwtException e) {
            throw new AuthException("Expired JWT token");
        }
        catch (UnsupportedJwtException e) {
            throw new AuthException("Unsupported JWT token");
        }
        catch (IllegalArgumentException e) {
            throw new AuthException("JWT token compact of handler are invalid");
        }
    }
}
