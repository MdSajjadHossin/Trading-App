package com.springboot.tradingApp.repository;

import com.springboot.tradingApp.model.TwoFactorOTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorOtpRepo extends JpaRepository<TwoFactorOTP, String> {

    TwoFactorOTP findByUserId(Long userId);

}
