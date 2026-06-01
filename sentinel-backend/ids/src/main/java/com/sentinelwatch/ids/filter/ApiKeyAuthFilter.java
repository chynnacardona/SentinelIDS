package com.sentinelwatch.ids.filter;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final String apiKeyHeaderName = "X-API-KEY";
    private final String expectedApiKey = "SentinelWatch-IDS-Key-Tester";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/api/alerts")) {
            String requestApiKey = request.getHeader(apiKeyHeaderName);

            if (requestApiKey == null || !requestApiKey.equals(expectedApiKey)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Unauthorized: Invalid or missing API Key.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}