package com.fansa.controller.shoppingcart;

import com.fansa.common.entity.ShoppingCart;
import com.fansa.controller.customer.CustomerNotFoundException;
import com.fansa.controller.customer.CustomerService;
import com.fansa.controller.product.ProductNotFoundException;
import com.fansa.response.ProductResponse;
import com.fansa.response.ShoppingCartResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth/cart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private CustomerService customerService;

    @PostMapping("/add/{productId}/{quantity}")
    public ResponseEntity<?> addProductToCart(@PathVariable(name = "productId") Long productId,
                                              @PathVariable(name = "quantity") Integer quantity,
                                              @RequestParam Long customerId) {
        try {

            if (quantity <= 0) {
                return ResponseEntity.badRequest().body("quantity > 0!");
            }

            cartService.addProduct(productId,quantity,customerId);
            return ResponseEntity.ok("");

        } catch (ProductNotFoundException | CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProductInCart(@PathVariable Long productId,
                                                 @RequestParam Long customerId) {
        try {
            cartService.removeCart(customerId,productId);
            return ResponseEntity.ok("");
        } catch (ProductNotFoundException | CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCartByCustomer(@PathVariable Long customerId) {
        try {
            List<ShoppingCart> carts = cartService.getCarts(customerId);

            return ResponseEntity.ok(listEntityToDTO(carts));
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private List<ShoppingCartResponse> listEntityToDTO(List<ShoppingCart> carts) {
        List<ShoppingCartResponse> dtos = new ArrayList<>();
        carts.forEach( item -> {
            dtos.add(entityToDTO(item));
        });
        return dtos;
    }

    private ShoppingCartResponse entityToDTO(ShoppingCart cart) {

        ProductResponse productDTO = ProductResponse.builder()
                .id(cart.getProduct().getId())
                .name(cart.getProduct().getName())
                .mainImage(cart.getProduct().getMainImage())
                .alias(cart.getProduct().getAlias())
                .shortDescription(cart.getProduct().getShortDescription())
                .fullDescription(cart.getProduct().getFullDescription())
                .cost(cart.getProduct().getCost())
                .price(cart.getProduct().getPrice())
                .sale(cart.getProduct().getSale())
                .productImages(cart.getProduct().getProductImages())
                .productDetails(cart.getProduct().getProductDetails()).build();

        return ShoppingCartResponse.builder()
                .id(cart.getId())
                .quantity(cart.getQuantity())
                .product(productDTO).build();
    }
}
