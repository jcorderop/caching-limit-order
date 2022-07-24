package com.example.limitorder.cache.hazelcast;

import com.example.limitorder.cache.LimitOrderResponse;
import com.example.limitorder.cache.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

//https://docs.hazelcast.com/imdg/4.2/query/how-distributed-query-works
@RestController
@Slf4j
public class LimitOrderController {

    @Autowired
    private LimitOrderHazelcastService limitOrderService;

    @PostMapping("/add")
    public LimitOrderResponse addOrder(@RequestBody Order order) {
        log.info("New Order to add: "+order);
        return limitOrderService.createLimitOrder(order);
    }

    @GetMapping("/get/{orderId}")
    public LimitOrderResponse getOrder(@PathVariable("orderId")  Long id) {
        log.info("Querying order id: "+id);
        return limitOrderService.getLimitOrder(id);
    }

    @GetMapping("/triggerGE")
    public Collection<Order> triggerOrdersGreaterEqual(@RequestParam(value = "price") Double price) {
        log.info("Triggering order greater equal with price: "+price);
        return limitOrderService.getLimitPricesGreaterEqualThan(price);
    }

    @GetMapping("/triggerLE")
    public Collection<Order> triggerOrdersLessEqual(@RequestParam(value = "price") Double price) {
        log.info("Triggering order less equal with price: "+price);
        return limitOrderService.getLimitPricesLowerEqualThan(price);
    }

}
