package com.ecommerce.ecommercewebsite.controllers.vendor;

import com.ecommerce.ecommercewebsite.dto.vendor.VendorDashboardResponseDTO;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.vendor.VendorDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/vendor")
public class VendorDashboardController {
    @Autowired
    private VendorDashboardService vendorDashboardService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<VendorDashboardResponseDTO>> getVendorDashboard(
            Principal principal) {

        String email = principal.getName();

        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));

        VendorDashboardResponseDTO dashboard = vendorDashboardService.getVendorDashboard(vendor);

        ApiResponse<VendorDashboardResponseDTO> response = new ApiResponse<>("success", dashboard);

        return ResponseEntity.ok(response);
    }

}
