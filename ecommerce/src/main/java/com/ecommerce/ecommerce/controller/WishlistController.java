package com.ecommerce.ecommerce.controller;


import com.ecommerce.ecommerce.exceptions.ProductException;
import com.ecommerce.ecommerce.modal.Product;
import com.ecommerce.ecommerce.modal.User;
import com.ecommerce.ecommerce.modal.WishList;
import com.ecommerce.ecommerce.repository.UserRepository;
import com.ecommerce.ecommerce.service.ProductService;
import com.ecommerce.ecommerce.service.UserService;
import com.ecommerce.ecommerce.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserRepository userRepository;
    private final UserService userService;
    private  final ProductService productService;

//    @PostMapping("/create")
//    public ResponseEntity<WishList>createWishlist(@RequestBody User user){
//        WishList wishList = wishlistService.createWishlist(user);
//        return  new ResponseEntity<>(wishList, HttpStatus.OK);
//    }

    @GetMapping()
    public ResponseEntity<WishList>getWishlistByUserId(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        WishList wishList = wishlistService.getWishlistByUserId(user);
        return  ResponseEntity.ok(wishList);

    }



    @PostMapping("/add-product/{productId}")
    public ResponseEntity<WishList> addProductToWishlist(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwtToken(jwt);
        WishList updatedWishlist = wishlistService.addProductToWishlist(
                user,
                product
        );
        return  ResponseEntity.ok(updatedWishlist);
    }
}
