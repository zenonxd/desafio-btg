package com.moreira.desafiobtg.dtos;

import java.util.List;

public record AllOrdersByClientDTO(Long orderId,
        List<OrderItemDto> orders) {
}
