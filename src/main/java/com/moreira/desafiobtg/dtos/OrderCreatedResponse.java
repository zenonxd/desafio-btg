package com.moreira.desafiobtg.dtos;

import com.moreira.desafiobtg.entities.OrderItem;

import java.util.List;

public record OrderCreatedResponse(Long codigoPedido,
                                   Long codigoCliente,
                                   List<OrderItemDto> itens) {
}
