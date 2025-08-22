package com.ecommerce.ecommerce.service.impl;

import com.ecommerce.ecommerce.modal.CartItem;
import com.ecommerce.ecommerce.modal.User;
import com.ecommerce.ecommerce.repository.CartItemRepository;
import com.ecommerce.ecommerce.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {


    private final CartItemRepository cartItemRepository;


    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {

        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new Exception("Cart item not found with id: " + id));
        User cartItemUser = item.getCart().getUser();


        if(cartItemUser.getId().equals(userId)){
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity()*item.getProduct().getMrpPrice());
            item.setSellingPrice(item.getQuantity()*item.getProduct().getSellingPrice());
            return  cartItemRepository.save(item);

        }
        throw  new Exception("you con't update this cartItem");

    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws Exception {
        // DB se existing cart item fetch karo
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new Exception("Cart item not found with id: " + cartItemId));

        User cartItemUser = item.getCart().getUser();

        if (cartItemUser.getId().equals(userId)) {
            cartItemRepository.delete(item);
            return; // ✅ delete ke baad return kar do
        }

        throw new Exception("You can't delete this cartItem");
    }
    @Override
    public CartItem findCartItemById(Long id) throws Exception {

        return  cartItemRepository.findById(id).orElseThrow(()->
                new Exception("Cart item not Found with id" + id));

    }
}
