package com.example.limitorder.cache;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CommonOrderTest {

    static public Order createNewOrder(final long orderId, final double price, final double volume) {
        return new Order(orderId, price, volume);
    }

    static public void createTest(LimitOrderServiceRepo limitOrderRepository) {
        int orderInitId = 100000;
        int size = 1000000;
        final var ordersGreater = CommonOrderTest.createRandomOrders(orderInitId,  size);
        long initLatency = System.nanoTime();
        limitOrderRepository.addAllOrders(ordersGreater);
        System.out.println("Latency adding " + ordersGreater.size() + " orders: "+(System.nanoTime()-initLatency)/1000000.0+" ms");
    }


    static public void executeTests(LimitOrderServiceRepo limitOrderRepository) {
        double jump = 1000.0;
        double [] lat = new double[1000000];
        for (int i = 0; i < 100000; i++) {
            System.out.println("___________________________________________________");
            System.out.println("Test run: " + i);
            System.out.println("___________________________________________________");
            double greaterPrice = 1100000 - jump;
            double lowerPrice = 100000 + jump-1;
            lat[i] = executeQuery(limitOrderRepository, greaterPrice, lowerPrice);
        }
        calculatePercentile(lat);
    }

    private static void calculatePercentile(final double[] lat) {
        System.out.println("___________________________________________________");
        System.out.println("____________________ Percentile ___________________");

        final Percentile percentile = new Percentile();
        percentile.setData(getData(lat));

        int ms = 1000000;
        List.of(1.0, 10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 95.0, 98.0, 99.0, 99.5, 99.8, 99.9, 99.95, 99.99,100.0)
                .stream()
                .forEach(pctl -> System.out.println(pctl + " %: " + percentile.evaluate(pctl)/ms));
    }

    private static double[] getData(final double[] lat) {
        System.out.println("Original size: "+ lat.length);
        final Set<Double> collect = Arrays.stream(lat).boxed().collect(Collectors.toSet());
        collect.remove(0);
        final double [] data = new double[collect.size()];
        final Iterator<Double> iterator = collect.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            data[count] = iterator.next();
            count++;
        }
        return data;
    }

    static private double executeQuery(LimitOrderServiceRepo limitOrderRepository, double greaterPrice, double lowerPrice) {
        System.out.println("limitOrderCache size: " + limitOrderRepository.size());
        //double greaterPrice = CommonOrderTest.createRandomNumber(200001, 299999);
        long initLatency = System.nanoTime();
        System.out.println("N. of orders found greater " + greaterPrice + ": "+limitOrderRepository.getLimitPricesGreaterEqualThan(greaterPrice).size());
        double lat = (System.nanoTime()-initLatency);
        System.out.println("Latency get greater: "+lat/1000000.0+" ms");

        //double lowerPrice = CommonOrderTest.createRandomNumber(100001, 199999);
        initLatency = System.nanoTime();
        System.out.println("N. of orders found lower " + lowerPrice + ": "+limitOrderRepository.getLimitPricesLowerEqualThan(lowerPrice).size());
        lat = (System.nanoTime()-initLatency);
        System.out.println("Latency get lower: "+lat/1000000.0+" ms");
        return lat;
    }

    static public List<Order> createRandomOrders(int orderInitId, int size) {
        /*
        return IntStream.range(orderInitId, orderInitId+size)
                .mapToObj(i -> createNewOrder(i, createRandomNumber(min, max), createRandomNumber(1, 100)))
                .collect(Collectors.toList());
        */
        return IntStream.range(orderInitId, orderInitId+size)
                .mapToObj(i -> {
                    Order order = createNewOrder(i, i, orderInitId+(i*10));
                    //System.out.println(order);
                    return order;
                })
                .collect(Collectors.toList());
    }

    static public Double createRandomNumber(double min, double max) {
        return Math.round(min + (new Random().nextDouble() * (max - min))) * 1.0;
    }
}
