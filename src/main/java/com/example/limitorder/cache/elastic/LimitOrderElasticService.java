package com.example.limitorder.cache.elastic;

import com.example.limitorder.cache.LimitOrderResponse;
import com.example.limitorder.cache.LimitOrderServiceRepo;
import com.example.limitorder.cache.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@AllArgsConstructor
@Slf4j
@Service("limitOrderElasticService")
public class LimitOrderElasticService implements LimitOrderServiceRepo {

    @Autowired
    private LimitOrderElasticRepository limitOrderElasticRepository;

    @Override
    public LimitOrderResponse createLimitOrder(Order order) {
         limitOrderElasticRepository.save(new LimitOrder(order.orderId(),
                 order.limitPrice(),
                 order.volume()));
        return new LimitOrderResponse(order);
    }

    @Override
    public LimitOrderResponse getLimitOrder(Long id) {
        Optional<LimitOrder> limitOrder = limitOrderElasticRepository.findById(id);
        return limitOrder.map(order -> new LimitOrderResponse(new Order(order.getId(),
                order.getLimitPrice(),
                order.getVolume()))).orElseGet(() -> new LimitOrderResponse(null));
    }

    @Override
    public Collection<Order> getLimitPricesGreaterEqualThan(Double price) {
        return limitOrderElasticRepository.findAllByLimitPriceGreaterThanEqual(price)
                .stream()
                .map(limitOrder -> new Order(limitOrder.getId(), limitOrder.getLimitPrice(), limitOrder.getVolume()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Order> getLimitPricesLowerEqualThan(Double price) {
        return limitOrderElasticRepository.findAllByLimitPriceLessThanEqual(price)
                .stream()
                .map(limitOrder -> new Order(limitOrder.getId(), limitOrder.getLimitPrice(), limitOrder.getVolume()))
                .collect(Collectors.toList());
    }

    @Override
    public void addAllOrders(List<Order> orders) {
        int block = 0;
        int blockSize = 10000;
        while (block < orders.size()) {
            var newBlock = block + blockSize;
            List<Order> ordersBlock = orders.subList(block, newBlock);
            limitOrderElasticRepository.saveAll(ordersBlock.stream()
                    .map(order -> new LimitOrder(order.orderId(),
                            order.limitPrice(),
                            order.volume()))
                    .collect(Collectors.toList()));
            block = newBlock;
            log.info("Adding Block: " + block + " of "+ orders.size());
        }
    }

    @Override
    public Long size() {
        return limitOrderElasticRepository.count();
    }
}
