package com.example.SmartCart.Products.Service.Impl;

import com.example.SmartCart.Mapper.Product.ProductMapper;
import com.example.SmartCart.Products.Dto.RequestDto.CreateProductRequestDto;
import com.example.SmartCart.Products.Dto.RequestDto.ProductSearchRequest;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDeleteResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDetailsResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import com.example.SmartCart.Products.Entity.Category;
import com.example.SmartCart.Products.Entity.Product;
import com.example.SmartCart.Products.Repository.CategoryRepository;
import com.example.SmartCart.Products.Repository.ProductRepository;
import com.example.SmartCart.Products.Service.ProductService;
import com.example.SmartCart.Products.enums.ProductStatus;
import com.example.SmartCart.User.Entity.SellerProfile;
import com.example.SmartCart.User.Repository.SellerProfileRepository;
import com.example.SmartCart.common.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerProfileRepository sellerProfileRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequestDto dto) {

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + dto.categoryId()));

        // TODO: Replace with authenticated seller after JWT authentication
        Long userId = 1L;
        SellerProfile seller = sellerProfileRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller not found."));

        Product product = productMapper.toEntity(dto);

        product.setCategory(category);
        product.setSeller(seller);
        product.setStatus(ProductStatus.ACTIVE);

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public Page<ProductResponse> getProducts(Pageable pageable,
                                             ProductSearchRequest searchRequest) {

        return productRepository
                .findByStatus(ProductStatus.ACTIVE, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public ProductDetailsResponse getProductById(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + productId));

        return productMapper.toDetails(product);
    }

    @Override
    public Page<ProductResponse> searchProducts(String keyword,
                                                Pageable pageable,
                                                ProductSearchRequest searchRequest) {

        return productRepository
                .findByNameContainingIgnoreCaseAndStatus(
                        keyword,
                        ProductStatus.ACTIVE,
                        pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public ProductDeleteResponse delProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id: " + productId));

        product.setStatus(ProductStatus.INACTIVE);

        productRepository.save(product);

        return new ProductDeleteResponse(
                product.getId(),
                "Product deactivated successfully."
        );
    }
}
