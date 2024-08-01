package com.springboot.tradingApp.service;

import com.springboot.tradingApp.enums.VerificationType;
import com.springboot.tradingApp.model.User;

public interface UserService {

    public User findUserProfileByJwt(String jwt) throws Exception;
    public User findByEmail(String email) throws Exception;
    public User findById(Long userId) throws Exception;

    public User enableTwoFactorAuthentication(VerificationType verificationType,String sendTo, User user);

    User updatePassword(User user, String newPassword);

}
