package com.example.limitorder.cache.collections;

import com.example.limitorder.cache.CommonOrderTest;
import com.example.limitorder.cache.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class LimitOrderCacheTest {

    @Autowired
    LimitOrderCache limitOrderCache;

    @BeforeEach
    void setUp() {
        limitOrderCache.clear();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addNewOrder() {
        //given
        final Order order = CommonOrderTest.createNewOrder(1L, CommonOrderTest.createRandomNumber(200001.0, 299999.0), 1000.0);

        //when
        limitOrderCache.addOrder(order);
        System.out.println(limitOrderCache);

        //then
        assertThat(limitOrderCache.size()).isEqualTo(1);
        List<ConcurrentNavigableMap<Double, List<Long>>> limitPricesHit = limitOrderCache
                .getLimitPricesGreaterEqualThan(order.limitPrice());

        assertThat(limitPricesHit.size()).isEqualTo(1);
        final Iterator<ConcurrentNavigableMap<Double, List<Long>>> it = limitPricesHit.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
        /*
        limitPricesHit.first();
        limitPricesHit.last();
        limitPricesHit.lower(1.0);
        limitPricesHit.higher(1.0);
        limitPricesHit.ceiling(1.0);
        limitPricesHit.floor(1.0);
        */
        assertThat(limitOrderCache.size()).isEqualTo(0);
        assertThat(limitPricesHit.size()).isEqualTo(1);
        //Map<Double, List<Long>> volumes = limitOrderCache.remove(limitPricesHit.first());

        //System.out.println(volumes);
        //assertThat(volumes.size()).isEqualTo(1);
        //assertThat(limitOrderCache.getVolumes(price).getOrders()).isEqualTo(order.orderId());

    }

    @Test
    void addNewOrder_withSamePrice_differentVolume() {
        //given
        final Order order = CommonOrderTest.createNewOrder(1L,
                30000,
                1000.0);
        final Order orderNext = CommonOrderTest.createNewOrder(order.orderId()+1,
                order.limitPrice(),
                10000.0);
        //when
        limitOrderCache.addOrder(order);
        limitOrderCache.addOrder(orderNext);

        //then
        System.out.println(limitOrderCache);
        final List<ConcurrentNavigableMap<Double, List<Long>>> limitPricesGreaterEqualThan = limitOrderCache
                .getLimitPricesGreaterEqualThan(order.limitPrice());
        /*
        final ConcurrentNavigableMap<Double, Queue<Long>> volumes = limitPrices.get(order.limitPrice());

        assertThat(volumes.remove(volumes.firstKey()).poll()).isEqualTo(order.orderId());
        assertThat(volumes.remove(volumes.firstKey()).poll()).isEqualTo(orderNext.orderId());

         */
    }

    @Test
    void addNewOrder_withSamePrice_sameVolume() {
        //given
        final Order order = CommonOrderTest.createNewOrder(1L,
                30000.0,
                1000.0);
        final Order orderNext = CommonOrderTest.createNewOrder(order.orderId()+1,
                order.limitPrice(),
                order.volume());
        //when
        limitOrderCache.addOrder(order);
        limitOrderCache.addOrder(orderNext);

        //then
        System.out.println(limitOrderCache);
        final List<ConcurrentNavigableMap<Double, List<Long>>> limitPricesHit = limitOrderCache
                .getLimitPricesGreaterEqualThan(order.limitPrice());
        /*
        final ConcurrentNavigableMap<Double, Queue<Long>> volumes = limitPrices.get(order.limitPrice());

        Queue<Long> orders = volumes.remove(volumes.firstKey());
        assertThat(orders.poll()).isEqualTo(order.orderId());
        assertThat(orders.poll()).isEqualTo(orderNext.orderId());

         */
    }

    @Test
    void addNewOrder_withDifferentPrice_sameVolume() {
        //given
        final Order order = CommonOrderTest.createNewOrder(1L,
                30000.0,
                1000.0);
        final Order orderNext = CommonOrderTest.createNewOrder(order.orderId()+1,
                30001.0,
                order.volume());
        //when
        limitOrderCache.addOrder(order);
        limitOrderCache.addOrder(orderNext);

        //then
        System.out.println(limitOrderCache);
        final List<ConcurrentNavigableMap<Double, List<Long>>> limitPricesHit = limitOrderCache
                .getLimitPricesGreaterEqualThan(order.limitPrice());
        /*
        final ConcurrentNavigableMap<Double, Queue<Long>> volume = limitPrices.remove(limitPrices.firstKey());
        assertThat(volume.remove(volume.firstKey()).poll()).isEqualTo(order.orderId());

        final ConcurrentNavigableMap<Double, Queue<Long>> volumeNext = limitPrices.remove(limitPrices.firstKey());
        assertThat(volumeNext.remove(volumeNext.firstKey()).poll()).isEqualTo(orderNext.orderId());
        */
    }

    @Test
    void addNewOrder_withDifferentPrice_differentVolume() {
        //given
        final Order order = CommonOrderTest.createNewOrder(1L,
                30000.0,
                1000.0);
        final Order orderNext = CommonOrderTest.createNewOrder(order.orderId()+1,
                30001.0,
                10000.0);
        //when
        limitOrderCache.addOrder(order);
        limitOrderCache.addOrder(orderNext);

        //then
        System.out.println(limitOrderCache);
        final List<ConcurrentNavigableMap<Double, List<Long>>> limitPricesHit = limitOrderCache
                .getLimitPricesGreaterEqualThan(order.limitPrice());
        /*
        final ConcurrentNavigableMap<Double, Queue<Long>> volume = limitPrices.remove(limitPrices.firstKey());
        assertThat(volume.remove(volume.firstKey()).poll()).isEqualTo(order.orderId());

        final ConcurrentNavigableMap<Double, Queue<Long>> volumeNext = limitPrices.remove(limitPrices.firstKey());
        assertThat(volumeNext.remove(volumeNext.firstKey()).poll()).isEqualTo(orderNext.orderId());

         */
    }
/*
    @Test
    void addLimitPrice() {
        int orderInitId = 100000;
        int size = 100000;
        final var ordersGreater = CommonOrderTest.createRandomOrders(orderInitId,
                size,
                200001,
                299999);
        long initLatency = System.currentTimeMillis();
        limitOrderCache.addAll(ordersGreater);
        System.out.println("Latency adding " + ordersGreater.size() + " orders: "+(System.currentTimeMillis()-initLatency)+" ms");

        final var ordersLower = CommonOrderTest.createRandomOrders(orderInitId,
                size,
                100001,
                199999);
        initLatency = System.currentTimeMillis();
        limitOrderCache.addAll(ordersLower);
        System.out.println("Latency adding " + ordersLower.size() + " orders: "+(System.currentTimeMillis()-initLatency)+" ms");

        System.out.println("limitOrderCache size: " + limitOrderCache.size());

        initLatency = System.currentTimeMillis();
        System.out.println("prices found greater: "+limitOrderCache.getLimitPricesGreaterEqualThan(290000.0).size());
        System.out.println("Latency get greater: "+(System.currentTimeMillis()-initLatency)+" ms");

        System.out.println("limitOrderCache size: " + limitOrderCache.size());
        initLatency = System.currentTimeMillis();
        System.out.println("prices found lower: "+limitOrderCache.getLimitPricesLowerEqualThan(110001.0).size());
        System.out.println("Latency get lower: "+(System.currentTimeMillis()-initLatency)+" ms");
    }
*/


    @Test
    void getGreaterEqual() {
    }

    @Test
    void getLessEqual() {
    }
}