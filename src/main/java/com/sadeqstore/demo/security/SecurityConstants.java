package com.sadeqstore.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {
    @Value("${security.secret:SadeqPasswd}")
     String SECRET;
    @Value("${security.header:Authorization}")
     String HEADER_STRING;
    @Value("${security.expire-length:3600000}")
     Long ValidityInMilliSeconds;//1h
    @Value("${security.token:Bearer }")
     String TOKEN_PREFIX;
}
