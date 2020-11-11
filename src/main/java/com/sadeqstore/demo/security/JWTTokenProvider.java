package com.sadeqstore.demo.security;
import java.util.Date;

import com.sadeqstore.demo.model.Role;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Component
public class JWTTokenProvider {
    private MyUserDetails myUserDetails;
    private SecurityConstants securityConstants;
@Autowired
public JWTTokenProvider(MyUserDetails myUserDetails, SecurityConstants securityConstants){
    this.securityConstants=securityConstants;
    this.myUserDetails=myUserDetails;
}
    public String createToken(String username, Role role) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", role.getAuthority());

        Date now = new Date();
        Date validity = new Date(now.getTime() + securityConstants.ValidityInMilliSeconds);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, securityConstants.SECRET)//
                .compact();
    }
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(securityConstants.SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        //System.out.println(securityConstants.TOKEN_PREFIX+"se");
        String bearerToken = req.getHeader(securityConstants.HEADER_STRING);
        if (bearerToken != null && bearerToken.startsWith(securityConstants.TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(securityConstants.SECRET).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"Expired or invalid JWT token");
        }
    }
    public Authentication getAuthentication(String token) {
        //Role role=Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody().get("auth",Role.class);
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
