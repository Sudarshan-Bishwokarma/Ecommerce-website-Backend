package com.ecommerce.ecommercewebsite.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EsewaSignatureUtil {
    public static String generateSignature(
            String totalAmount,
            String transactionUuid,
            String productCode,
            String secretKey
    ) {

        try {

            String message =
                    "total_amount=" + totalAmount
                            + ",transaction_uuid=" + transactionUuid
                            + ",product_code=" + productCode;


            System.out.println("SIGNATURE MESSAGE = " + message);


            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec secretKeySpec =
                    new SecretKeySpec(
                            secretKey.getBytes(StandardCharsets.UTF_8),
                            "HmacSHA256"
                    );


            mac.init(secretKeySpec);


            byte[] hash =
                    mac.doFinal(
                            message.getBytes(StandardCharsets.UTF_8)
                    );


            return Base64.getEncoder()
                    .encodeToString(hash);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
