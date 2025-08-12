package com.ecommerce.ecommerce.utils;

import java.security.SecureRandom;

public class OtpUtil {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateOtp() {
        int otpLength = 6; // Fixed length for OTP

        StringBuilder otp = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            otp.append(secureRandom.nextInt(10)); // generates digit 0–9
        }
        return otp.toString();
    }
}
