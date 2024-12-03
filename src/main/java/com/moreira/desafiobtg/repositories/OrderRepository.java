package com.moreira.desafiobtg.repositories;

import com.moreira.desafiobtg.dtos.ApiResponse;
import com.moreira.desafiobtg.dtos.OrderResponse;
import com.moreira.desafiobtg.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, Long> {


    Page<Order> findAllByCustomerId(Long id, PageRequest pageRequest);
}
