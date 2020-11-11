package com.sadeqstore.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JWTokenFilter extends OncePerRequestFilter {
    private JWTTokenProvider tokenProvider;
    @Autowired
    public JWTokenFilter(JWTTokenProvider tokenProvider){
        this.tokenProvider=tokenProvider;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token=tokenProvider.resolveToken(httpServletRequest);
        try{
            if(token!=null && tokenProvider.validateToken(token)){
                Authentication auth = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("authentication set to securityContext");
            }
        }catch (ResponseStatusException ex){
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(ex.getStatus().value(), ex.getMessage());
            return;
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
