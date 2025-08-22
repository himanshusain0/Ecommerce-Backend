package com.ecommerce.ecommerce.controller;

import com.ecommerce.ecommerce.config.JwtProvider;
import com.ecommerce.ecommerce.domain.AccountStatus;
import com.ecommerce.ecommerce.exceptions.SellerException;
import com.ecommerce.ecommerce.modal.Seller;
import com.ecommerce.ecommerce.modal.SellerReport;
import com.ecommerce.ecommerce.modal.VerificationCode;
import com.ecommerce.ecommerce.repository.VerificationCodeRepository;
import com.ecommerce.ecommerce.request.LoginRequest;
import com.ecommerce.ecommerce.response.AuthResponse;
import com.ecommerce.ecommerce.service.AuthService;
import com.ecommerce.ecommerce.service.EmailService;
import com.ecommerce.ecommerce.service.SellerReportService;
import com.ecommerce.ecommerce.service.SellerService;
import com.ecommerce.ecommerce.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;
    private final SellerReportService sellerReportService;

    // -------------------- AUTH --------------------
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {
        String email = req.getEmail();
        req.setEmail("seller_" + email);
        AuthResponse authResponse = authService.siging(req);
        return ResponseEntity.ok(authResponse);
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("Wrong OTP.");
        }
        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);
        return ResponseEntity.ok(seller);
    }

    // -------------------- CREATE --------------------
    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception {
        Seller savedSeller = sellerService.createSeller(seller);

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "Apna Bazar Email Verification Code";
        String text = "Welcome to Apna Bazar, verify your account using this link";
        emailService.sendVerificationOtpEmail(seller.getEmail(), otp, subject, text);

        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }

    // -------------------- GET ONE --------------------
    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
        return ResponseEntity.ok(sellerService.getSellerById(id));
    }

    // -------------------- GET PROFILE --------------------
    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        return ResponseEntity.ok(sellerService.getSellerProfile(jwt));
    }

    // -------------------- GET ALL --------------------
    @GetMapping()
    public ResponseEntity<List<Seller>> getAllSellers(
            @RequestParam(required = false) AccountStatus status) {
        return ResponseEntity.ok(sellerService.getAllSellers(status));
    }

    // -------------------- UPDATE --------------------
    @PatchMapping
    public ResponseEntity<Seller> updateSeller(
            @RequestHeader("Authorization") String jwt,
            @RequestBody Seller seller) throws Exception {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(updatedSeller);
    }

    // -------------------- DELETE --------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
//        String email = jwtProvider.getEmailFromJwtToken(jwt);
        Seller seller = sellerService.getSellerProfile(jwt);
        SellerReport report = sellerReportService.getSellerReport(seller);
        return  new ResponseEntity<>(report,HttpStatus.OK);
    }
}
