package com.springboot.tradingApp.response;

import lombok.Data;

@Data
public class AuthResponse {

    private String jwt;
    private boolean status;
    private String message;
    private boolean error;
    private boolean isTwoFactorAuthEnabled;
    private String session;
}
