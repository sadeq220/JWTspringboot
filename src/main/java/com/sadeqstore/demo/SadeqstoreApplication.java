package com.sadeqstore.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sadeqstore.demo.model.TokenBucket;
import com.sadeqstore.demo.repository.TokensRepository;
import com.sadeqstore.demo.security.SecurityConfig;
import com.sadeqstore.demo.swaggerConfig.SwConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Import({SecurityConfig.class, SwConfig.class})
@EnableScheduling
public class SadeqstoreApplication{
    @Autowired
    private TokensRepository tokensRepository;
    public static void main(String[] args) {
        SpringApplication.run(SadeqstoreApplication.class, args);
    }
    @Bean
    public ObjectMapper objectMapper(){return new ObjectMapper();}

    @Scheduled(fixedDelay = 1000*60*30,initialDelay = 1000*60)
    public void deleteStaleTokens(){
        tokensRepository.staleDelete();
    }
}
