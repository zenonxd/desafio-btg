package com.moreira.desafiobtg.services;

import com.moreira.desafiobtg.dtos.*;
import com.moreira.desafiobtg.entities.Order;
import com.moreira.desafiobtg.entities.OrderItem;
import com.moreira.desafiobtg.repositories.OrderRepository;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(OrderCreatedResponse event) {
        Order order = new Order();
        order.setOrderId(event.codigoPedido());
        order.setCustomerId(event.codigoCliente());

        order.setTotal(getTotal(event));


        order.setOrderItems(getList(event));

        orderRepository.save(order);
    }

    private static List<OrderItem> getList(OrderCreatedResponse event) {
        return event.itens().stream()
                .map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco()))
                .toList();
    }

    private BigDecimal getTotal(OrderCreatedResponse event) {
        return event.itens()
                .stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal::add)
                //caso não tenha nada retorna um zero
                .orElse(BigDecimal.ZERO);
    }


    public Page<OrderResponse> findAllOrdersByClientId(Long id, PageRequest pageRequest) {

        var orders = orderRepository.findAllByCustomerId(id, pageRequest);

        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
        var aggregations = newAggregation(
                //the key we want to compare
                match(Criteria.where("customerId").is(customerId)),
                //creating the query on MongoDB where he'll return the sum for us
                group().sum("total").as("total")

        );

        //Document is always "BSON"
        var response = mongoTemplate.aggregate(aggregations, "tb_order", Document.class);



        //To access this document
        //he is gonna try to access the "total" fied, if we cant, he's going to return zero
        return new BigDecimal(response.getUniqueMappedResult().get("total").toString());

    }
}
