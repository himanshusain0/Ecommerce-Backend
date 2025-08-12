package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.domain.Role;
import com.ecommerce.ecommerce.modal.User;
import com.ecommerce.ecommerce.modal.VerificationCode;
import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.request.LoginOtpRequest;
import com.ecommerce.ecommerce.request.LoginRequest;
import com.ecommerce.ecommerce.response.ApiResonse;
import com.ecommerce.ecommerce.response.AuthResponse;
import com.ecommerce.ecommerce.response.SignupRequest;
import com.ecommerce.ecommerce.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
//@AllArgsConstructor
@RequiredArgsConstructor
public class AuthController {



    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception {

        String jwt = authService.createUser(req);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("Register Success");
        res.setRole(Role.ROLE_CUSTOMER);
        return  ResponseEntity.ok(res);


    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResonse> sentOtpHandler(@RequestBody LoginOtpRequest req) throws Exception {

    authService.sentLoginOtp(req.getEmail(),req.getRole());

        ApiResonse res = new ApiResonse();

        res.setMessage("otp sent successfully");
        return  ResponseEntity.ok(res);

    }


    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req) throws Exception {

        AuthResponse authResponse = authService.siging(req);;


        return  ResponseEntity.ok(authResponse);


    }
}
