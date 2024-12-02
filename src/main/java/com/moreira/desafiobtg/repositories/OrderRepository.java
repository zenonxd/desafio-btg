package com.moreira.desafiobtg.repositories;

import com.moreira.desafiobtg.entities.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, Long> {


}
