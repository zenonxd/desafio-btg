package com.moreira.desafiobtg.dtos;

import com.moreira.desafiobtg.entities.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public record OrderItemDto(String produto,
                           Integer quantidade,
                           BigDecimal preco) {
}
