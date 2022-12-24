package com.itmo.se.recommendationservice.recommendations.security;

import com.itmo.se.recommendationservice.config.props.SecurityProperties;
import com.itmo.se.recommendationservice.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final SecurityProperties securityProperties;
    public AuthToken readToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(securityProperties.getSecret().getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();
        long id = Long.parseLong(claims.getSubject());
        UserRole role = UserRole.valueOf(claims.get("role", String.class));
        return AuthToken.builder()
                .userId(id)
                .role(role)
                .build();
    }

    public String createToken(AuthToken data) {
        return Jwts.builder()
                .setSubject(String.valueOf(data.getId()))
                .setIssuedAt(new Date())
                .setExpiration(Date.from(
                        Instant.now().plus(securityProperties.getTokenTtl()))
                )
                .claim("role", data.getRole())
                .signWith(Keys.hmacShaKeyFor(securityProperties.getSecret().getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
