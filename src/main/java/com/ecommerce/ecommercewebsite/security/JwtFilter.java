package com.ecommerce.ecommercewebsite.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    // PUBLIC endpoints (NO JWT REQUIRED)
    private static final List<String> PUBLIC_URLS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/verify-otp",
            "/api/auth/resend-otp",
            "/api/auth/forget-password",
            "/api/auth/reset-password",

            "/api/all-districts",
            "/api/all-categories",
            "/api/sort-products/",
            "/api/product/",
            "/api/products/",
            "/api/all-products",
            "/api/epay/login"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String method = request.getMethod();
        String path = request.getRequestURI();

        System.out.println("======================================");
        System.out.println("JWT FILTER REQUEST: " + method + " " + path);

        // 1. Allow preflight
        if ("OPTIONS".equalsIgnoreCase(method)) {
            System.out.println("OPTIONS REQUEST - ALLOWED");
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Skip public APIs
        if (isPublic(path)) {
            System.out.println("PUBLIC URL - JWT SKIPPED");
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Get Authorization header
        String authHeader = request.getHeader("Authorization");

        System.out.println("AUTH HEADER PRESENT: " + (authHeader != null));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("NO VALID BEARER TOKEN FOUND");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        System.out.println("TOKEN RECEIVED: YES");

        String username;

        try {
            username = jwtUtil.extractUsername(token);

            System.out.println("USERNAME FROM TOKEN: " + username);

        } catch (Exception e) {

            System.out.println("JWT EXTRACTION FAILED");
            System.out.println("JWT ERROR: " + e.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Invalid token\"}");
            return;
        }

        // 4. Set authentication
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            System.out.println("LOADING USER DETAILS...");

            if (jwtUtil.isTokenExpired(token)) {

                System.out.println("TOKEN IS EXPIRED");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Token expired\"}");
                return;
            }

            UserDetails userDetails;

            try {

                userDetails =
                        userDetailsService.loadUserByUsername(username);

                System.out.println("USER LOADED: " + userDetails.getUsername());
                System.out.println("USER AUTHORITIES: " + userDetails.getAuthorities());

            } catch (Exception e) {

                System.out.println("USER DETAILS LOADING FAILED");
                System.out.println("USER DETAILS ERROR: " + e.getMessage());

                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder.getContext()
                    .setAuthentication(authToken);

            System.out.println("AUTHENTICATION SET SUCCESSFULLY");
            System.out.println(
                    "SECURITY CONTEXT AUTHORITIES: " +
                            SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .getAuthorities()
            );
        }

        System.out.println("CONTINUING TO CONTROLLER");
        System.out.println("======================================");

        filterChain.doFilter(request, response);
    }

    // Helper method
    private boolean isPublic(String path) {
        return PUBLIC_URLS.stream().anyMatch(path::startsWith);
    }
}