
package com.example.SmartCart.Mapper.Cart;

import com.example.SmartCart.Cart.Dto.Response.CartItemRes;
import com.example.SmartCart.Cart.Entity.CartItem;
import com.example.SmartCart.Products.Entity.Product;
import com.example.SmartCart.Products.Entity.ProductImage;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Comparator;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "cartItemId", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "unitPrice", source = "product.price")
    @Mapping(target = "availableStock", source = "product.stock")
    @Mapping(target = "thumbnail",
            expression = "java(getThumbnail(cartItem.getProduct()))")
    @Mapping(target = "subtotal", ignore = true)
    CartItemRes toCartItemResponse(CartItem cartItem);

    default String getThumbnail(Product product) {

        if (product.getImages() == null ||
                product.getImages().isEmpty()) {
            return null;
        }

        return product.getImages().stream()
                .filter(image ->
                        Boolean.TRUE.equals(image.getPrimaryImage()))
                .findFirst()
                .or(() -> product.getImages().stream()
                        .min(Comparator.comparing(
                                ProductImage::getDisplayOrder,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()
                                )
                        )))
                .map(ProductImage::getImageUrl)
                .orElse(null);
    }
}
