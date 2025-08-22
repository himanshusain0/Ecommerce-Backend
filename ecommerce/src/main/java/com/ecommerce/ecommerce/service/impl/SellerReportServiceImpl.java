package com.ecommerce.ecommerce.service.impl;

import com.ecommerce.ecommerce.modal.Seller;
import com.ecommerce.ecommerce.modal.SellerReport;
import com.ecommerce.ecommerce.repository.SellerReportRepository;
import com.ecommerce.ecommerce.service.SellerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {

    private  final SellerReportRepository sellerReportRepository;


    @Override
    public SellerReport getSellerReport(Seller seller) {

        SellerReport sellerreport = sellerReportRepository.findBySellerId(seller.getId());

        if(sellerreport == null){
            SellerReport newSellerReport = new SellerReport();
            newSellerReport.setSeller(seller);
            return  sellerReportRepository.save(newSellerReport);
        }


        return sellerreport;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {

        return  sellerReportRepository.save(sellerReport);

    }
}
