package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.EmailDetailsDTO;
import com.ecommerce.ecommercewebsite.dto.FeaturedProductResponseDTO;
import com.ecommerce.ecommercewebsite.dto.FeaturedRequestActionDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminFeaturedRequestDetailsResponseDTO;
import com.ecommerce.ecommercewebsite.enums.FeaturedRequestStatus;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.mappers.FeaturedRequestDetailsMapper;
import com.ecommerce.ecommercewebsite.mappers.FeaturedRequestMapper;
import com.ecommerce.ecommercewebsite.model.FeaturedRequest;
import com.ecommerce.ecommercewebsite.repositories.FeaturedRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FeaturedRequestAdminServiceImpl implements FeaturedRequestAdminService {
    @Autowired
    private FeaturedRequestMapper featuredRequestMapper;
    @Autowired
    private FeaturedRequestRepository featuredRequestRepository;
    @Autowired
    private FeaturedRequestDetailsMapper featuredRequestDetailsMapper;
    @Autowired
    private EmailService emailService;

    @Override
    public SuperAdminFeaturedRequestDetailsResponseDTO getFeaturedRequestDetails(Long featuredRequestId) {
        FeaturedRequest request = featuredRequestRepository.findById(featuredRequestId).orElseThrow(() -> new ApiException(ProductErrorCode.FEATURED_PRODUCT_REQUEST_NOT_FOUND));

        return featuredRequestDetailsMapper.mapToFeaturedRequestDetailsResponseDTO(request);
    }

    @Override
    public Page<FeaturedProductResponseDTO> getPendingFeaturedRequests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FeaturedRequest> allPendingFeaturedRequests = featuredRequestRepository.findByStatus(FeaturedRequestStatus.PENDING, pageable);
        return allPendingFeaturedRequests.map(featuredRequestMapper::mapToFeaturedProductResponseDTO);
    }

    @Override
    public String approvedFeaturedRequest(Long id, FeaturedRequestActionDTO featuredRequestActionDTO) {
        FeaturedRequest request = featuredRequestRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.FEATURED_PRODUCT_REQUEST_NOT_FOUND));
        if (request.getStatus() != FeaturedRequestStatus.PENDING) {
            throw new ApiException(ProductErrorCode.INVALID_PRODUCT_STATUS);
        }
        request.setStatus(FeaturedRequestStatus.APPROVED);
        request.setAdminMessage(featuredRequestActionDTO.getMessage());
        featuredRequestRepository.save(request);
        EmailDetailsDTO emailDetails = new EmailDetailsDTO();

        emailDetails.setRecipient(request.getVendor().getEmail());
        emailDetails.setSubject("Featured Product Request Approved - LocalConnect");

        emailDetails.setMsgBody(
                "Dear " + request.getVendor().getName() + ",\n\n" +
                        "Good news! Your featured product request has been approved.\n\n" +
                        "Product: " + request.getProduct().getProductName() + "\n" +
                        "Status: APPROVED\n\n" +
                        "Admin Message:\n" +
                        request.getAdminMessage() + "\n\n" +
                        "You can now proceed with the featured product payment from your vendor dashboard.\n\n" +
                        "Thank you for using LocalConnect.\n\n" +
                        "Regards,\n" +
                        "LocalConnect Team"
        );

        emailService.sendSimpleMail(emailDetails);

        return "Featured request approved successfully";
    }

    @Override
    public String rejectFeaturedRequest(Long id, FeaturedRequestActionDTO requestActionDTO) {
        FeaturedRequest request = featuredRequestRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.FEATURED_PRODUCT_REQUEST_NOT_FOUND));
        if (request.getStatus() != FeaturedRequestStatus.PENDING) {
            throw new ApiException(ProductErrorCode.INVALID_PRODUCT_STATUS);
        }
        request.setStatus(FeaturedRequestStatus.REJECTED);
        request.setAdminMessage(requestActionDTO.getMessage());
        featuredRequestRepository.save(request);
        EmailDetailsDTO emailDetails = new EmailDetailsDTO();

        emailDetails.setRecipient(request.getVendor().getEmail());
        emailDetails.setSubject("Featured Product Request Rejected - LocalConnect");

        emailDetails.setMsgBody(
                "Dear " + request.getVendor().getName() + ",\n\n" +
                        "Your featured product request has been reviewed and unfortunately was rejected.\n\n" +
                        "Product: " + request.getProduct().getProductName() + "\n" +
                        "Status: REJECTED\n\n" +
                        "Reason from Admin:\n" +
                        request.getAdminMessage() + "\n\n" +
                        "You may review the feedback and submit another featured request if applicable.\n\n" +
                        "Thank you for using LocalConnect.\n\n" +
                        "Regards,\n" +
                        "LocalConnect Team"
        );

        emailService.sendSimpleMail(emailDetails);
        return "Featured request rejected successfully";
    }

    @Override
    public Page<FeaturedProductResponseDTO> getFeaturedRequests(FeaturedRequestStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<FeaturedRequest> featuredRequests;

        if (status != null) {
            featuredRequests = featuredRequestRepository.findByStatus(status, pageable);

        } else {
            featuredRequests = featuredRequestRepository.findAll(pageable);
        }
        return featuredRequests.map(featuredRequestMapper::mapToFeaturedProductResponseDTO);
    }
}
