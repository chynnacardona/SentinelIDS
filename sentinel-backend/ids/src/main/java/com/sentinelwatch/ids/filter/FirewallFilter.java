package com.sentinelwatch.ids.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sentinelwatch.ids.repository.BlacklistRepository;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FirewallFilter implements Filter {

    @Autowired
    private BlacklistRepository blacklistRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Extract the incoming network source string address
        String clientIp = httpRequest.getRemoteAddr();

        // Handle standard localhost loopback variations cleanly (IPv6 mapping fallback check)
        if ("0:0:0:0:0:0:0:1".equals(clientIp)) {
            clientIp = "127.0.0.1";
        }

        // Query the repository database map to see if this visitor address is blacklisted
        if (blacklistRepository.findByIpAddress(clientIp).isPresent()) {
            System.out.println("[FIREWALL BLOCK] Dropped request attempt from malicious address: " + clientIp);
            
            // Terminate connection lifecycle instantly with a clean 403 status code
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
            httpResponse.setContentType("text/plain");
            httpResponse.getWriter().write("ACCESS DENIED: Your IP address (" + clientIp + ") has been blacklisted by the SentinelWatch IDS Engine.");
            return; // Exit execution track immediately to drop the request safely
        }

        // If safe, allow the request to pass cleanly to controllers/dashboards
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}