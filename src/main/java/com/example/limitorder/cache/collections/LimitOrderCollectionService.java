package com.example.limitorder.cache.collections;

import com.example.limitorder.cache.LimitOrderResponse;
import com.example.limitorder.cache.LimitOrderServiceRepo;
import com.example.limitorder.cache.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

@Service("limitOrderCollectionService")
public class LimitOrderCollectionService implements LimitOrderServiceRepo {

    private final ConcurrentNavigableMap<Double, List<Long>> limitPrices;
    private final ConcurrentMap<Long, Order> orders;

    public LimitOrderCollectionService() {
        this.limitPrices = new ConcurrentSkipListMap();
        this.orders = new ConcurrentHashMap<>();
    }


    @Override
    public LimitOrderResponse createLimitOrder(Order order) {
        if (limitPrices.containsKey(order.limitPrice())) {
            limitPrices.computeIfPresent(order.limitPrice(),(limitPrices, ids) -> {
                final List<Long> list = new ArrayList();
                list.addAll(ids);
                list.add(order.orderId());
                return list;
            });
        } else {
            limitPrices.putIfAbsent(order.limitPrice(), List.of(order.orderId()));
        }
        orders.put(order.orderId(), order);
        return new LimitOrderResponse(order);
    }

    @Override
    public LimitOrderResponse getLimitOrder(Long id) {
        return new LimitOrderResponse(orders.get(id));
    }

    @Override
    public Collection<Order> getLimitPricesGreaterEqualThan(Double price) {
        ConcurrentNavigableMap<Double, List<Long>> hits = limitPrices
                .tailMap(price, true);
        return getOrders(hits);
    }

    @Override
    public Collection<Order> getLimitPricesLowerEqualThan(Double price) {
        ConcurrentNavigableMap<Double, List<Long>> hits = limitPrices
                .descendingMap()
                .tailMap(price, true);
        return getOrders(hits);
    }

    private List<Order> getOrders(ConcurrentNavigableMap<Double, List<Long>> hits) {
        if (hits.isEmpty()) {
            return new ArrayList<>();
        } else {
            final List<Order> ordersHit = hits.values()
                    .stream()
                    .flatMap(Collection::stream)
                    .map(id -> orders.get(id))
                    //.map(id -> orders.remove(id))
                    .collect(Collectors.toList());
            /*
            hits.keySet().stream().forEach(key -> {
                limitPrices.remove(key);
            });
            */
            return ordersHit;
        }
    }
    @Override
    public void addAllOrders(List<Order> orders) {
        orders.stream().forEach(order -> createLimitOrder(order));
    }

    @Override
    public Long size() {
        System.out.println("Prices size: "+limitPrices.size());
        return Long.valueOf(orders.size());
    }
}
