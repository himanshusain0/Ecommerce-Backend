package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.modal.Cart;
import com.ecommerce.ecommerce.modal.CartItem;
import com.ecommerce.ecommerce.modal.Product;
import com.ecommerce.ecommerce.modal.User;

public interface CartService {

    public CartItem addCartItem(
            User user,
            Product product,
            String size,
            int quantity
    );

    public Cart findUserCart(User user);

}
