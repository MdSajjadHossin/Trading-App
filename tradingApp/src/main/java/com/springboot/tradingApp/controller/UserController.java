package com.springboot.tradingApp.controller;

import com.springboot.tradingApp.enums.VerificationType;
import com.springboot.tradingApp.model.ForgetPasswordToken;
import com.springboot.tradingApp.model.User;
import com.springboot.tradingApp.model.VerificationCode;
import com.springboot.tradingApp.request.ResetPasswordRequest;
import com.springboot.tradingApp.response.ApiResponse;
import com.springboot.tradingApp.response.AuthResponse;
import com.springboot.tradingApp.response.ForgetPasswordTokenRequest;
import com.springboot.tradingApp.service.EmailService;
import com.springboot.tradingApp.service.ForgotPasswordService;
import com.springboot.tradingApp.service.UserService;
import com.springboot.tradingApp.service.VerificationCodeService;
import com.springboot.tradingApp.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private EmailService emailService;

    private ForgotPasswordService forgotPasswordService;

    private String jwt;

    @GetMapping("/api/user/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @PatchMapping("/api/user/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(@RequestHeader("Authorization") String jwt, @PathVariable VerificationType verificationType) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        if(verificationCode == null){
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }
        if(verificationType.equals(verificationType.EMAIL)){
            emailService.sendVerificationOtp(user.getEmail(), verificationCode.getOtp());
        }



        return new ResponseEntity<>("verification otp send successful", HttpStatus.OK);
    }

    @PatchMapping("/api/user/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@PathVariable String otp, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL)?verificationCode.getEmail():verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);
        if (isVerified){
            User updateUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);
            verificationCodeService.deleteVerificationCodeById(verificationCode);
            return new ResponseEntity<>(updateUser, HttpStatus.OK);
        }
        throw new Exception("wrong otp");
    }

    @PatchMapping("/auth/user/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgetPasswordOtp(@RequestBody ForgetPasswordTokenRequest request) throws Exception {
        User user = userService.findByEmail(request.getSendTo());
        String otp = OtpUtils.generateOTP();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgetPasswordToken token = forgotPasswordService.findByUser(user.getId());

        if(token == null){
            token = forgotPasswordService.createToken(user, id,otp, request.getVerificationType(), request.getSendTo());
        }

        if (request.getVerificationType().equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtp(
                    user.getEmail(),
                    token.getOtp());
        }
        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password reset otp sent successful");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/auth/user/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String id,
                                              @RequestBody ResetPasswordRequest request,
                                              @RequestHeader("Authentication") String jwt) throws Exception {

        ForgetPasswordToken forgetPasswordToken = forgotPasswordService.findById(id);
        boolean isVerified = forgetPasswordToken.getOtp().equals(request.getOtp());

        if(isVerified){
            userService.updatePassword(forgetPasswordToken.getUser(), request.getPassword());
            ApiResponse response = new ApiResponse();
            response.setMessage("Password update successful");

            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
        throw new Exception("wrong otp");

    }

}
