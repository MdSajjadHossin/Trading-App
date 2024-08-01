package com.springboot.tradingApp.repository;


import com.springboot.tradingApp.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepo extends JpaRepository<VerificationCode, Long> {

    public VerificationCode findByUserId(Long userId);
}
