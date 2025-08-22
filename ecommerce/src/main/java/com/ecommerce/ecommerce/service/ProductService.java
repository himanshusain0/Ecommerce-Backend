package com.ecommerce.ecommerce.service;

import com.ecommerce.ecommerce.exceptions.ProductException;
import com.ecommerce.ecommerce.modal.Product;
import com.ecommerce.ecommerce.modal.Seller;
import com.ecommerce.ecommerce.request.CreateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.PrinterURI;
import java.util.List;

@Service
public interface ProductService {

    public Product createProduct(CreateProductRequest req, Seller seller);

    public  void  deleteProduct(Long productId) throws ProductException;
    public Product updateProduct(Long productId,Product product) throws ProductException;
    Product findProductById(Long productId) throws ProductException;
    List<Product> searchProduct(String querry);
    public Page<Product> getAllProduct(
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber

    );
    List<Product> getProductBySellerId(Long sellerId);

}
