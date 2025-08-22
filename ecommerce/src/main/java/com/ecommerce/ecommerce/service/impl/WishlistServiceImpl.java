package com.ecommerce.ecommerce.service.impl;

import com.ecommerce.ecommerce.modal.Product;
import com.ecommerce.ecommerce.modal.User;
import com.ecommerce.ecommerce.modal.WishList;
import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.repository.WishlistRepository;
import com.ecommerce.ecommerce.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final UserRepository userRepository;
    private  final WishlistRepository wishlistRepository;



    @Override
    public WishList createWishlist(User user) {
        WishList wishList = new WishList();
        wishList.setUser(user);

        return wishlistRepository.save(wishList);
    }

    @Override
    public WishList getWishlistByUserId(User user) {

        WishList wishList = new WishList();
        if(wishList == null){
            wishList = createWishlist(user);
        }
        return wishList;
    }

    @Override
    public WishList addProductToWishlist(User user, Product product) {

        WishList wishList = getWishlistByUserId(user);
        if(wishList.getProducts().contains(product))
        {
            wishList.getProducts().remove(product);
        }else{
            wishList.getProducts().add(product);
        }
        return wishlistRepository.save(wishList);
    }
}
