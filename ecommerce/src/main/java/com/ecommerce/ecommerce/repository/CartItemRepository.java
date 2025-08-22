package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.modal.Cart;
import com.ecommerce.ecommerce.modal.CartItem;
import com.ecommerce.ecommerce.modal.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository  extends JpaRepository<CartItem ,Long> {

    CartItem findByCartAndProductAndSize(Cart cart , Product product, String size);
}
