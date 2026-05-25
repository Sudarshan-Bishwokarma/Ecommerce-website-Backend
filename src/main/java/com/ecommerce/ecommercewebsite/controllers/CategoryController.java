package com.ecommerce.ecommercewebsite.controllers;

import com.ecommerce.ecommercewebsite.model.Category;
import com.ecommerce.ecommercewebsite.repositories.CategoryRepository;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //  fetch all  categories
    @GetMapping("/all-categories")
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        ApiResponse<List<Category>> response = new ApiResponse<>("Categories Fetched Successfully", categories);
        return ResponseEntity.ok(response);
    }

}
