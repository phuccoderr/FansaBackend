package com.fansa.request;

import com.fansa.common.entity.order.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutInfo {
    public Payment payment;
    public String address;
    public String name;
    public String phone;
    public float total;
}
