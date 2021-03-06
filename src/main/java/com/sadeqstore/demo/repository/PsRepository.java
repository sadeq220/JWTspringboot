package com.sadeqstore.demo.repository;

import com.sadeqstore.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PsRepository extends JpaRepository<com.sadeqstore.demo.model.Product,String> {

    @Override
    Page<Product> findAll(Pageable pageable);
    List<Product> findAllByNameLike(String regexName);
    Integer deleteByName(String name);
    Product findByName(String productName);

    @Modifying
    @Query(value = "UPDATE ps p SET p.name=:pN,p.cost=:pC WHERE p.name=:pON")
    Integer updateP(@Param(value = "pON") String oldName,@Param(value = "pN") String pName,@Param(value = "pC") Double pCost);

    @Query(value = "SELECT p from ps p")
    Slice<Product> products(Pageable pageable);
    @Override
    <S extends Product> S save(S s);
}
