package com.fansa.response;

import com.fansa.common.entity.Customer;
import com.fansa.common.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingCartResponse {
    private Long id;
    private int quantity;
    private ProductResponse product;

}
