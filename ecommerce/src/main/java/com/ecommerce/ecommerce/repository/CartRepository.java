package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.modal.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
}
