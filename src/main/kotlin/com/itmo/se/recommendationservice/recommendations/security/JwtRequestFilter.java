package com.itmo.se.recommendationservice.recommendations.security;

import com.itmo.se.recommendationservice.user.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Supplier;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            readTokenIfProvided(req);
        } catch (Exception e) {
            log.error("Cannot parse token", e);
        }
        chain.doFilter(req, res);
    }

    private void readTokenIfProvided(HttpServletRequest req) {
        String tokenString = req.getHeader("Authorization");
        if (tokenString == null || !tokenString.startsWith("Bearer "))
            return;
        String token = tokenString.substring("Bearer ".length());
        AuthToken authToken = jwtUtil.readToken(token);
        Supplier<AppUser> userProvider = () -> new AppUser(authToken.getId(), authToken.getRole());
        SecurityContextHolder.getContext().setAuthentication(authToken.createAuthentication(userProvider));
        log.debug("User {} authorized", authToken.getUserId());
    }
}