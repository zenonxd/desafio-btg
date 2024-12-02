package com.moreira.desafiobtg.controllers;

import com.moreira.desafiobtg.dtos.AllOrdersByClientDTO;
import com.moreira.desafiobtg.dtos.OrderCreatedResponse;
import com.moreira.desafiobtg.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findAllOrdersByClient(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                   @PathVariable Long id) {


    }
}
