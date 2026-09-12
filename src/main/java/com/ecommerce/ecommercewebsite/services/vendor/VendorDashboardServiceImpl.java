package com.ecommerce.ecommercewebsite.services.vendor;

import com.ecommerce.ecommercewebsite.dto.vendor.VendorDashboardResponseDTO;
import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.ProductRepository;
import com.ecommerce.ecommercewebsite.repositories.VendorOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class VendorDashboardServiceImpl implements VendorDashboardService {
    @Autowired
    private VendorOrderRepository vendorOrderRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public VendorDashboardResponseDTO getVendorDashboard(User vendor) {
        Long totalOrders = vendorOrderRepository.countByVendor(vendor);

        Long pendingOrders = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.PENDING_PAYMENT);

        Long paidOrders = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.PAID);

        Long processingOrders = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.PROCESSING);

        Long shippedOrders = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.SHIPPED);

        Long deliveredOrders = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.DELIVERED);

        Long cancelledOrders = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.CANCELLED);

        BigDecimal totalEarnings = vendorOrderRepository.getTotalVendorEarnings(vendor);
        Long totalProducts = productRepository.countByVendor(vendor);

        return new VendorDashboardResponseDTO(
                totalProducts,
                totalOrders,
                pendingOrders,
                paidOrders,
                processingOrders,
                shippedOrders,
                deliveredOrders,
                cancelledOrders,
                totalEarnings
        );
    }


}
