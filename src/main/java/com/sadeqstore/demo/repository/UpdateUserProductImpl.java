package com.sadeqstore.demo.repository;

import com.sadeqstore.demo.model.Product;
import com.sadeqstore.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class UpdateUserProductImpl implements UpdateUserProduct {
    private EntityManager entityManager;
    @Autowired
    public UpdateUserProductImpl(EntityManager entityManager){
        this.entityManager=entityManager;
    }
    @Override
    public void updateUserPs(String username, Product product) {
    User user=entityManager.find(User.class,username);
    user.getBoughtPs().add(product);
    }
}
