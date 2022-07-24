package com.example.limitorder.cache.hazelcast;

import com.example.limitorder.cache.Order;
import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableFactory;

public class OrderPortableFactory implements PortableFactory {

    @Override
    public Portable create(int classId ) {
        if ( Order.ID == classId )
            return new Order();
        else
            return null;
    }
}