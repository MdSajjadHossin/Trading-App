package com.springboot.tradingApp.service;

import com.springboot.tradingApp.enums.VerificationType;
import com.springboot.tradingApp.model.User;
import com.springboot.tradingApp.model.VerificationCode;

public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId);

    void deleteVerificationCodeById(VerificationCode verificationCode);

}
