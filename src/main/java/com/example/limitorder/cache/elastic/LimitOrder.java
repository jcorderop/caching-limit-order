package com.example.limitorder.cache.elastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@Document(indexName = "orderindex")
public class LimitOrder {
    @Id
    private Long id;

    @Field(type = FieldType.Double, name = "limitPrice")
    private Double limitPrice;

    @Field(type = FieldType.Double, name = "volume")
    private Double volume;
}
