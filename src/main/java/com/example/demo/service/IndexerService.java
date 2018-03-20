package com.example.demo.service;

import com.example.demo.Constants;
import com.example.demo.domain.Car;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class IndexerService {
    private static final String CAR_INDEX_NAME = "car_index";
    private static final String CAR_INDEX_TYPE = "car_type";

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    public long bulkIndex() throws Exception {
        int counter = 0;
        try {
            if (!elasticsearchTemplate.indexExists(CAR_INDEX_NAME)) {
                elasticsearchTemplate.createIndex(CAR_INDEX_NAME);
            }
            Gson gson = new Gson();
            List<IndexQuery> queries = new ArrayList<IndexQuery>();
            List<Car> cars = createTestData();
            for (Car car : cars) {
                IndexQuery indexQuery = new IndexQuery();
                indexQuery.setId(car.getId().toString());
                indexQuery.setSource(gson.toJson(car));
                indexQuery.setIndexName(CAR_INDEX_NAME);
                indexQuery.setType(CAR_INDEX_TYPE);
                queries.add(indexQuery);
                if (counter % Constants.INDEX_COMMIT_SIZE == 0) {
                    elasticsearchTemplate.bulkIndex(queries);
                    queries.clear();
                    System.out.println("bulkIndex counter : " + counter);
                }
                counter++;
            }
            if (queries.size() > 0) {
                elasticsearchTemplate.bulkIndex(queries);
            }
            elasticsearchTemplate.refresh(CAR_INDEX_NAME);
            System.out.println("bulkIndex completed.");
        } catch (Exception e) {
            System.out.println("IndexerService.bulkIndex e;" + e.getMessage());
            throw e;
        }
        return -1;
    }

    private List<Car> createTestData() {
        List<Car> cars = new ArrayList<Car>();
        cars.add(new Car(Long.valueOf(123), "Seat", "Leon", BigDecimal.valueOf(78000)));
        cars.add(new Car(Long.valueOf(124), "Seat", "Rio", BigDecimal.valueOf(62000)));
        cars.add(new Car(Long.valueOf(125), "Kia", "Ceed", BigDecimal.valueOf(80000)));
        cars.add(new Car(Long.valueOf(126), "Mazda", "3", BigDecimal.valueOf(80000)));
        cars.add(new Car(Long.valueOf(127), "Volkswagen", "Polo", BigDecimal.valueOf(72000)));
        cars.add(new Car(Long.valueOf(128), "Volkswagen", "Golf", BigDecimal.valueOf(98000)));
        cars.add(new Car(Long.valueOf(129), "Volkswagen", "Tiguan", BigDecimal.valueOf(120000)));
        cars.add(new Car(Long.valueOf(130), "Opel", "Astra", BigDecimal.valueOf(80000)));
        cars.add(new Car(Long.valueOf(131), "Opel", "Corsa", BigDecimal.valueOf(70000)));
        return cars;
    }
}
