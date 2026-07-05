package com.ecommerce.ecommercewebsite.controllers.vendor;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.repositories.OrderRepository;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.AdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {
    @Autowired
    AdminOrderService adminOrderService;
    @Autowired
    private OrderRepository orderRepository;

    // get all orders
    @GetMapping("/orders")
    ResponseEntity<ApiResponse<Page<OrderResponseDTO>>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ) {
        Page<OrderResponseDTO> orderResponseDTOPage = adminOrderService.getAllUsersOrders(page, size);
        ApiResponse<Page<OrderResponseDTO>> apiResponse = new ApiResponse<>("success", orderResponseDTOPage);
        return ResponseEntity.ok(apiResponse);
    }

    //  get order  details
    @GetMapping("/order/{orderId}")
    ResponseEntity<ApiResponse<OrderDetailDTO>> getOrdersByUserId(@PathVariable Long orderId) {
        OrderDetailDTO responseDTO = adminOrderService.getMoreOrderDetails(orderId);
        ApiResponse<OrderDetailDTO> apiResponse = new ApiResponse<>("success", responseDTO);
        return ResponseEntity.ok(apiResponse);
    }

    //filter  order  by status
    @GetMapping("/orders/status")
    public ResponseEntity<ApiResponse<Page<OrderResponseDTO>>> getOrderStatus(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String status

    ) {
        Page<OrderResponseDTO> response = adminOrderService.getOrderByStatus(status, page, size);
        ApiResponse<Page<OrderResponseDTO>> apiResponse = new ApiResponse<>("success", response);
        return ResponseEntity.ok(apiResponse);

    }

    //  update  status
    @PutMapping("/order/{orderId}/status/update")
    ResponseEntity<ApiResponse<UpdateOrderStatusResponseDTO>> updateStatus(@PathVariable Long orderId, @RequestBody UpdateOrderStatusDTO updateOrderStatusDTO) {
        UpdateOrderStatusResponseDTO status = adminOrderService.updateOrderStatus(orderId, updateOrderStatusDTO);
        ApiResponse<UpdateOrderStatusResponseDTO> apiResponse = new ApiResponse<>("success", status);
        return ResponseEntity.ok(apiResponse);
    }

    // filter  order  by  date
    @GetMapping("/orders/date")
    public ResponseEntity<ApiResponse<Page<OrderResponseDTO>>> getOrdersByDate(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
    ) {
        Page<OrderResponseDTO> result = adminOrderService.getOrdersByDate(startDate, endDate, page, size);
        ApiResponse<Page<OrderResponseDTO>> response = new ApiResponse<>("success", result);
        return ResponseEntity.ok(response);
    }

    //   get order by user email
    @GetMapping("/orders/user")
    public ResponseEntity<ApiResponse<Page<OrderResponseDTO>>> getOrderByUserEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<OrderResponseDTO> response = adminOrderService.getOrderByEmail(email, page, size);
        ApiResponse<Page<OrderResponseDTO>> apiResponse = new ApiResponse<>("success", response);
        return ResponseEntity.ok(apiResponse);
    }

    //  cancel  order   by admin
    @PutMapping("/order/{orderId}/cancel")
    ResponseEntity<ApiResponse<UpdateOrderStatusResponseDTO>> cancelOrder(@PathVariable Long orderId) {
        UpdateOrderStatusResponseDTO responseDTO = adminOrderService.cancelOrder(orderId);
        ApiResponse<UpdateOrderStatusResponseDTO> apiResponse = new ApiResponse<>("Order Deleted Successfully", responseDTO);
        return ResponseEntity.ok(apiResponse);

    }

    //   get  total  order status   for piechart
    @GetMapping("/orders/status-summary")
    public ResponseEntity<ApiResponse<OrderStatusSummaryDTO>> getOrderStatusSummary() {
        OrderStatusSummaryDTO status = adminOrderService.getOrderStatusSummary();
        ApiResponse<OrderStatusSummaryDTO> apiResponse = new ApiResponse<>("success", status);
        return ResponseEntity.ok(apiResponse);
    }

    // get orders per month
    @GetMapping("/orders/monthly")
    public ResponseEntity<ApiResponse<List<MonthlyOrderDTO>>> getOrdersByMonth() {
        List<MonthlyOrderDTO> monthlyOrderDTO = adminOrderService.getMonthlyOrders();
        ApiResponse<List<MonthlyOrderDTO>> response = new ApiResponse<>("success", monthlyOrderDTO);
        return ResponseEntity.ok(response);

    }
}
