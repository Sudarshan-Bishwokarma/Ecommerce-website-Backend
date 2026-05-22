package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.ProductRequestDTO;
import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.exception.CategoryNotFoundException;
import com.ecommerce.ecommercewebsite.exception.ProductNotFoundException;
import com.ecommerce.ecommercewebsite.model.Category;
import com.ecommerce.ecommercewebsite.model.District;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.repositories.CategoryRepository;
import com.ecommerce.ecommercewebsite.repositories.DistrictRepository;
import com.ecommerce.ecommercewebsite.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private DistrictRepository districtRepository;

    @Override
    public ProductResponseDTO addProduct(ProductRequestDTO request, MultipartFile file) {
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
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        if (allProducts.isEmpty()) {
            return new ArrayList<>();
        }
        List<ProductResponseDTO> dtos = new ArrayList<>();
        for (Product product : allProducts) {
            ProductResponseDTO dto = mapToDTO(product);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategoryId(Long id) {
        List<Product> products = productRepository.findByCategory_id(id);
        if (products.isEmpty()) {
            throw new CategoryNotFoundException("Category Not Found");
        }
        List<ProductResponseDTO> dtos = new ArrayList<>();
        for (Product product : products) {
            ProductResponseDTO dto = mapToDTO(product);
            dtos.add(dto);
        }
        return dtos;

    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
        ProductResponseDTO dto = mapToDTO(product);

        return dto;
    }

    @Override
    public List<ProductResponseDTO> filterProductsByPrice(Double minPrice, Double maxPrice) {
        List<Product> allProducts = productRepository.findByProductPriceBetween(minPrice, maxPrice);
        if (allProducts.isEmpty()) {
            throw new ProductNotFoundException("Product Not Found");
        }
        List<ProductResponseDTO> dtos = new ArrayList<>();
        for (Product product : allProducts) {
            ProductResponseDTO dto = mapToDTO(product);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public Page<ProductResponseDTO> sortProducts(String sortType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> allProducts;
        switch (sortType) {
            case "priceAsc":
                allProducts = productRepository.findAllByOrderByProductPriceAsc(pageable);
                break;
            case "priceDesc":
                allProducts = productRepository.findAllByOrderByProductPriceDesc(pageable);
                break;
            case "nameAsc":
                allProducts = productRepository.findAllByOrderByProductNameAsc(pageable);
                break;
            case "nameDesc":
                allProducts = productRepository.findAllByOrderByProductNameDesc(pageable);
                break;
            default:
                allProducts = productRepository.findAll(pageable);

        }
        return allProducts.map(this::mapToDTO);
    }

    @Override
    public Page<ProductResponseDTO> getAllProductsByDistrict(Long id, int page, int size) {
        // check  district  exist or not
        District district = districtRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.DISTRICT_NOT_FOUND));
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findByDistrict_Id(id, pageable);

        return products.map(this::mapToDTO);
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