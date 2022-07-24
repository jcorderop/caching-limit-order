package com.example.limitorder.cache;


import com.example.limitorder.cache.Order;

public class LimitOrderResponse {

    private Order value;

    public LimitOrderResponse(Order value) {
        this.value  = value;
    }

    public Order getValue() {
        return value;
    }

    public void setValue(Order value) {
        this.value = value;
    }
}
