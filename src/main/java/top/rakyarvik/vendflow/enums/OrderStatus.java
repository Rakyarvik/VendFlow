package top.rakyarvik.vendflow.enums;

public enum OrderStatus {
    CREATED,     // 已创建，待支付
    CANCELLED,   // 超时未支付，已取消
    PAID,        // 支付成功，待下发出货指令
    DISPENSING,  // 出货指令已下发，等待设备回执
    SUCCESS,     // 出货成功，终态
    FAILED,      // 出货失败，待退款
    REFUNDING,   // 退款中
    REFUNDED     // 退款完成，终态
}
