package top.rakyarvik.vendflow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private Long deviceId;

    private String slotNo;

    private Long productId;

    private BigDecimal amount;

    /** 订单状态，存 OrderStatus 的 name() */
    private String status;

    /** 支付过期时间，创建订单时写入 */
    private LocalDateTime expireAt;

    private LocalDateTime paidAt;

    private LocalDateTime dispensedAt;

    /** 失败原因：DEVICE_OFFLINE / ACK_TIMEOUT / DEVICE_REJECT 等 */
    private String failReason;

    /** 乐观锁版本号 */
    private Integer version;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
