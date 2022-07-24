package com.example.limitorder.cache.collections;

import com.example.limitorder.cache.Order;
import com.google.common.collect.ImmutableList;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@ToString
@Component
public class LimitOrderCache {

    //https://www.geeksforgeeks.org/concurrentskiplistset-in-java-with-examples/
    //prices -> Map, volume -> Map, orders -> list
    private final ConcurrentNavigableMap<Double, ConcurrentNavigableMap<Double, List<Long>>> limitPricesCache;


    public LimitOrderCache() {
        this.limitPricesCache = new ConcurrentSkipListMap();
    }

    public void addOrder(final Order order) {
        if (limitPricesCache.containsKey(order.limitPrice())) {
            limitPricesCache.computeIfPresent(order.limitPrice(), (limitPrice, volumeMap) -> {
                if (volumeMap.containsKey(order.volume())) {
                    volumeMap.computeIfPresent(order.volume(), addOrderIfPresent(order));
                } else {
                    volumeMap.computeIfAbsent(order.volume(), volume -> addOrderIfAbsent(order));
                }
                return volumeMap;
            });
        } else {
            limitPricesCache.computeIfAbsent(order.limitPrice(), limitPrice -> {
                final ConcurrentNavigableMap<Double, List<Long>> volume = new ConcurrentSkipListMap<>();
                volume.put(order.volume(), addOrderIfAbsent(order));
                return volume;
            });
        }
    }

    private List<Long> addOrderIfAbsent(Order order) {
        return ImmutableList.of(order.orderId());
    }

    private BiFunction<Double, List<Long>, List<Long>> addOrderIfPresent(Order order) {
        return (volume, orders) -> ImmutableList.<Long>builder()
                                        .addAll(orders)
                                        .add(order.orderId())
                                        .build();
    }

    public void addAll(List<Order> orders) {
        orders.stream()
                .forEach(order -> addOrder(order));
    }

    public int size() {
        return limitPricesCache.size();
    }

    public List<ConcurrentNavigableMap<Double, List<Long>>> getLimitPricesGreaterEqualThan(Double limitPrice) {
        return limitPricesCache
                .tailMap(limitPrice, true)
                .keySet().stream().map(price -> limitPricesCache.remove(price)).collect(Collectors.toList());
        /*
        return ImmutableSortedSet
                .copyOf(limitPricesCache
                        .tailMap(limitPrice, true)
                        .keySet());
        /*
        ImmutableSortedMap<Double, Map<Double, List<Long>>> doubleMapImmutableSortedMap = ImmutableSortedMap.copyOf(limitPricesCache.tailMap(limitPrice, true));
        */
    }
    public NavigableSetWrapper<Double> something(Double limitPrice) {

        return new NavigableSetWrapper(limitPricesCache
                .tailMap(limitPrice, true)
                .keySet());
        /*
        return ImmutableSortedSet
                .copyOf(limitPricesCache
                        .tailMap(limitPrice, true)
                        .keySet());
        /*
        ImmutableSortedMap<Double, Map<Double, List<Long>>> doubleMapImmutableSortedMap = ImmutableSortedMap.copyOf(limitPricesCache.tailMap(limitPrice, true));
        */
    }

    public List<ConcurrentNavigableMap<Double, List<Long>>> getLimitPricesLowerEqualThan(Double limitPrice) {
        return limitPricesCache
                .descendingMap()
                .tailMap(limitPrice, true)
                .keySet().stream().map(price -> limitPricesCache.remove(price)).collect(Collectors.toList());
        /*
        return ImmutableSortedSet
                .copyOf(limitPricesCache
                        .descendingMap()
                        .tailMap(limitPrice, true)
                        .keySet());

         */
    }

    public void clear() {
        limitPricesCache.clear();
    }

    public Map<Double, List<Long>> remove(Double first) {
        return limitPricesCache.remove(first);
    }
}
