package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.modal.*;
import com.ecommerce.ecommerce.response.ApiResonse;
import com.ecommerce.ecommerce.response.PaymentLinkResponse;
import com.ecommerce.ecommerce.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private  final UserService userService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final TransactionService transactionService;


    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResonse> paymentSuccessHandler(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        PaymentLinkResponse paymentLinkResponse;
        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

        boolean paymentSuccess = paymentService.ProceedPaymentOrder(
                paymentOrder,
                paymentId,
                paymentLinkId
        );

        if(paymentSuccess){
            for(Order order :paymentOrder.getOrders()){
                transactionService.createTransaction(order);
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport report = sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders()+1);
                report.setTotalEarnings(report.getTotalEarnings() +order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales()+order.getOderItems().size() );
                sellerReportService.updateSellerReport(report);
            }
        }

        ApiResonse res = new ApiResonse();

        res.setMessage("payment successfully");

        return  new ResponseEntity<>(res, HttpStatus.CREATED);
    }
}
