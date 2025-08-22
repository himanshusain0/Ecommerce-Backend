package com.ecommerce.ecommerce.modal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;  // Added this field to prevent null issues

    @Column(unique = true)
    private String categoryId;

    @ManyToOne
    private Category parentCategory;

    @NotNull
    private Integer level;
}