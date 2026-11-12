package top.rakyarvik.vendflow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("payment")
public class Payment {
    private Long id;
    private Long orderId;
    private String paymentNo;
    private String idempotencyKey;
    private String callbackStatus;
    private String callbackBody;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
