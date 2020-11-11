package com.sadeqstore.demo.repository;

import com.sadeqstore.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface MyRepository extends JpaRepository<com.sadeqstore.demo.model.Product,String> {

    @Override
    List<Product> findAll();
    List<Product> findAllByNameIsLike(String regexName);
}
