package com.ecommerce.ecommercewebsite.services.payment;

import com.ecommerce.ecommercewebsite.config.EsewaConfig;
import com.ecommerce.ecommercewebsite.dto.EsewaPaymentRequestDTO;
import com.ecommerce.ecommercewebsite.dto.EsewaResponseDTO;
import com.ecommerce.ecommercewebsite.dto.PaymentResponseDTO;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.model.FeaturedPlan;
import com.ecommerce.ecommercewebsite.model.FeaturedRequest;
import com.ecommerce.ecommercewebsite.repositories.FeaturedRequestRepository;
import com.ecommerce.ecommercewebsite.utils.EsewaSignatureUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;

import java.math.BigDecimal;
import java.util.Base64;

@Service
public class EsewaPaymentServiceImpl implements EsewaPaymentService {
    @Autowired
    private EsewaConfig esewaConfig;
    @Autowired
    private FeaturedRequestRepository featuredRequestRepository;

    @Override
    public PaymentResponseDTO createPayment(Long featuredRequestId, FeaturedPlan featuredPlan) {

        FeaturedRequest featuredRequest = featuredRequestRepository.findById(featuredRequestId).orElseThrow(() -> new ApiException(ProductErrorCode.FEATURED_PRODUCT_REQUEST_NOT_FOUND));

        BigDecimal amount = featuredPlan.getPrice();

        BigDecimal totalAmount = amount.setScale(2, RoundingMode.HALF_UP); //Take the amount, keep only 2 decimal places, and round it properly

        String transactionUuid = "FEATURED-" + featuredRequestId + "-" + System.currentTimeMillis();

        featuredRequest.setTransactionUuid(transactionUuid);
        featuredRequestRepository.save(featuredRequest);


        String signature = EsewaSignatureUtil.generateSignature(
                totalAmount.toPlainString(),
                transactionUuid,
                esewaConfig.getProductCode(),
                esewaConfig.getSecretKey()
        );


        EsewaPaymentRequestDTO esewaRequest = new EsewaPaymentRequestDTO();

        esewaRequest.setAmount(totalAmount);
        esewaRequest.setTax_amount(BigDecimal.ZERO);
        esewaRequest.setTotal_amount(totalAmount);

        esewaRequest.setTransaction_uuid(transactionUuid);
        esewaRequest.setProduct_code(esewaConfig.getProductCode());

        esewaRequest.setProduct_service_charge(BigDecimal.ZERO);
        esewaRequest.setProduct_delivery_charge(BigDecimal.ZERO);

        esewaRequest.setSuccess_url(esewaConfig.getSuccessUrl());
        esewaRequest.setFailure_url(esewaConfig.getFailureUrl());

        esewaRequest.setSigned_field_names("total_amount,transaction_uuid,product_code");

        esewaRequest.setSignature(signature);

        PaymentResponseDTO response = new PaymentResponseDTO();

        response.setTransactionId(transactionUuid);
        response.setAmount(totalAmount);
        response.setPaymentUrl(esewaConfig.getPaymentUrl());
        response.setPaymentData(esewaRequest);

        return response;
    }

    @Override
    public String verifyPayment(String data) {

        try {

            byte[] decodedBytes = Base64.getDecoder().decode(data);

            String json = new String(decodedBytes);

            System.out.println("Decoded eSewa Response:");
            System.out.println(json);


            ObjectMapper mapper = new ObjectMapper();

            EsewaResponseDTO response =
                    mapper.readValue(json, EsewaResponseDTO.class);


            if (!"COMPLETE".equals(response.getStatus())) {
                throw new RuntimeException("Payment failed");
            }


            return response.getTransaction_uuid();


        } catch (Exception e) {

            throw new RuntimeException("Invalid eSewa response");

        }
    }
}
