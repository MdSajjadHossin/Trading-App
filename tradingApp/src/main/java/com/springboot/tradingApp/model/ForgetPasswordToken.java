package com.springboot.tradingApp.model;

import com.springboot.tradingApp.enums.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ForgetPasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String otp;

    @OneToOne
    private User user;

    private VerificationType verificationType;

    private String sendTo;
}
