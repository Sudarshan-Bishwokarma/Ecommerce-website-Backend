package com.ecommerce.ecommercewebsite.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse<T> { //It allows your class to handle any type of data, without writing separate classes for each type.
    private String message;
    private T data;// T is a datatype, but a flexible, generic datatype that changes depending on how you use the class.
}
