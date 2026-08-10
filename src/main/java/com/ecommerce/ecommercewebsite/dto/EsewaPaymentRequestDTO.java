package com.ecommerce.ecommercewebsite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EsewaPaymentRequestDTO {

    @JsonProperty("amount") //Use amount as the JSON field name for this Java variable
    private BigDecimal amount;

    @JsonProperty("tax_amount")
    private BigDecimal tax_amount;

    @JsonProperty("total_amount")
    private BigDecimal total_amount;

    @JsonProperty("transaction_uuid")
    private String transaction_uuid;

    @JsonProperty("product_code")
    private String product_code;

    @JsonProperty("product_service_charge")
    private BigDecimal product_service_charge;

    @JsonProperty("product_delivery_charge")
    private BigDecimal product_delivery_charge;

    @JsonProperty("success_url")
    private String success_url;

    @JsonProperty("failure_url")
    private String failure_url;

    @JsonProperty("signed_field_names")
    private String signed_field_names;

    @JsonProperty("signature")
    private String signature;
}