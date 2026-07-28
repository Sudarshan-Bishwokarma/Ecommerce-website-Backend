package com.ecommerce.ecommercewebsite.controllers.vendor;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import com.ecommerce.ecommercewebsite.repositories.OrderRepository;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.VendorOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vendor")
public class VendorOrderController {
    @Autowired
    VendorOrderService vendorOrderService;
    @Autowired
    private OrderRepository orderRepository;

    // get all orders
    @GetMapping("/orders")
    ResponseEntity<ApiResponse<Page<VendorOrderResponseDTO>>> getOrders(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ) {
        String email = principal.getName();
        Page<VendorOrderResponseDTO> responseDTOS = vendorOrderService.getVendorOrders(email, page, size);
        ApiResponse<Page<VendorOrderResponseDTO>> apiResponse = new ApiResponse<>("success", responseDTOS);
        return ResponseEntity.ok(apiResponse);
    }

    //  get order  details
    @GetMapping("/vendor-order/{vendorOrderId}")
    ResponseEntity<ApiResponse<VendorOrderResponseDTO>> getVendorOrderDetails(@PathVariable Long vendorOrderId, Principal principal) {
        String email = principal.getName();
        VendorOrderResponseDTO responseDTO = vendorOrderService.getVendorOrderDetails(vendorOrderId, email);
        ApiResponse<VendorOrderResponseDTO> apiResponse = new ApiResponse<>("success", responseDTO);
        return ResponseEntity.ok(apiResponse);
    }

    //filter  order  by status
    @GetMapping("/orders/status")
    public ResponseEntity<ApiResponse<Page<VendorOrderResponseDTO>>> getOrderStatus(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam OrderStatus status

    ) {
        String email = principal.getName();
        Page<VendorOrderResponseDTO> response = vendorOrderService.getOrderByStatus(email, status, page, size);
        ApiResponse<Page<VendorOrderResponseDTO>> apiResponse = new ApiResponse<>("success", response);
        return ResponseEntity.ok(apiResponse);

    }

    //  update  status
    @PutMapping("/vendor-order/{vendorOrderId}/status/")
    ResponseEntity<ApiResponse<UpdateVendorOrderStatusResponseDTO>> updateStatus(@PathVariable Long vendorOrderId, @RequestBody UpdateOrderStatusDTO updateOrderStatusDTO, Principal principal) {
        String email = principal.getName();
        UpdateVendorOrderStatusResponseDTO responseDTO = vendorOrderService.updateVendorOrderStatus(email, vendorOrderId, updateOrderStatusDTO);
        ApiResponse<UpdateVendorOrderStatusResponseDTO> apiResponse = new ApiResponse<>("success", responseDTO);
        return ResponseEntity.ok(apiResponse);
    }

    // filter  order  by  date
    @GetMapping("/vendor-orders/date")
    public ResponseEntity<ApiResponse<Page<VendorOrderResponseDTO>>> getOrdersByDate(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate
    ) {
        String email = principal.getName();
        Page<VendorOrderResponseDTO> responseDTOS = vendorOrderService.getOrdersByDate(email, startDate, endDate, page, size);
        ApiResponse<Page<VendorOrderResponseDTO>> response = new ApiResponse<>("success", responseDTOS);
        return ResponseEntity.ok(response);
    }

    //   get order by user email
    @GetMapping("/vendor-orders/{email}")
    public ResponseEntity<ApiResponse<Page<VendorOrderResponseDTO>>> getOrderByUserEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<VendorOrderResponseDTO> response = vendorOrderService.getOrderByEmail(email, page, size);
        ApiResponse<Page<VendorOrderResponseDTO>> apiResponse = new ApiResponse<>("success", response);
        return ResponseEntity.ok(apiResponse);
    }

    //  cancel  order   by vendor
    @PutMapping("/vendor-order/{vendorOrderId}/cancel")
    ResponseEntity<ApiResponse<UpdateVendorOrderStatusResponseDTO>> cancelOrder(@PathVariable Long vendorOrderId, Principal principal) {
        String email = principal.getName();
        UpdateVendorOrderStatusResponseDTO responseDTO = vendorOrderService.cancelOrder(email, vendorOrderId);
        ApiResponse<UpdateVendorOrderStatusResponseDTO> apiResponse = new ApiResponse<>("Order Deleted Successfully", responseDTO);
        return ResponseEntity.ok(apiResponse);

    }

    //   get  total  order status   for piechart
    @GetMapping("/vendor-orders/status-summary")
    public ResponseEntity<ApiResponse<VendorOrderStatusSummaryDTO>> getOrderStatusSummary(Principal principal) {
        String email = principal.getName();
        VendorOrderStatusSummaryDTO status = vendorOrderService.getOrderStatusSummary(email);
        ApiResponse<VendorOrderStatusSummaryDTO> apiResponse = new ApiResponse<>("success", status);
        return ResponseEntity.ok(apiResponse);
    }

    // get orders per months
    @GetMapping("/orders/monthly")
    public ResponseEntity<ApiResponse<List<MonthlyOrderDTO>>> getOrdersByMonth() {
        List<MonthlyOrderDTO> monthlyOrderDTO = vendorOrderService.getMonthlyOrders();
        ApiResponse<List<MonthlyOrderDTO>> response = new ApiResponse<>("success", monthlyOrderDTO);
        return ResponseEntity.ok(response);

    }
}
