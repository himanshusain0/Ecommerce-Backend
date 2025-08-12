package com.ecommerce.ecommerce.service.impl;

import com.ecommerce.ecommerce.domain.Role;
import com.ecommerce.ecommerce.modal.Seller;
import com.ecommerce.ecommerce.modal.User;
import com.ecommerce.ecommerce.repository.SellerRepository;
import com.ecommerce.ecommerce.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomUserServiceIml implements UserDetailsService {

    private final UserRepository userRepository;
    private static final String SELLER_PREFIX= "seller_";
    private  final SellerRepository sellerRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.startsWith(SELLER_PREFIX)){
            String actualUserName = username.substring(SELLER_PREFIX.length());
            Seller seller = sellerRepository.findByEmail(actualUserName);

            if(seller !=null){
                return  buildUserDetails(seller.getEmail(),seller.getPassword(),seller.getRole());
            }


        }else {
            User user = userRepository.findByEmail(username);
            if(user !=null){
                return  buildUserDetails(user.getEmail(),user.getPassword(),user.getRole());

            }
        }
        throw  new UsernameNotFoundException("User or Seller Not Found with this email "+ username);
    }

    private UserDetails buildUserDetails(@NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email, @NotBlank(message = "Password is required") String password, @NotNull(message = "Role is required") Role role) {

        if(role ==null) role = Role.ROLE_CUSTOMER;
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.toString() ));
        return  new org.springframework.security.core.userdetails.User(
                email,
                password,
                authorityList
        );

    }

}
