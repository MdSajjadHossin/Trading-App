package com.springboot.tradingApp.response;

import com.springboot.tradingApp.enums.VerificationType;
import lombok.Data;

@Data
public class ForgetPasswordTokenRequest {

    private String sendTo;
    private VerificationType verificationType;

}
