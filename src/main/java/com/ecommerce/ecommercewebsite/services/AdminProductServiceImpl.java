package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.ProductRequestDTO;
import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.exception.ProductNotFoundException;
import com.ecommerce.ecommercewebsite.model.Category;
import com.ecommerce.ecommercewebsite.model.District;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.CategoryRepository;
import com.ecommerce.ecommercewebsite.repositories.DistrictRepository;
import com.ecommerce.ecommercewebsite.repositories.ProductRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class AdminProductServiceImpl implements AdminProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ProductResponseDTO addProduct(ProductRequestDTO request, MultipartFile file) {
        // get the  logged  user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User admin = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));

        District district = districtRepository.findById(request.getDistrictId()).orElseThrow(() -> new ApiException(ProductErrorCode.DISTRICT_NOT_FOUND));
        // fetching category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ApiException(ProductErrorCode.CATEGORY_NOT_FOUND));
        productRepository.findByProductName(request.getProductName()).ifPresent(existingProduct -> {
            throw new ApiException(ProductErrorCode.PRODUCT_ALREADY_EXIST);
        });
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setProductPrice(request.getProductPrice());
        product.setProductDescription(request.getProductDescription());
        product.setCategory(category);
        product.setDistrict(district);
        product.setAdmin(admin);
        try {
            product.setProductImage(file.getBytes());
        } catch (Exception e) {
            throw new ApiException(ProductErrorCode.IMAGE_NOT_FOUND);
        }
        Product saved = productRepository.save(product);

        ProductResponseDTO responseDTO = mapToDTO(saved);
        return responseDTO;
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        // check if the   product exist in the database
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));
        product.setProductName(productRequestDTO.getProductName());
        product.setProductPrice(productRequestDTO.getProductPrice());
        product.setProductDescription(productRequestDTO.getProductDescription());
        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category Not Found"));
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        System.out.println("Product Updated Successfully");

        //  prepared for response
        ProductResponseDTO responseDTO = mapToDTO(savedProduct);

        return responseDTO;
    }

    @Override
    public String deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));
        productRepository.delete(product);
        return "Product Deleted Successfully";
    }

    @Override
    public Page<ProductResponseDTO> getMyProducts(String email, int page, int size) {
        // fetch the user   first
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        Long admin_id = user.getId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> myProducts = productRepository.findByAdmin_Id(admin_id, pageable);
        if (!myProducts.hasContent()) {
            throw new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND);
        }
        return myProducts.map(this::mapToDTO);

    }

    @Override
    public Page<ProductResponseDTO> getMySortProducts(String email, String sortType, int page, int size) {
        User admin = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        Long admin_id = admin.getId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> sortedProducts;
        switch (sortType) {
            case "priceAsc":
                sortedProducts = productRepository.findByAdmin_IdOrderByProductPriceAsc(admin_id, pageable);
                break;
            case "priceDesc":
                sortedProducts = productRepository.findByAdmin_IdOrderByProductPriceDesc(admin_id, pageable);
                break;
            case "nameAsc":
                sortedProducts = productRepository.findByAdmin_IdOrderByProductNameAsc(admin_id, pageable);
                break;
            case "nameDesc":
                sortedProducts = productRepository.findByAdmin_IdOrderByProductNameDesc(admin_id, pageable);
                break;
            default:
                sortedProducts = productRepository.findByAdmin_Id(admin_id, pageable);

        }
        return sortedProducts.map(this::mapToDTO);
    }

    @Override
    public Page<ProductResponseDTO> getProducts(String email, Long districtId, Long categoryId, String sortType, int page, int size) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        Long admin_id = user.getId();
        Pageable pageable;
        if (sortType != null) {
            switch (sortType) {
                case "priceAsc":
                    pageable = PageRequest.of(page, size, Sort.by("productPrice").ascending());
                    break;
                case "priceDesc":
                    pageable = PageRequest.of(page, size, Sort.by("productPrice").descending());
                    break;
                case "nameAsc":
                    pageable = PageRequest.of(page, size, Sort.by("productName").ascending());
                    break;
                case "nameDesc":
                    pageable = PageRequest.of(page, size, Sort.by("productName").descending());
                    break;
                default:
                    pageable = PageRequest.of(page, size);
            }

        } else {
            pageable = PageRequest.of(page, size);
        }
        Page<Product> allProducts;
        if (districtId != null && categoryId != null) {
            allProducts = productRepository.findByAdmin_IdAndDistrict_IdAndCategory_Id(admin_id, districtId, categoryId, pageable);

        } else if (districtId != null) {
            allProducts = productRepository.findByAdmin_IdAndDistrict_Id(admin_id, districtId, pageable);
        } else if (categoryId != null) {
            allProducts = productRepository.findByAdmin_IdAndCategory_Id(admin_id, categoryId, pageable);
        } else {
            allProducts = productRepository.findByAdmin_Id(admin_id, pageable);
        }
        return allProducts.map(this::mapToDTO);
    }


    // ------------------------Helper Class ------------------------
    private ProductResponseDTO mapToDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setProductPrice(product.getProductPrice());
        dto.setProductCategory(product.getCategory().getCategoryName());
        dto.setDistrictName(product.getDistrict().getDistrictName());
        if (product.getProductImage() != null) {
            String base64 = Base64.getEncoder().encodeToString(product.getProductImage());
            dto.setProductImageBase64(base64);
        }

        return dto;
    }
}
/*
//First, get the user’s cart or create a new one if it doesn’t exist.
// Then, find the product by its ID. Check if the product is already in the cart —
// if yes, increase the quantity and update the total price; if no, create a new cart item with the product, quantity, and total price.
 Finally, save the cart item and return the DTO with the cart item details
*/