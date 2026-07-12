package com.example.SmartCart.Products.Service.Impl;

import com.example.SmartCart.Mapper.Product.SellerMapper;
import com.example.SmartCart.Products.Dto.RequestDto.CreateProductRequestDto;
import com.example.SmartCart.Products.Dto.RequestDto.UpdateProductRequest;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductDeleteResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.ProductResponse;
import com.example.SmartCart.Products.Dto.ResponseDto.SellerProductResponse;
import com.example.SmartCart.Products.Entity.Category;
import com.example.SmartCart.Products.Entity.Product;
import com.example.SmartCart.Products.Repository.CategoryRepository;
import com.example.SmartCart.Products.Repository.ProductRepository;
import com.example.SmartCart.Products.Service.SellerProductService;
import com.example.SmartCart.User.Entity.SellerProfile;
import com.example.SmartCart.User.Repository.SellerProfileRepository;
import com.example.SmartCart.common.Exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerProductServiceImpl implements SellerProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final SellerProfileRepository sellerRepo;
    private final SellerMapper sellerMapper;

    @Override
    public ProductResponse createProduct(CreateProductRequestDto request) {

        Long sellerId = 1L;

        SellerProfile seller = sellerRepo.findById(sellerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller not found"));

        Category category = categoryRepo.findById(request.categoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        Product product = sellerMapper.toEntity(request);

        product.setSeller(seller);
        product.setCategory(category);

        Product saved = productRepo.save(product);

        return sellerMapper.toProductResponse(saved);
    }

    @Override
    public Page<SellerProductResponse> getSellerProducts(Pageable pageable) {

        Long sellerId = 1L;

        return productRepo.findBySeller_Id(sellerId, pageable)
                .map(sellerMapper::toSellerProductResponse);
    }

    @Override
    public ProductResponse updateProduct(Long productId,
                                         UpdateProductRequest request) {
        Long sellerId = 1L;

        Product product = productRepo.findByIdAndSeller_Id(productId, sellerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        sellerMapper.updateProductFromDto(request, product);

        if (request.categoryId() != null) {

            Category category = categoryRepo.findById(request.categoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Category not found"));

            product.setCategory(category);
        }

        Product updated = productRepo.save(product);

        return sellerMapper.toProductResponse(updated);
    }

    @Override
    public ProductDeleteResponse deleteProduct(Long productId) {

        Long sellerId = 1L;
        Product product = productRepo.findByIdAndSeller_Id(productId, sellerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        productRepo.delete(product);

        return new ProductDeleteResponse(
                product.getId(),
                "Product deleted successfully."
        );
    }
}