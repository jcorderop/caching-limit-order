package com.example.limitorder.cache.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface LimitOrderElasticRepository extends ElasticsearchRepository<LimitOrder, Long> {

    List<LimitOrder> findAllByLimitPriceGreaterThanEqual(Double limitPrice);
    List<LimitOrder> findAllByLimitPriceLessThanEqual(Double limitPrice);
}
