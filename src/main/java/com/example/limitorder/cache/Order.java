package com.example.limitorder.cache;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;
import lombok.*;

import java.io.IOException;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Order implements Serializable, Portable {

    private long orderId;
    private double limitPrice;
    private double volume;

    public final static int ID = 1;

    public double limitPrice() {
        return limitPrice;
    }

    public double volume() {
        return volume;
    }

    public long orderId() {
        return orderId;
    }

    @Override
    public int getFactoryId() {
        return 1;
    }

    @Override
    public int getClassId() {
        return ID;
    }

    @Override
    public void writePortable(PortableWriter portableWriter) throws IOException {
        portableWriter.writeLong( "i", this.orderId);
        portableWriter.writeDouble( "p", this.limitPrice);
        portableWriter.writeDouble( "v", this.volume);
    }

    @Override
    public void readPortable(PortableReader portableReader) throws IOException {
        this.orderId = portableReader.readLong("i");
        this.limitPrice = portableReader.readDouble("p");
        this.volume = portableReader.readDouble("v");
    }
}
