package com.sadeqstore.demo.repository;

import com.sadeqstore.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface MyRepository extends JpaRepository<com.sadeqstore.demo.model.Product,String> {

    @Override
    List<Product> findAll();
    List<Product> findAllByNameLike(String regexName);
    Integer deleteByName(String name);

    @Modifying
    @Query(value = "UPDATE ps p SET p.name=:pN,p.cost=:pC WHERE p.name=:pON")
    Integer updateP(@Param(value = "pON") String oldName,@Param(value = "pN") String pName,@Param(value = "pC") Integer pCost);

    @Override
    <S extends Product> S save(S s);
}
