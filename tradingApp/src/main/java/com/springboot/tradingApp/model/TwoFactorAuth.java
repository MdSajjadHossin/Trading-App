package com.springboot.tradingApp.model;

import com.springboot.tradingApp.enums.VerificationType;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class TwoFactorAuth {

    private boolean isEnable = false;
    private VerificationType sendTo;
}
