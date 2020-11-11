package com.sadeqstore.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadeqstore.demo.model.Role;
import com.sadeqstore.demo.model.User;
import com.sadeqstore.demo.security.SecurityConfig;
import com.sadeqstore.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SecurityConfig.class)
public class SadeqstoreApplication{

    public static void main(String[] args) {
        SpringApplication.run(SadeqstoreApplication.class, args);
    }
    @Bean
    public ObjectMapper objectMapper(){return new ObjectMapper();}

}
