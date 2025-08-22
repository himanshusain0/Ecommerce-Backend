package com.ecommerce.ecommerce.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CreateProductRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "MRP price is required")
    private Integer mrpPrice;

    @NotNull(message = "Selling price is required")
    private Integer sellingPrice;

    private String color;
    private List<String> images;
    private String sizes;
    private Integer quantity;

    // Category fields - make sure these are sent in request
    @NotBlank(message = "Primary category is required")
    private String category;

    private String category2;
    private String category3;

    public String getCategory() {
        return category;
    }

    public String getCategory2() {
        return category2;
    }

    public String getCategory3() {
        return category3;
    }
}