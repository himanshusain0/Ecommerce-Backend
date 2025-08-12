package com.ecommerce.ecommerce.response;

import com.ecommerce.ecommerce.domain.Role;
import lombok.Data;

@Data
public class AuthResponse {

    private  String jwt;
    private String message;
    private Role role;

}
