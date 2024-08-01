package com.springboot.tradingApp.service;

import com.springboot.tradingApp.configuration.JwtProvider;
import com.springboot.tradingApp.enums.VerificationType;
import com.springboot.tradingApp.model.TwoFactorAuth;
import com.springboot.tradingApp.model.User;
import com.springboot.tradingApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);
        User user = userRepo.findByEmail(email);

        if(user == null){
            throw new Exception("User not found...");
        }
        return user;
    }

    @Override
    public User findByEmail(String email) throws Exception {
        User user = userRepo.findByEmail(email);

        if(user == null){
            throw new Exception("User not found...");
        }
        return user;

    }

    @Override
    public User findById(Long userId) throws Exception {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()){
            throw new Exception("User not found...");
        }
        return user.get();
    }

    @Override
    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnable(true);
        twoFactorAuth.setSendTo(verificationType);

        user.setTwoFactorAuth(twoFactorAuth);
        return userRepo.save(user);
    }


    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepo.save(user);
    }
}
