package com.ecommerce.ecommerce.request;

import com.ecommerce.ecommerce.domain.Role;
import lombok.Data;

@Data
public class LoginOtpRequest {

    private String email;
    private String otp;
    private Role role;

}
