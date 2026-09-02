package com.ecommerce.ecommercewebsite.controllers.superAdmin;

import com.ecommerce.ecommercewebsite.dto.superadmin.OrderStatisticsDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.RecentOrderResponseDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminOrderDetailDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminOrderResponseDTO;
import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.superadmin.SuperAdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/super-admin")
@RestController
public class SuperAdminOrderController {
    @Autowired
    private SuperAdminOrderService superAdminOrderService;

    // get   total  orders
    @GetMapping("/total-orders")
    public ResponseEntity<ApiResponse<Long>> countTotalOrders() {

        Long totalOrders = superAdminOrderService.countTotalOrders();
        ApiResponse<Long> response = new ApiResponse<>("success", totalOrders);
        return ResponseEntity.ok(response);
    }

    // order statics
    @GetMapping("/orders/statistics")
    public ResponseEntity<ApiResponse<OrderStatisticsDTO>> getOrderStatistics() {

        OrderStatisticsDTO statistics = superAdminOrderService.getOrderStatistics();

        ApiResponse<OrderStatisticsDTO> response = new ApiResponse<>("Order statistics fetched successfully", statistics);

        return ResponseEntity.ok(response);
    }

    // recent  orders
    @GetMapping("/recent/orders")
    public ResponseEntity<ApiResponse<List<RecentOrderResponseDTO>>> getRecentOrders() {

        List<RecentOrderResponseDTO> recentOrders = superAdminOrderService.getRecentOrders();

        return ResponseEntity.ok(new ApiResponse<>("Recent orders fetched successfully", recentOrders));
    }

    //  all  orders
    @GetMapping("/orders/all")
    public ResponseEntity<ApiResponse<Page<SuperAdminOrderResponseDTO>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(required = false) OrderStatus status
    ) {

        Page<SuperAdminOrderResponseDTO> orders = superAdminOrderService.getAllOrders(page, size, sort, status);

        ApiResponse<Page<SuperAdminOrderResponseDTO>> response = new ApiResponse<>("All orders fetched successfully", orders);

        return ResponseEntity.ok(response);
    }

    //
    @GetMapping("/order-details/{orderId}")
    public ResponseEntity<ApiResponse<SuperAdminOrderDetailDTO>> getOrderDetails(@PathVariable Long orderId
    ) {
        SuperAdminOrderDetailDTO orderDetails = superAdminOrderService.getOrderDetails(orderId);

        ApiResponse<SuperAdminOrderDetailDTO> response = new ApiResponse<>("Order details fetched successfully", orderDetails);

        return ResponseEntity.ok(response);
    }

}
