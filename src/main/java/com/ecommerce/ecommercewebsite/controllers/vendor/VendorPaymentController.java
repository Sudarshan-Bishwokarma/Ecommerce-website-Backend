package com.ecommerce.ecommercewebsite.controllers.vendor;

import com.ecommerce.ecommercewebsite.dto.PaymentRequestDTO;
import com.ecommerce.ecommercewebsite.dto.PaymentResponseDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.PaymentService;
import com.ecommerce.ecommercewebsite.services.payment.EsewaPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
@RequestMapping("/api/vendor/")
public class VendorPaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private EsewaPaymentService esewaPaymentService;
    // initiate     payment for featured   product

    @PostMapping("/featured-request/{id}/payment")
    public ResponseEntity<ApiResponse<PaymentResponseDTO>> initiatePayment(@PathVariable Long id, @RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = paymentService.initiatePayment(id, request);
        ApiResponse<PaymentResponseDTO> apiResponse = new ApiResponse<>("success", response);
        return ResponseEntity.ok(apiResponse);

    }

    //   handle payment success

    @GetMapping("/featured-payment/success")
    public RedirectView paymentSuccess(@RequestParam("data") String data) {

        String transactionUuid = esewaPaymentService.verifyPayment(data);

        paymentService.handlePaymentSuccess(transactionUuid);

        return new RedirectView(
                "http://localhost:5173/vendor/payment/success?transactionUuid="
                        + transactionUuid
        );
    }
}
