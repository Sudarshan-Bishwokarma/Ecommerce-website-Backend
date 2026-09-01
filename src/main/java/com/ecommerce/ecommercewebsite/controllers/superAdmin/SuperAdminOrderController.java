package com.ecommerce.ecommercewebsite.controllers.superAdmin;

import com.ecommerce.ecommercewebsite.dto.superadmin.OrderStatisticsDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.RecentOrderResponseDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.superadmin.SuperAdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
