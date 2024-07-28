package com.springboot.tradingApp.service;

import com.springboot.tradingApp.model.TwoFactorOTP;
import com.springboot.tradingApp.model.User;
import com.springboot.tradingApp.repository.TwoFactorOtpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService{

    @Autowired
    private TwoFactorOtpRepo twoFactorOtpRepo;

    @Override
    public TwoFactorOTP createOtp(User user, String otp, String jwt) {

        UUID uuid = UUID.randomUUID();

        String id = uuid.toString();

        TwoFactorOTP twoFactorOTP = new TwoFactorOTP();
        twoFactorOTP.setOtp(otp);
        twoFactorOTP.setJwt(jwt);
        twoFactorOTP.setId(id);
        twoFactorOTP.setUser(user);
        return twoFactorOtpRepo.save(twoFactorOTP);
    }

    @Override
    public TwoFactorOTP findByUser(Long userId) {
        return twoFactorOtpRepo.findByUserId(userId);
    }

    @Override
    public TwoFactorOTP findById(String id) {
        Optional<TwoFactorOTP> otp = twoFactorOtpRepo.findById(id);
        return otp.orElse(null);
    }

    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp) {
        return twoFactorOTP.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP) {
        twoFactorOtpRepo.delete(twoFactorOTP);
    }
}
