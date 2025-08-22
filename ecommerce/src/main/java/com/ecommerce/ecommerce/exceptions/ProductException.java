package com.ecommerce.ecommerce.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
public class ProductException  extends  Exception{

//    @ExceptionHandler
    public  ProductException(String message){
        super(message);
    }
}
