package com.moreira.desafiobtg.controllers;

import com.moreira.desafiobtg.dtos.*;
import com.moreira.desafiobtg.services.OrderService;
import com.rabbitmq.client.Return;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/customer/{customerId}")
    public ResponseEntity<ApiResponse<OrderResponse>> findAllOrdersByClientId(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                                            @PathVariable Long customerId) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);


        var pageResponse = orderService.findAllOrdersByClientId(customerId, pageRequest);
        var totalOnOrders = orderService.findTotalOnOrdersByCustomerId(customerId);
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalOnOrders", totalOnOrders);

        return ResponseEntity.ok(new ApiResponse<>(
                summary,
                pageResponse.getContent(),
                PaginationDTO.fromPage(pageResponse)
        ));
    }

}
