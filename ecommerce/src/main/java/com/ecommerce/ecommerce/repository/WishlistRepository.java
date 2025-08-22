package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.modal.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<WishList ,Long> {

    WishList findByUserId(Long userId);

}
