package top.rakyarvik.vendflow.task;

import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.rakyarvik.vendflow.entity.Order;
import top.rakyarvik.vendflow.mapper.OrderMapper;
import top.rakyarvik.vendflow.service.OrderService;

import java.util.List;

@Component
public class OrderTimeoutTask {
    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderService orderService;

    @Scheduled(fixedDelay = 60_000)
    public void cancelExpiredOrders() {
        List<Order> expired = orderMapper.selectExpiredCreatedOrders();
        for (Order order : expired) {
            try {
                orderService.cancelIfUnpaid(order);
            } catch (Exception e) {
                // 单条失败不影响其他订单，记录日志即可
                System.err.println("取消订单失败: " + order.getOrderNo() + ", " + e.getMessage());
            }
        }
    }
}
