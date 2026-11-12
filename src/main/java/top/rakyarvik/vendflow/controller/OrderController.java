package top.rakyarvik.vendflow.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.rakyarvik.vendflow.common.Result;
import top.rakyarvik.vendflow.entity.Order;
import top.rakyarvik.vendflow.service.OrderService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping("/create")
    public Result<Order> create(@RequestParam("deviceId") Long deviceId,
                                @RequestParam("slotNo") String slotNo,
                                @RequestParam("productId") Long productId,
                                @RequestParam("amount") BigDecimal amount) {
        return Result.success(orderService.createOrder(deviceId, slotNo, productId, amount));
    }
}
