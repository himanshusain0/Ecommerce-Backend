package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.modal.Order;
import com.ecommerce.ecommerce.modal.Seller;
import com.ecommerce.ecommerce.modal.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Order order);
    List<Transaction> getTransactionBySellerId(Seller seller);
    List<Transaction> getAllTransaction();
}
