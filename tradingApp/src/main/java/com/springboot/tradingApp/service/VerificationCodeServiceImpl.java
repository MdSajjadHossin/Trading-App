package com.springboot.tradingApp.service;

import com.springboot.tradingApp.enums.VerificationType;
import com.springboot.tradingApp.model.User;
import com.springboot.tradingApp.model.VerificationCode;
import com.springboot.tradingApp.repository.VerificationCodeRepo;
import com.springboot.tradingApp.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService{

    @Autowired
    private VerificationCodeRepo verificationCodeRepo;



    @Override
    public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(OtpUtils.generateOTP());
        verificationCode.setVerificationType(verificationType);
        return verificationCodeRepo.save(verificationCode);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {
        Optional<VerificationCode> verificationCode = verificationCodeRepo.findById(id);

        if(verificationCode.isPresent()){
            return verificationCode.get();
        }
        throw new Exception("Verification code not found");
    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return verificationCodeRepo.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {
        verificationCodeRepo.delete(verificationCode);
    }
}
