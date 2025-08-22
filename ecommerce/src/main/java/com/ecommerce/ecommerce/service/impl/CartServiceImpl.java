package com.ecommerce.ecommerce.service.impl;

import com.ecommerce.ecommerce.modal.Cart;
import com.ecommerce.ecommerce.modal.CartItem;
import com.ecommerce.ecommerce.modal.Product;
import com.ecommerce.ecommerce.modal.User;
import com.ecommerce.ecommerce.repository.CartItemRepository;
import com.ecommerce.ecommerce.repository.CartRepository;
import com.ecommerce.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {


    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;






    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
        Cart cart = findUserCart(user);
        CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart ,product, size);
        if(isPresent == null){
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSize(size);
            cartItem.setCart(cart);
            cartItem.setMrpPrice(product.getMrpPrice());

            int totalPrice = quantity * product.getSellingPrice();
            cartItem.setSellingPrice(totalPrice);

            cart.getCartItem().add(cartItem);
            cartItem.setCart(cart);
            return  cartItemRepository.save(cartItem);
        }

        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) {

        Cart cart = cartRepository.findByUserId(user.getId());

        int totalPrice = 0;
        int totalDiscountedPrice =0;
        int  totalItem = 0;
        for(CartItem cartItem: cart.getCartItem() ){
            totalPrice += cartItem.getMrpPrice()*cartItem.getQuantity();
            totalDiscountedPrice += cartItem.getSellingPrice()*cartItem.getQuantity();
            totalItem += cartItem.getQuantity();
        }

        cart.setTotalMrpPrice(totalPrice);
//        cart.setCartItem(totalItem);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setDiscount(totalPrice - totalDiscountedPrice);//9:12:02
        cart.setTotalItem(totalItem);
        return cart;
    }
}
