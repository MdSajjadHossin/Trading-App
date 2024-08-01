package com.springboot.tradingApp.service;

import com.springboot.tradingApp.enums.VerificationType;
import com.springboot.tradingApp.model.ForgetPasswordToken;
import com.springboot.tradingApp.model.User;
import com.springboot.tradingApp.repository.ForgetPasswordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgetPasswordServiceImpl implements ForgotPasswordService{

    @Autowired
    private ForgetPasswordRepo forgetPasswordRepo;

    @Override
    public ForgetPasswordToken createToken(User user, String otp, String id, VerificationType verificationType, String sendTo) {
        ForgetPasswordToken token = new ForgetPasswordToken();
        token.setUser(user);
        token.setSendTo(sendTo);
        token.setVerificationType(verificationType);
        token.setOtp(otp);
        token.setId(id);
        return forgetPasswordRepo.save(token);
    }

    @Override
    public ForgetPasswordToken findById(String id) {
        Optional<ForgetPasswordToken> token = forgetPasswordRepo.findById(id);
        return token.orElse(null);
    }

    @Override
    public ForgetPasswordToken findByUser(Long userId) {
        return forgetPasswordRepo.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgetPasswordToken token) {
        forgetPasswordRepo.delete(token);
    }
}
