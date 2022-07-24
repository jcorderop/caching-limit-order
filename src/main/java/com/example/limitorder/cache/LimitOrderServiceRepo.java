package com.example.limitorder.cache;

import java.util.Collection;
import java.util.List;

public interface LimitOrderServiceRepo {
    LimitOrderResponse createLimitOrder (Order order) ;
    LimitOrderResponse getLimitOrder(Long id);
    Collection<Order> getLimitPricesGreaterEqualThan(Double price);
    Collection<Order> getLimitPricesLowerEqualThan(Double price);
    void addAllOrders(List<Order> orders);
    Long size();
}
