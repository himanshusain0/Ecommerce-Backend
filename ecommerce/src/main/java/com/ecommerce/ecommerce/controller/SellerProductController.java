package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.exceptions.ProductException;
import com.ecommerce.ecommerce.exceptions.SellerException;
import com.ecommerce.ecommerce.modal.Product;
import com.ecommerce.ecommerce.modal.Seller;
import com.ecommerce.ecommerce.request.CreateProductRequest;
import com.ecommerce.ecommerce.service.ProductService;
import com.ecommerce.ecommerce.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sellers/products")
public class SellerProductController  {

    private final ProductService productService;
    private final SellerService sellerService;

    @GetMapping()
    public ResponseEntity<List<Product>>getProductBySellerId(@RequestHeader("Authorization") String jwt) throws Exception {
        Seller seller= sellerService.getSellerProfile(jwt);
        List<Product> products = productService.getProductBySellerId(seller.getId());
        return  ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody CreateProductRequest req,
                                           BindingResult result,
                                           @RequestHeader("Authorization") String jwt) throws Exception {

        // Check for validation errors
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        Seller seller = sellerService.getSellerProfile(jwt);
        Product product = productService.createProduct(req, seller);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @DeleteMapping("/{productId}")
    public  ResponseEntity<Void> deleteProduct(@PathVariable long productId){
        try {
            productService.deleteProduct(productId);
            return  new ResponseEntity<>(HttpStatus.OK);
        } catch (ProductException e) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}")
    public  ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                  @RequestBody Product product){
        try {
            Product updatedProduct = productService.updateProduct(productId,product);
            return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
        } catch (ProductException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}