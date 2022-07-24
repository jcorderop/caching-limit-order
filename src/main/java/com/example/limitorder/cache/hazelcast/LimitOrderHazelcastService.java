package com.example.limitorder.cache.hazelcast;

import com.example.limitorder.cache.LimitOrderServiceRepo;
import com.example.limitorder.cache.LimitOrderResponse;
import com.example.limitorder.cache.Order;
import com.hazelcast.config.IndexConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service("limitOrderHazelcastService")
public class LimitOrderHazelcastService implements LimitOrderServiceRepo {


    @Qualifier("hazelcastInstance")
    final private HazelcastInstance hazelcastInstance;
    final private IMap map;

    @Autowired
    public LimitOrderHazelcastService(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        this.map = createMap();
    }


    private IMap<Long,Order> createMap() {
        final IMap map = hazelcastInstance.getMap( "order" );
        map.addIndex(new IndexConfig(IndexType.SORTED, "p"));
        return map;
    }

    private IMap<Long,Order> retrieveMap() {
        return this.map;
    }

    public LimitOrderResponse createLimitOrder (Order order) {
        retrieveMap().put(order.orderId(), order);
        return new LimitOrderResponse(order);
    }



    public LimitOrderResponse getLimitOrder(Long id) {
        return new LimitOrderResponse(retrieveMap().get(id));
    }

    public Collection<Order> getLimitPricesGreaterEqualThan(Double price) {
        Collection<Order> limitPrice = retrieveMap().values(Predicates
                .newPredicateBuilder()
                .getEntryObject()
                .get("p")
                .greaterEqual(price));
        var l = limitPrice.stream().collect(Collectors.toList());
        for (int i = 0; i < limitPrice.size(); i++) {
            retrieveMap().remove(l.get(i).orderId());
        }
        return limitPrice;
    }

    public Collection<Order> getLimitPricesLowerEqualThan(Double price) {
        Collection<Order> limitPrice = retrieveMap().values(Predicates
                .newPredicateBuilder()
                .getEntryObject()
                .get("p")
                .lessEqual(price));
        //limitPrice.stream().forEach(o -> retrieveMap().remove(o.orderId()));
        return limitPrice;
    }

    public void addAllOrders(List<Order> orders) {
        orders.forEach(order -> createLimitOrder(order));
    }

    public Long size() {
        return Long.valueOf(retrieveMap().size());
    }
}
