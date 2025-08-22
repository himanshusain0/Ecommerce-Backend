package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.modal.Product;
import com.ecommerce.ecommerce.modal.User;
import com.ecommerce.ecommerce.modal.WishList;

public interface WishlistService {

    WishList createWishlist(User user);
    WishList getWishlistByUserId(User user);
    WishList addProductToWishlist(User user, Product product);
}
