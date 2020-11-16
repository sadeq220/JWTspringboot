package com.sadeqstore.demo.repository;

import com.sadeqstore.demo.model.TokenBucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TokensRepository extends JpaRepository<TokenBucket,String>{
    @Override
    <S extends TokenBucket> S save(S s);

    @Modifying
    @Query(value = "DELETE FROM TokenBucket token WHERE token.timeStamp < CURRENT_TIMESTAMP ")
    Integer staleDelete();

    TokenBucket findByToken(String token);
    Integer deleteByToken(String token);
}
