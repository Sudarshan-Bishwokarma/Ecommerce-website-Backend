package com.ecommerce.ecommercewebsite.controllers.user;

import com.ecommerce.ecommercewebsite.services.OrderPaymentService;
import com.ecommerce.ecommercewebsite.services.payment.EsewaPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/api/user")
@RestController
public class OrderPaymentController {

    @Autowired
    OrderPaymentService orderPaymentService;
    @Autowired
    private EsewaPaymentService esewaPaymentService;

    //  handle order  payment success
    @GetMapping("/order-payment/success")
    public RedirectView paymentSuccess(@RequestParam("data") String data) {
        String transactionUuid = esewaPaymentService.verifyPayment(data);

        orderPaymentService.handlePaymentSuccess(transactionUuid);

        return new RedirectView(
                "http://localhost:5173/order/payment/success?transactionUuid="
                        + transactionUuid
        );

    }

    // handle failure url

    @GetMapping("/order-payment/failure")
    public RedirectView paymentFailure(
            @RequestParam(value = "data", required = false) String data,
            @RequestParam(value = "transactionUuid", required = false) String transactionUuid
    ) {
        if (data != null) {
            transactionUuid = esewaPaymentService.verifyPayment(data);
        }
        if (transactionUuid != null) {
            orderPaymentService.handlePaymentFailure(transactionUuid);
        }
        return new RedirectView(
                "http://localhost:5173/order/payment/failure"
        );
    }
}
