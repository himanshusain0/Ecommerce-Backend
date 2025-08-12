package com.ecommerce.ecommerce.modal;


import com.ecommerce.ecommerce.domain.AccountStatus;
import com.ecommerce.ecommerce.domain.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String SellerName;
    private String mobile;

    @Column(unique = true, nullable = false)
    private String email;
    private String password;


    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();

    @Embedded
    private BankDetails bankDetails = new  BankDetails();

    @OneToOne(cascade = CascadeType.ALL)
    private Address pickupAddress = new Address();

    private String GSTIN;


    private Role role;

    private Boolean EmailVerified =false;

    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;





}
