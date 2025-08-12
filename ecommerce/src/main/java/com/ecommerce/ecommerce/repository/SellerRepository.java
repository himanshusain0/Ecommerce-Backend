package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.domain.AccountStatus;
import com.ecommerce.ecommerce.modal.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<Seller , Long> {

    Seller findByEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus status);

}
