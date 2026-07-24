package com.ecommerce.orderservice.security;


import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

	public JwtAuthenticationFilter(JwtService jwtService) {

		this.jwtService = jwtService;
	}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {

			filterChain.doFilter(request, response);
			return;
		}

        String token = authHeader.substring(7);

        String username = jwtService.extractUsername(token);
        
     // DEBUG
        System.out.println("Username from JWT: " + username);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, null);

			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			 // DEBUG
			
			 System.out.println("Authentication set successfully");

		}

        filterChain.doFilter(request,response);

    }
}