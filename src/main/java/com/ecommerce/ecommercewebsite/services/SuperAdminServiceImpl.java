package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.dto.superadmin.*;
import com.ecommerce.ecommercewebsite.dto.vendor.VendorDetailsResponseDTO;
import com.ecommerce.ecommercewebsite.enums.*;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.exception.UserNotFoundException;
import com.ecommerce.ecommercewebsite.mappers.*;
import com.ecommerce.ecommercewebsite.model.*;
import com.ecommerce.ecommercewebsite.repositories.*;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private BusinessProfileRepository businessProfileRepository;
    @Autowired
    private VendorMapper vendorMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private VendorOrderRepository vendorOrderRepository;

    @Autowired
    private FeaturedPaymentRepository featuredPaymentRepository;
    @Autowired
    private VendorDetailsMapper vendorDetailsMapper;
    @Autowired
    private EmailService emailService;

    @Autowired
    private SuperAdminProductDetailsMapper superAdminProductDetailsMapper;

    @Override
    public String deleteVendor(Long id) {
        userRepository.deleteById(id);
        return "Vendor Deleted Successfully";
    }


    @Override
    public Long countTotalVendors() {
        Role role = roleRepository.findByRole("ROLE_VENDOR");
        return userRepository.countByRole(role);
    }

    @Override
    public Long countTotalProducts() {
        return productRepository.countByStatus(ProductStatus.ACTIVE);

    }

    @Override
    public VendorDetailsResponseDTO getVendorDetails(Long id) {
        User vendor = userRepository.findById(id).orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));
        return vendorDetailsMapper.map(vendor);

    }


    @Override
    public Long countTotalUsers() {
        Role role = roleRepository.findByRole("ROLE_USER");
        Long count = userRepository.countByRole(role);
        return count;
    }

    @Override
    public Page<VendorResponseDTO> getAllVendors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Role role = roleRepository.findByRole("ROLE_VENDOR");
        Page<BusinessProfile> businessProfilePage = businessProfileRepository.findAll(pageable);
        return businessProfilePage.map(vendorMapper::map);
    }

    @Override
    public String updateVendorApproval(Long id, ApprovalStatus status) {


        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));

        BusinessProfile businessProfile = businessProfileRepository.findByUser(user).orElseThrow(() -> new ApiException(AuthErrorCode.BUSINESS_PROFILE_NOT_FOUND));

        if (status != ApprovalStatus.APPROVED && status != ApprovalStatus.REJECTED) {
            throw new ApiException(AuthErrorCode.INVALID_STATUS);
        }

        if (businessProfile.getApprovalStatus() != ApprovalStatus.PENDING) {

            throw new ApiException(AuthErrorCode.INVALID_STATUS);
        }

        businessProfile.setApprovalStatus(status);

        businessProfileRepository.save(businessProfile);
        // Send email to vendor
        EmailDetailsDTO emailDetails = new EmailDetailsDTO();

        emailDetails.setRecipient(user.getEmail());

        if (status == ApprovalStatus.APPROVED) {

            emailDetails.setSubject("LocalConnect Vendor Application Approved");

            emailDetails.setMsgBody(
                    "Dear " + user.getName() + ",\n\n" +
                            "Congratulations! Your vendor application has been approved.\n\n" +
                            "You can now access your vendor account and start adding your products.\n\n" +
                            "Thank you for joining LocalConnect.\n\n" +
                            "Regards,\n" +
                            "LocalConnect Team"
            );

        } else {

            emailDetails.setSubject("LocalConnect Vendor Application Rejected");

            emailDetails.setMsgBody(
                    "Dear " + user.getName() + ",\n\n" +
                            "We regret to inform you that your vendor application has been rejected.\n\n" +
                            "If you believe this decision was made in error or need further information, " +
                            "please contact the LocalConnect support team.\n\n" +
                            "Regards,\n" +
                            "LocalConnect Team"
            );
        }
        emailService.sendSimpleMail(emailDetails);

        return "Vendor approval updated successfully";
    }

    @Override
    public Page<VendorResponseDTO> getAllPendingVendors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BusinessProfile> profiles = businessProfileRepository.findByApprovalStatus(ApprovalStatus.PENDING, pageable);
        return profiles.map(vendorMapper::map);

    }

    @Override
    public Page<ProductResponseDTO> getProducts(ProductStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("productId").descending());

        Page<Product> products;

        if (status != null) {
            products = productRepository.findByStatus(status, pageable);
        } else {
            products = productRepository.findByStatusIn(
                    List.of(
                            ProductStatus.APPROVAL_PENDING,
                            ProductStatus.ACTIVE,
                            ProductStatus.REJECTED
                    ),
                    pageable
            );
        }

        return products.map(productMapper::mapToDTO);
    }

    @Override
    public ProductDetailsResponseDTO getProductDetails(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));

        return superAdminProductDetailsMapper.mapToDTO(product);
    }

    @Override
    public Page<ProductResponseDTO> getPendingProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("productId").descending()); //  admin  see the latest  product
        Page<Product> pendingProducts = productRepository.findByStatus(ProductStatus.APPROVAL_PENDING, pageable);
        return pendingProducts.map(productMapper::mapToDTO);
    }

    @Override
    public String updateApprovalProduct(Long id, ProductStatus status) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        if (!product.getStatus().equals(ProductStatus.APPROVAL_PENDING)) {
            throw new ApiException(ProductErrorCode.INVALID_PRODUCT_STATUS);
        }
        if (status != ProductStatus.APPROVED && status != ProductStatus.REJECTED) {
            throw new ApiException(ProductErrorCode.INVALID_PRODUCT_STATUS);
        }
        product.setStatus(status);
        productRepository.save(product);
        return "Product approval status updated successfully";
    }


    @Override
    public BigDecimal getOrderCommission() {
        return vendorOrderRepository.getTotalCommission();
    }

    @Override
    public BigDecimal getFeaturedRevenue() {
        return featuredPaymentRepository.getTotalAmountByStatus(PaymentStatus.SUCCESS);
    }

    @Override
    public BigDecimal getTotalEarnings() {
        BigDecimal orderCommission = getOrderCommission();
        BigDecimal featuredRevenue = getFeaturedRevenue();
        return orderCommission.add(featuredRevenue);
    }

    @Override
    public SuperAdminIncomeResponseDTO getIncome(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay(); //Start searching from the very beginning of the selected start date.

        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);

        BigDecimal orderCommission = vendorOrderRepository.getCommissionBetween(startDateTime, endDateTime);

        BigDecimal featuredIncome = featuredPaymentRepository.getSuccessfulAmountBetween(startDateTime, endDateTime);

        BigDecimal totalIncome = orderCommission.add(featuredIncome);

        return new SuperAdminIncomeResponseDTO(orderCommission, featuredIncome, totalIncome);
    }

    @Override
    public List<SuperAdminSalesDTO> getSalesByProduct(String sort) {

        List<SuperAdminSalesDTO> sales = orderItemRepository.getSalesByProduct();

        switch (sort) {

            case "quantityAsc":
                sales.sort(
                        Comparator.comparing(
                                SuperAdminSalesDTO::getQuantitySold
                        )
                );
                break;

            case "salesDesc":
                sales.sort(
                        Comparator.comparing(
                                SuperAdminSalesDTO::getSalesValue
                        ).reversed()
                );
                break;

            case "salesAsc":
                sales.sort(
                        Comparator.comparing(
                                SuperAdminSalesDTO::getSalesValue
                        )
                );
                break;

            case "nameAsc":
                sales.sort(
                        Comparator.comparing(
                                SuperAdminSalesDTO::getProductName,
                                String.CASE_INSENSITIVE_ORDER
                        )
                );
                break;

            case "nameDesc":
                sales.sort(
                        Comparator.comparing(
                                SuperAdminSalesDTO::getProductName,
                                String.CASE_INSENSITIVE_ORDER
                        ).reversed()
                );
                break;

            case "quantityDesc":
            default:
                sales.sort(
                        Comparator.comparing(
                                SuperAdminSalesDTO::getQuantitySold
                        ).reversed()
                );
                break;
        }

        return sales;
    }

    @Override
    public List<SuperAdminCommissionDTO> getCommissionByVendor(
            LocalDate startDate,
            LocalDate endDate
    ) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        return vendorOrderRepository.getCommissionByVendor(startDateTime, endDateTime);
    }

    @Override
    public List<SuperAdminDistrictCommissionDTO> getCommissionByDistrict(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        return vendorOrderRepository.getCommissionByDistrict(startDateTime, endDateTime);
    }

}
