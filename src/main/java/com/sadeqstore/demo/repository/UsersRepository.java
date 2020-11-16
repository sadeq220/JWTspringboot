package com.sadeqstore.demo.repository;

import com.sadeqstore.demo.model.Product;
import com.sadeqstore.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UsersRepository extends JpaRepository<com.sadeqstore.demo.model.User,String>,UpdateUserProduct {
    User findByName(String name);
    Boolean existsByName(String name);
    Integer deleteByName(String name);
    @Override
    <S extends User> S save(S s);

    @Override
    void updateUserPs(String username, Product product);
}