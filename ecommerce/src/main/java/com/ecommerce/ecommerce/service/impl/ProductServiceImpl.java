package com.ecommerce.ecommerce.service.impl;

import com.ecommerce.ecommerce.exceptions.ProductException;
import com.ecommerce.ecommerce.modal.Category;
import com.ecommerce.ecommerce.modal.Product;
import com.ecommerce.ecommerce.modal.Seller;
import com.ecommerce.ecommerce.repository.CategoryRepository;
import com.ecommerce.ecommerce.repository.ProductRepository;
import com.ecommerce.ecommerce.request.CreateProductRequest;
import com.ecommerce.ecommerce.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Product createProduct(CreateProductRequest req, Seller seller) {
        // Validate required category fields
        if(req.getCategory() == null || req.getCategory().isEmpty()) {
            throw new IllegalArgumentException("Primary category ID cannot be null or empty");
        }

        // Handle category hierarchy with null checks
        Category category1 = getOrCreateCategory(req.getCategory(), 1, null);
        Category category2 = req.getCategory2() != null && !req.getCategory2().isEmpty() ?
                getOrCreateCategory(req.getCategory2(), 2, category1) :
                null;
        Category category3 = req.getCategory3() != null && !req.getCategory3().isEmpty() ?
                getOrCreateCategory(req.getCategory3(), 3, category2 != null ? category2 : category1) :
                null;

        int discountPercentage = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());

        Product product = new Product();
        product.setSeller(seller);
        product.setCategory(category3 != null ? category3 : (category2 != null ? category2 : category1));
        product.setDescription(req.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(req.getTitle());
        product.setColor(req.getColor());
        product.setSellingPrice(req.getSellingPrice());
        product.setImages(req.getImages());
        product.setMrpPrice(req.getMrpPrice());
        product.setSizes(req.getSizes());
        product.setDiscountPercent(discountPercentage);
        product.setQuantity(req.getQuantity() != null ? req.getQuantity() : 0);

        return productRepository.save(product);
    }

    private Category getOrCreateCategory(String categoryId, int level, Category parent) {
        Category category = categoryRepository.findByCategoryId(categoryId);
        if(category == null) {
            category = new Category();
            category.setCategoryId(categoryId);
            category.setLevel(level);
            category.setParentCategory(parent);
            // Set a default name if needed
            category.setName("Category-"+categoryId);
            category = categoryRepository.save(category);
        }
        return category;
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice <=0){
            throw new IllegalArgumentException("Actual price must be greater then 0");
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount/mrpPrice)*100;
        return (int) discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        findProductById(productId);
        product.setId(productId);
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId).orElseThrow(()->
                new ProductException("product not found with this id" + productId));
    }

    @Override
    public List<Product> searchProduct(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProduct(String category, String brand, String colors, String size,
                                       Integer minPrice, Integer maxPrice,
                                       Integer minDiscount, String sort, String stock,
                                       Integer pageNumber) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null && !category.isEmpty()) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }

            if (brand != null && !brand.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("brand"), brand));
            }

            if (colors != null && !colors.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("color"), colors));
            }

            if (size != null && !size.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("sizes"), size));
            }

            // Price range - fixed to use correct field names
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
            }

            // Minimum discount
            if (minDiscount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
            }

            // Stock filter - fixed to use quantity field
            if (stock != null && !stock.isEmpty()) {
                if (stock.equalsIgnoreCase("in_stock")) {
                    predicates.add(criteriaBuilder.greaterThan(root.get("quantity"), 0));
                } else if (stock.equalsIgnoreCase("out_of_stock")) {
                    predicates.add(criteriaBuilder.equal(root.get("quantity"), 0));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable;
        Sort sortBy = Sort.unsorted();

        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "price_low" -> sortBy = Sort.by("sellingPrice").ascending();
                case "price_high" -> sortBy = Sort.by("sellingPrice").descending();
                default -> sortBy = Sort.unsorted();
            }
        }

        pageable = PageRequest.of(
                pageNumber != null ? pageNumber : 0,
                10,
                sortBy
        );

        return productRepository.findAll(specification, pageable);
    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}