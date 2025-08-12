package com.ecommerce.ecommerce.service.impl;

import com.ecommerce.ecommerce.config.JwtProvider;
import com.ecommerce.ecommerce.domain.Role;
import com.ecommerce.ecommerce.modal.Cart;
import com.ecommerce.ecommerce.modal.Seller;
import com.ecommerce.ecommerce.modal.User;
import com.ecommerce.ecommerce.modal.VerificationCode;
import com.ecommerce.ecommerce.repository.CartRepository;
import com.ecommerce.ecommerce.repository.SellerRepository;
import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.repository.VerificationCodeRepository;
import com.ecommerce.ecommerce.request.LoginRequest;
import com.ecommerce.ecommerce.response.AuthResponse;
import com.ecommerce.ecommerce.response.SignupRequest;
import com.ecommerce.ecommerce.service.AuthService;
import com.ecommerce.ecommerce.service.EmailService;
import com.ecommerce.ecommerce.utils.OtpUtil;
import lombok.AllArgsConstructor;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private  final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceIml customUserServiceIml;
    private final SellerRepository sellerRepository;


    @Override
    public void sentLoginOtp(String email ,Role role) throws Exception {
        String SIGNING_PREFIX = "signing_";

        if(email.startsWith(SIGNING_PREFIX)){
            email = email.substring(SIGNING_PREFIX.length());
            if(role.equals(Role.ROLE_SELLER)){
                Seller seller = sellerRepository.findByEmail(email);
                if(seller == null){
                    throw new Exception("Seller not found");
                }
            }
            else{
                User user = userRepository.findByEmail(email);
                if(user == null){
                    throw  new Exception("user not exist woth provided email");
                }
            }

        }
        VerificationCode isExist = verificationCodeRepository.findByEmail(email);

        if(isExist != null){
            verificationCodeRepository.delete(isExist);
        }

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject = "apna bazar login/signup otp";
        String text = "your login/signup  otp  is - " + otp;

        emailService.sendVerificationOtpEmail(email,otp,subject,text);


    }

    @Override
    public String createUser(SignupRequest req) throws Exception {


        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
        if(verificationCode == null || !verificationCode.getOtp().equals(req.getOtp()) ){
            throw  new Exception("Wrong Otp");
        }

        User user = userRepository.findByEmail(req.getEmail());

        if(user == null){
            User createdUser = new User();

            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());
            createdUser.setRole(Role.ROLE_ADMIN);
            createdUser.setMobile("9641398563");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));

            user= userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(Role.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(),null,authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return  jwtProvider.generateToken(authentication);


    }

    @Override
    public AuthResponse siging(LoginRequest req) {
       String username = req.getEmail();
       String otp = req.getOtp();
       Authentication authentication = authenticate(username,otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse= new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Success");

        Collection<? extends  GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
        authResponse.setRole(Role.valueOf(roleName));
        return authResponse;


    }

    private Authentication authenticate(String username, String otp) {

        UserDetails userDetails = customUserServiceIml.loadUserByUsername(username);

        String SELLER_PREFIX ="seller_";
        if(username.startsWith(SELLER_PREFIX)){
            username= username.substring(SELLER_PREFIX.length());
        }
        if(userDetails == null){
            throw new BadCredentialsException("Invalid username or password");


        }



        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

        if(verificationCode == null || !verificationCode.getOtp().equals(otp) ){
            throw new BadCredentialsException("wrong otp");
        }

        return  new UsernamePasswordAuthenticationToken(userDetails,null,
                userDetails.getAuthorities()
                );

    }
}
