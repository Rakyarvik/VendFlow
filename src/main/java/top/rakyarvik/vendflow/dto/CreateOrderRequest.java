package top.rakyarvik.vendflow.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderRequest {
    private Long deviceId;
    private String slotNo;
    private Long productId;
    private BigDecimal amount;
}