package com.ecommerce.ecommercewebsite.Controllers;

import com.ecommerce.ecommercewebsite.dto.AddToCartRequestDTO;
import com.ecommerce.ecommercewebsite.dto.AddToCartResponseDTO;
import com.ecommerce.ecommercewebsite.model.Cart;
import com.ecommerce.ecommercewebsite.model.CartItem;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.CartRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/cart/add")
    public ResponseEntity<ApiResponse<AddToCartResponseDTO>> addToCart(@RequestBody AddToCartRequestDTO addToCartRequestDTO, Principal principal) {
        String email = principal.getName();
        AddToCartResponseDTO result = userService.addToCart(email, addToCartRequestDTO);
        ApiResponse<AddToCartResponseDTO> apiResponse = new ApiResponse<>("Product  added successfully to the cart", result);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/cart")
    public ResponseEntity<ApiResponse<List<AddToCartResponseDTO>>> getCart(@AuthenticationPrincipal User user) {
        List<AddToCartResponseDTO> result = userService.getCart(user);
        ApiResponse<List<AddToCartResponseDTO>> apiResponse = new ApiResponse<>("Cart has been successfully  fetched", result);
        return ResponseEntity.ok(apiResponse);
    }

}
