package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@Document(indexName = "carIndex", type = "carType", shards = 1, replicas = 0)
public class Car {

    @Id
    private Long id;
    private String brand;
    private String model;
    private BigDecimal amount;

}
