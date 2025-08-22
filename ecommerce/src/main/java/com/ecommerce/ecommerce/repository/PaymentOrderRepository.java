package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.modal.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOrderRepository extends JpaRepository<PaymentOrder , Long> {

    PaymentOrder findByPaymentLinkId(String paymentId);
}
