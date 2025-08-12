package com.ecommerce.ecommerce.controller;


import com.ecommerce.ecommerce.response.ApiResonse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ApiResonse HomeControllerHandler(){
        ApiResonse apiResonse = new ApiResonse();

        apiResonse.setMessage("Hello ,Welcome Here");
        return  apiResonse;
    }
}
