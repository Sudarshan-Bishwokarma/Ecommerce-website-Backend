package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.PaymentRequestDTO;
import com.ecommerce.ecommercewebsite.dto.PaymentResponseDTO;
import com.ecommerce.ecommercewebsite.enums.*;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.model.FeaturedPayment;
import com.ecommerce.ecommercewebsite.model.FeaturedPlan;
import com.ecommerce.ecommercewebsite.model.FeaturedRequest;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.repositories.FeaturedPaymentRepository;
import com.ecommerce.ecommercewebsite.repositories.FeaturedPlanRepository;
import com.ecommerce.ecommercewebsite.repositories.FeaturedRequestRepository;
import com.ecommerce.ecommercewebsite.repositories.ProductRepository;
import com.ecommerce.ecommercewebsite.services.payment.EsewaPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    FeaturedRequestRepository featuredRequestRepository;
    @Autowired
    private FeaturedPlanRepository featuredPlanRepository;

    @Autowired
    private EsewaPaymentService esewaPaymentService;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FeaturedPaymentRepository featuredPaymentRepository;

    @Override
    public PaymentResponseDTO initiatePayment(Long featuredRequestId, PaymentRequestDTO paymentRequestDTO) {
        FeaturedRequest featuredRequest = featuredRequestRepository.findById(featuredRequestId).orElseThrow(() -> new ApiException(ProductErrorCode.FEATURED_PRODUCT_REQUEST_NOT_FOUND));
        FeaturedPlan plan = featuredPlanRepository.findById(paymentRequestDTO.getFeaturedPlanId()).orElseThrow(() -> new ApiException(FeaturedPlanErrorCode.FEATURED_PLAN_NOT_FOUND));
        if (featuredRequest.getStatus() != FeaturedRequestStatus.APPROVED) {
            throw new ApiException(PaymentErrorCode.PAYMENT_NOT_ALLOWED);

        }
        FeaturedPayment featuredPayment = new FeaturedPayment();
        featuredPayment.setFeaturedRequest(featuredRequest);
        featuredPayment.setFeaturedPlan(plan);
        featuredPayment.setAmount(plan.getPrice());
        featuredPayment.setPaymentMethod(paymentRequestDTO.getPaymentMethod());
        featuredPayment.setStatus(PaymentStatus.PENDING);
        featuredPaymentRepository.save(featuredPayment);
        if (paymentRequestDTO.getPaymentMethod() == PaymentMethod.ESEWA) {

            PaymentResponseDTO response = esewaPaymentService.createPayment(featuredRequestId, plan);

            featuredPayment.setTransactionId(response.getTransactionId());
            featuredPaymentRepository.save(featuredPayment);

            return response;

        } else if (paymentRequestDTO.getPaymentMethod() == PaymentMethod.KHALTI) {
            return null;
        } else {
            throw new ApiException(PaymentErrorCode.INVALID_PAYMENT_METHOD);
        }

    }

    @Transactional
    @Override
    public String handlePaymentSuccess(String transactionUuid) {
        FeaturedRequest featuredRequest = featuredRequestRepository.findByTransactionUuid(transactionUuid).orElseThrow(() -> new ApiException(ProductErrorCode.FEATURED_PRODUCT_REQUEST_NOT_FOUND));
        FeaturedPayment featuredPayment = featuredPaymentRepository.findByTransactionId(transactionUuid).orElseThrow(() -> new ApiException(PaymentErrorCode.PAYMENT_NOT_FOUND));
        if (featuredRequest.getStatus() != FeaturedRequestStatus.APPROVED) {
            throw new ApiException(PaymentErrorCode.PAYMENT_NOT_ALLOWED);
        }
        featuredPayment.setStatus(PaymentStatus.SUCCESS);
        featuredPaymentRepository.save(featuredPayment);
        featuredRequest.setStatus(FeaturedRequestStatus.PAID);

        LocalDateTime now = LocalDateTime.now();
        featuredRequest.setStartDate(now);

        featuredRequest.setEndDate(now.plusDays(featuredRequest.getFeaturedPlan().getDurationDays()));
        Product product = featuredRequest.getProduct();

        product.setFeatured(true);

        productRepository.save(product);

        featuredRequestRepository.save(featuredRequest);
        return "Featured product payment completed successfully";
    }

    @Override
    @Transactional
    public String handlePaymentFailure(String transactionUuid) {

        FeaturedPayment featuredPayment =
                featuredPaymentRepository.findByTransactionId(transactionUuid)
                        .orElseThrow(() -> new ApiException(
                                PaymentErrorCode.PAYMENT_NOT_FOUND
                        ));

        if (featuredPayment.getStatus() == PaymentStatus.SUCCESS) {
            throw new ApiException(
                    PaymentErrorCode.PAYMENT_ALREADY_COMPLETED
            );
        }

        featuredPayment.setStatus(PaymentStatus.FAILED);

        featuredPaymentRepository.save(featuredPayment);

        return "Featured product payment failed";
    }

}
