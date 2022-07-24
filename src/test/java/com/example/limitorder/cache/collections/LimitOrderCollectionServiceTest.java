package com.example.limitorder.cache.collections;

import com.example.limitorder.cache.CommonOrderTest;
import com.example.limitorder.cache.LimitOrderServiceRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LimitOrderCollectionServiceTest {

    @Qualifier(value = "limitOrderCollectionService")
    @Autowired
    private LimitOrderServiceRepo limitOrderRepository;

    @Test
    void testLoad() {
        CommonOrderTest.createTest(limitOrderRepository);
        CommonOrderTest.executeTests(limitOrderRepository);
    }

}