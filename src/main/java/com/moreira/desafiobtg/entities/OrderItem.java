package com.moreira.desafiobtg.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(value = "tb_order_item")
public class OrderItem {

    private String item;
    private int quantity;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;


}
