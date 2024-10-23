package com.shoux_kream.user.service;

import com.shoux_kream.user.repository.JwtBlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final JwtBlacklistRepository jwtBlacklistRepository;

    // 해당 jti가 블랙리스트에 존재하는지 확인
    public boolean isBlacklisted(String jti) {
        return jwtBlacklistRepository.existsByJti(jti);
    }

    // 매일 정각에 만료된 토큰 삭제
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpiredTokens() {
        Date currentDate = new Date();
        jwtBlacklistRepository.deleteAllByExpirationTimeBefore(currentDate);
    }
}
