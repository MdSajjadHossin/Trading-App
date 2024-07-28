package com.springboot.tradingApp.service;

import com.springboot.tradingApp.model.TwoFactorOTP;
import com.springboot.tradingApp.model.User;

public interface TwoFactorOtpService {

    TwoFactorOTP createOtp(User user, String otp, String jwt);

    TwoFactorOTP findByUser(Long userId);

    TwoFactorOTP findById(String id);

    boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp);

    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP);

}
