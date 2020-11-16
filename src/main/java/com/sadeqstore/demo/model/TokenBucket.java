package com.sadeqstore.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Entity
public class TokenBucket {
    @Id
    private String token;
    private String productName;
    private String username;
    private LocalDateTime timeStamp;
    @Transient
    private Integer expirationMin =20;

    public TokenBucket(){
        timeStamp=LocalDateTime.now().plusMinutes(expirationMin);
    }
    public TokenBucket(String productName, String username,String token){
        this.token=token;
        this.productName=productName;
        this.username=username;
        timeStamp=LocalDateTime.now().plusMinutes(expirationMin);
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
