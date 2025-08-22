package com.ecommerce.ecommerce.response;


import com.ecommerce.ecommerce.domain.Role;
import lombok.Data;

@Data
public class SignupRequest {

    private String email;

    private String fullName;
    private String otp;
    private Role role;


}
