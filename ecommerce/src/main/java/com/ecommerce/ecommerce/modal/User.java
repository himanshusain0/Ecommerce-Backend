package com.ecommerce.ecommerce.modal;

import com.ecommerce.ecommerce.domain.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Changed to IDENTITY
    private Long id;

   @NotBlank(message = "Password is required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password; // Fixed capitalization

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number should be 10 digits")
    private String mobile;

   @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_CUSTOMER;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<Address> addresses = new HashSet<>();

    @ManyToMany(mappedBy = "usedByUsers")
    @JsonIgnore
    private Set<Coupon> usedCoupons = new HashSet<>(); // Fixed typo in field name
}