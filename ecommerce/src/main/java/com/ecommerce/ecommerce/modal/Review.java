package com.ecommerce.ecommerce.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Changed to IDENTITY
    private Long id;

    @Column(nullable = false)
    private String reviewText;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private double rating;

    @ElementCollection
    @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    private List<String> productImages;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id", nullable = false) // Changed from @Column to @JoinColumn
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id") // Added proper join column
    private User user;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}