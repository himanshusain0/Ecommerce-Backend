package com.ecommerce.ecommerce.service;


import com.ecommerce.ecommerce.domain.Role;
import com.ecommerce.ecommerce.request.LoginRequest;
import com.ecommerce.ecommerce.response.AuthResponse;
import com.ecommerce.ecommerce.response.SignupRequest;

public interface AuthService {

    void  sentLoginOtp(String email, Role role) throws Exception;
    String createUser(SignupRequest rew) throws Exception;
    AuthResponse siging(LoginRequest req);

}
