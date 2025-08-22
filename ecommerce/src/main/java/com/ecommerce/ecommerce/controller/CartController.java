package com.ecommerce.ecommerce.controller;


import com.ecommerce.ecommerce.exceptions.UserException;
import com.ecommerce.ecommerce.modal.Cart;
import com.ecommerce.ecommerce.modal.CartItem;
import com.ecommerce.ecommerce.modal.Product;
import com.ecommerce.ecommerce.modal.User;
import com.ecommerce.ecommerce.request.AddItemRequest;
import com.ecommerce.ecommerce.response.ApiResonse;
import com.ecommerce.ecommerce.service.CartItemService;
import com.ecommerce.ecommerce.service.CartService;
import com.ecommerce.ecommerce.service.ProductService;
import com.ecommerce.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {


    private final CartService cartService;
    private final CartItemService cartItemService;
    private  final UserService userService;
    private  final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        return  new ResponseEntity<Cart>(cart, HttpStatus.OK);

    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest req,
                                                    @RequestHeader("Authorization") String jwt) throws Exception {

        User user =userService.findUserByJwtToken(jwt);

        Product product= productService.findProductById(req.getProductId());
        CartItem item = cartService.addCartItem(user,
                product,
                req.getSize(),
                req.getQuantity());
        ApiResonse res = new ApiResonse();
        res.setMessage("Item Added to cart successfully");
        return  new ResponseEntity<>(item,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResonse> deleteCartItemHandler(@PathVariable Long cartItemId,
                                                            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResonse res = new ApiResonse();
        res.setMessage("Removed from the Cart");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem>updateCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        CartItem updateCartItem = null;
        if(cartItem.getQuantity() > 0){
            updateCartItem= cartItemService.updateCartItem(user.getId(),cartItemId,cartItem);

        }

        return  new ResponseEntity<>(updateCartItem,HttpStatus.ACCEPTED);

    }




}
