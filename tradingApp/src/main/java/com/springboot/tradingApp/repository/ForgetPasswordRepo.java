package com.springboot.tradingApp.repository;

import com.springboot.tradingApp.model.ForgetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgetPasswordRepo extends JpaRepository<ForgetPasswordToken, String> {

    ForgetPasswordToken findByUserId(Long userId);

}
