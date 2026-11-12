package top.rakyarvik.vendflow.service;

import jakarta.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.rakyarvik.vendflow.entity.Order;
import top.rakyarvik.vendflow.entity.Payment;
import top.rakyarvik.vendflow.enums.OrderStatus;
import top.rakyarvik.vendflow.mapper.OrderMapper;
import top.rakyarvik.vendflow.mapper.PaymentMapper;
import top.rakyarvik.vendflow.statemachine.OrderStateMachine;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private PaymentMapper paymentMapper;

    // 创建订单：初始状态 CREATED，无需校验流转
    public Order createOrder(Long deviceId, String slotNo, Long productId, BigDecimal amount) {
        Order order = new Order();
        //Todo 之后用雪花算法或日期+序列号
        order.setOrderNo("ORD" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        order.setDeviceId(deviceId);
        order.setSlotNo(slotNo);
        order.setProductId(productId);
        order.setAmount(amount);
        order.setStatus(OrderStatus.CREATED.name());
        order.setExpireAt(LocalDateTime.now().plusMinutes(15));
        order.setVersion(0);// 设置 expire_at 等字段...
        orderMapper.insert(order);
        return order;
    }

    // 支付回调：从 CREATED 流转到 PAID
    public void onPaySuccess(Long orderId) {
        Order order = getOrder(orderId);  // 查库

        OrderStatus from = OrderStatus.valueOf(order.getStatus());
        OrderStatus to = OrderStatus.PAID;

        // 先校验，不合法直接抛异常，不会往下走
        OrderStateMachine.checkTransition(from, to);

        order.setStatus(to.name());
        // orderMapper.updateById(order);
    }

    // 超时取消：从 CREATED 流转到 CANCELLED
    public void cancelIfUnpaid(Order order) {
        OrderStatus from = OrderStatus.valueOf(order.getStatus());
        OrderStatus to = OrderStatus.CANCELLED;

        if (!OrderStateMachine.canTransition(from, to)) {
            // 已经不是 CREATED 了，说明已支付或已取消，直接返回
            return;
        }

        order.setStatus(to.name());
        orderMapper.updateById(order);
    }

    private Order getOrder(Long orderId) {
        // 从数据库查询，这里省略
        return orderMapper.selectById(orderId);
    }

    public void handlePayCallback(String idempotencyKey, String paymentNo, Long orderId, String callbackBody) {
        // 第一道：幂等键唯一约束，重复回调插入会抛异常
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setPaymentNo(paymentNo);
        payment.setIdempotencyKey(idempotencyKey);
        payment.setCallbackStatus("RECEIVED");
        payment.setCallbackBody(callbackBody);
        try {
            paymentMapper.insert(payment);
        } catch (DuplicateKeyException e) {
            // 已经处理过，直接返回，不重复改订单状态
            return;
        }
        // 第二道：状态机校验，只有 CREATED 才能到 PAID
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在: " + orderId);
        }
        OrderStatus from = OrderStatus.valueOf(order.getStatus());
        if (!OrderStateMachine.canTransition(from, OrderStatus.PAID)) {
            // 订单已支付或已取消，回调迟到，标记 IGNORED 即可
            payment.setCallbackStatus("IGNORED");
            paymentMapper.updateById(payment);
            return;
        }
        order.setStatus(OrderStatus.PAID.name());
        order.setPaidAt(LocalDateTime.now());
        orderMapper.updateById(order);

        payment.setCallbackStatus("PROCESSED");
        paymentMapper.updateById(payment);
    }
}
