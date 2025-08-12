package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.modal.User;

public interface UserService {

      User findUserByJwtToken(String jwt) throws Exception;
      User findUserByEmail(String email) throws Exception;
}
