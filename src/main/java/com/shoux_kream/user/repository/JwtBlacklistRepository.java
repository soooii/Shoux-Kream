package com.shoux_kream.user.repository;

import com.shoux_kream.user.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface JwtBlacklistRepository extends JpaRepository<BlacklistToken, String> {

    boolean existsByJti(String jti);

    // 만료된 토큰 삭제 메서드
    @Modifying
    @Query("DELETE FROM BlacklistToken b WHERE b.expirationTime < :currentDate")
    void deleteAllByExpirationTimeBefore(@Param("currentDate") Date currentDate);
}
