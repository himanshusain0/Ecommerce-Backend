package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.modal.Seller;
import com.ecommerce.ecommerce.modal.SellerReport;

public interface SellerReportService
{
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);
}
