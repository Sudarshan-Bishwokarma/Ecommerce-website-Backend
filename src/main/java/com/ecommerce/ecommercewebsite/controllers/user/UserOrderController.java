package com.ecommerce.ecommercewebsite.controllers.user;

import com.ecommerce.ecommercewebsite.dto.OrderRequestDTO;
import com.ecommerce.ecommercewebsite.dto.OrderResponseDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/order")
public class UserOrderController {
    @Autowired
    private UserOrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(
            @RequestBody OrderRequestDTO orderRequestDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();

        OrderResponseDTO orderResponseDTO =
                orderService.placeOrder(email, orderRequestDTO);

        ApiResponse<OrderResponseDTO> apiResponse =
                new ApiResponse<>("Order placed successfully", orderResponseDTO);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<OrderResponseDTO>>> GetMyOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        Page<OrderResponseDTO> responseDTOS = orderService.getUsersOrders(email, page, size);
        ApiResponse<Page<OrderResponseDTO>> response = new ApiResponse<>("Orders  fetched successfully", responseDTOS);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<ApiResponse<String>> cancelOrder(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long orderId) {
        String email = userDetails.getUsername();
        String message = orderService.cancelOrder(orderId, email);
        ApiResponse<String> response = new ApiResponse<>("Order cancelled successfully", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(@PathVariable Long orderId, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        OrderResponseDTO responseDTO = orderService.getOrderById(orderId, email);
        ApiResponse<OrderResponseDTO> apiResponse = new ApiResponse<>("Order found successfully", responseDTO);
        return ResponseEntity.ok(apiResponse);
    }

}