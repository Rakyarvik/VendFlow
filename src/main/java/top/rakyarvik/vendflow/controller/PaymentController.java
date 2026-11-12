package top.rakyarvik.vendflow.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.rakyarvik.vendflow.common.Result;
import top.rakyarvik.vendflow.service.OrderService;

import java.util.Map;

@RestController
@RequestMapping("/api/pay")
public class PaymentController {
    @Resource
    private OrderService orderService;

    @PostMapping("/callback")
    public Result<String> callback(@RequestBody Map<String, String> body) {
        orderService.handlePayCallback(
                body.get("idempotencyKey"),
                body.get("paymentNo"),
                Long.valueOf(body.get("orderId")),
                body.toString()//todo 太粗糙 建议存原始JSON字符串
        );
        return Result.success("ok");
    }
}
