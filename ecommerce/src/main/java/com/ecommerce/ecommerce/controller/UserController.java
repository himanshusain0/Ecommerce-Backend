package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.modal.User;
import com.ecommerce.ecommerce.response.AuthResponse;
import com.ecommerce.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/profile")
    public ResponseEntity<User> createUserHandler(@RequestHeader("Authorization") String jwt) throws Exception {

        User  user = userService.findUserByJwtToken(jwt);
        return  ResponseEntity.ok(user);
    }
}
