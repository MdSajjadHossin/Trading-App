package com.springboot.tradingApp.controller;

import com.springboot.tradingApp.configuration.JwtProvider;
import com.springboot.tradingApp.model.TwoFactorOTP;
import com.springboot.tradingApp.model.User;
import com.springboot.tradingApp.repository.UserRepo;
import com.springboot.tradingApp.response.AuthResponse;
import com.springboot.tradingApp.service.CustomUserDetailsService;
import com.springboot.tradingApp.service.EmailService;
import com.springboot.tradingApp.service.TwoFactorOtpService;
import com.springboot.tradingApp.utils.OtpUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {


        User isEmailExist = userRepo.findByEmail(user.getEmail());

        if(isEmailExist != null){
            throw new Exception("User already exist");
        }
        User newUser = userRepo.save(user);
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());

        User saveUser = userRepo.save(newUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("registration successful");


        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> signIn(@RequestBody User user) throws MessagingException {

        String userName = user.getEmail();
        String password = user.getPassword();

        Authentication auth = authenticate(userName, password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        User authUser = userRepo.findByEmail(userName);

        if(user.getTwoFactorAuth().isEnable()){
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Two factor authentication is enabled");
            authResponse.setTwoFactorAuthEnabled(true);
            String otp = OtpUtils.generateOTP();

            TwoFactorOTP oldTwoFactorOtp = twoFactorOtpService.findByUser(user.getId());
            if(oldTwoFactorOtp != null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
            }

            TwoFactorOTP newTwoFactorOtp = twoFactorOtpService.createOtp(authUser, otp, jwt);

            emailService.sendVerificationOtp(userName, otp);

            authResponse.setSession(newTwoFactorOtp.getId());
            return new ResponseEntity<>(authResponse, HttpStatus.ACCEPTED);
        }

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Login Successful");

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        if(userDetails == null){
            throw new BadCredentialsException("invalid user name");
        }
        if (!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("password not matched");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    public ResponseEntity<AuthResponse> verifySignInOtp(@PathVariable String otp,
                                                        @RequestParam String id) throws Exception {
        TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(id);
        if(twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP, otp)){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two Factor authentication verified");
            res.setTwoFactorAuthEnabled(true);
            res.setJwt(twoFactorOTP.getJwt());
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        throw new Exception("Invalid otp");
    }
}
