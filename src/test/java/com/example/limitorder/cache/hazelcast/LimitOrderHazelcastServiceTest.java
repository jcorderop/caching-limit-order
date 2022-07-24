package com.example.limitorder.cache.hazelcast;

import com.example.limitorder.cache.CommonOrderTest;
import com.example.limitorder.cache.LimitOrderServiceRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LimitOrderHazelcastServiceTest {

    @Qualifier(value = "limitOrderHazelcastService")
    @Autowired
    private LimitOrderServiceRepo limitOrderRepository;

    @Test
    void testLoad() {
        CommonOrderTest.createTest(limitOrderRepository);
        CommonOrderTest.executeTests(limitOrderRepository);
    }
}