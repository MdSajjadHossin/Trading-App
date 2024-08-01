package com.springboot.tradingApp.service;

import com.springboot.tradingApp.enums.VerificationType;
import com.springboot.tradingApp.model.ForgetPasswordToken;
import com.springboot.tradingApp.model.User;

public interface ForgotPasswordService {

    ForgetPasswordToken createToken(User user, String otp, String id, VerificationType verificationType, String sendTo);
    ForgetPasswordToken findById(String id);
    ForgetPasswordToken findByUser(Long userId);
    void deleteToken(ForgetPasswordToken token);

}
