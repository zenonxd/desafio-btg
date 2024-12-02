package com.moreira.desafiobtg.services;

import com.moreira.desafiobtg.dtos.AllOrdersByClientDTO;
import com.moreira.desafiobtg.dtos.OrderCreatedResponse;
import com.moreira.desafiobtg.dtos.OrderItemDto;
import com.moreira.desafiobtg.entities.Order;
import com.moreira.desafiobtg.entities.OrderItem;
import com.moreira.desafiobtg.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private static OrderItem getOrderItems(OrderItemDto item) {
        return new OrderItem(
                item.produto(),
                item.quantidade(),
                item.preco()
        );
    }

    public void save(OrderCreatedResponse event) {
        Order order = new Order();
        order.setOrderId(event.codigoPedido());
        order.setClientId(event.codigoCliente());

        order.setTotal(getTotal(event));


        order.setOrderItems(
                event.itens()
                        .stream()
                        .map(OrderService::getOrderItems)
                .toList());

        orderRepository.save(order);
    }

    private BigDecimal getTotal(OrderCreatedResponse event) {
        return event.itens()
                .stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal::add)
                //caso não tenha nada retorna um zero
                .orElse(BigDecimal.ZERO);
    }


}
