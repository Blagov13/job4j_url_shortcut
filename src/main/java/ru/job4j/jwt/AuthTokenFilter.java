package ru.job4j.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthTokenFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtUtils.resolveToken(request);
        try {
            if (token != null && jwtUtils.validateToken(token)) {
                SecurityContextHolder.getContext().setAuthentication(jwtUtils.getAuthentication(token));
            }
        } catch (Exception e) {
            logger.error("Невозможно установить аутентификацию пользователя: {}", e);
        }
        filterChain.doFilter(request, response);
    }
}
