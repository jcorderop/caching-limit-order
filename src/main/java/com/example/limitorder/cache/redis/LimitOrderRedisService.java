package com.example.limitorder.cache.redis;

import com.example.limitorder.cache.LimitOrderServiceRepo;
import com.example.limitorder.cache.Order;
import com.example.limitorder.cache.LimitOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

import java.util.stream.Collectors;

@Service("limitOrderRedisService")
public class LimitOrderRedisService implements LimitOrderServiceRepo {

    private static final String TABLE_NAME_ORDERS = "order";
    private static final String TABLE_NAME_LIMIT_PRICES = "limitPrice";

    Function<Long, String> createOrderRowName = (orderId) -> TABLE_NAME_ORDERS+":"+orderId;

    Function<Order, String> createLimitOrderTableName = (order) -> TABLE_NAME_ORDERS+":"+order.orderId()+":"+TABLE_NAME_LIMIT_PRICES;

    private final RedisTemplate<String, Long> redisTemplate;
    private final HashOperations<String, String, Order> hashOperations;
    private final ZSetOperations<String, Long> zSetOperations;

    @Autowired
    public LimitOrderRedisService(final RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    @Override
    public LimitOrderResponse createLimitOrder(final Order order) {
        //hashOperations.put(TABLE_NAME_ORDERS, createOrderRowName.apply(order.orderId()), order);
        zSetOperations.add(TABLE_NAME_LIMIT_PRICES, order.orderId(), order.limitPrice());
        return new LimitOrderResponse(order);
    }

    @Override
    public LimitOrderResponse getLimitOrder(final Long id) {
        return new LimitOrderResponse(hashOperations.get(TABLE_NAME_ORDERS, id));
    }

    @Override
    public Collection<Order> getLimitPricesGreaterEqualThan(final Double price) {
        Set<ZSetOperations.TypedTuple<Long>> typedTuples = zSetOperations.rangeByScoreWithScores(TABLE_NAME_LIMIT_PRICES, price, Double.MAX_VALUE);
        return getOrders(typedTuples);
    }

    @Override
    public Collection<Order> getLimitPricesLowerEqualThan(final Double price) {
        Set<ZSetOperations.TypedTuple<Long>> typedTuples = zSetOperations.reverseRangeByScoreWithScores(TABLE_NAME_LIMIT_PRICES, Double.MIN_VALUE, price);
        return getOrders(typedTuples);
    }

    private List<Order> getOrders(Set<ZSetOperations.TypedTuple<Long>> typedTuples) {
        if (typedTuples != null) {
            return typedTuples.stream().map(longTypedTuple -> hashOperations
                            .get(TABLE_NAME_ORDERS, longTypedTuple.getValue()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void addAllOrders(List<Order> orders) {
        Map<String, Order> collect = orders.stream().collect(Collectors.toMap(order -> createOrderRowName.apply(order.orderId()), order -> order));
        //hashOperations.putAll(TABLE_NAME_ORDERS, collect);
        orders.stream().forEach(order -> zSetOperations.add(TABLE_NAME_LIMIT_PRICES, order.orderId(), order.limitPrice()));
    }

    @Override
    public Long size() {
        return hashOperations.size(TABLE_NAME_ORDERS);
    }
}
