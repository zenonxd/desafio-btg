package com.moreira.desafiobtg.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.juli.logging.Log;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(value = "tb_order")
public class Order {

    @MongoId
    private Long orderId;

    @Indexed(name = "customer_id_index")
    private Long clientId;

    private List<OrderItem> orderItems;

    private BigDecimal total;


}
