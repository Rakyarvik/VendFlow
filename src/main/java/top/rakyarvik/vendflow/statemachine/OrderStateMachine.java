package top.rakyarvik.vendflow.statemachine;

import top.rakyarvik.vendflow.enums.OrderStatus;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class OrderStateMachine {
    // 每个状态允许流转到哪些状态
    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED = new EnumMap<>(OrderStatus.class);
    static {
        ALLOWED.put(OrderStatus.CREATED, EnumSet.of(OrderStatus.PAID, OrderStatus.CANCELLED));
        ALLOWED.put(OrderStatus.PAID, EnumSet.of(OrderStatus.DISPENSING, OrderStatus.FAILED));
        ALLOWED.put(OrderStatus.DISPENSING, EnumSet.of(OrderStatus.SUCCESS, OrderStatus.FAILED));
        ALLOWED.put(OrderStatus.FAILED, EnumSet.of(OrderStatus.REFUNDING));
        ALLOWED.put(OrderStatus.REFUNDING, EnumSet.of(OrderStatus.REFUNDED));
        // CANCELLED、SUCCESS、REFUNDED 是终态，没有后继状态
    }
    /**
     * 判断能否从 from 流转到 to
     */
    public static boolean canTransition(OrderStatus from, OrderStatus to) {
        Set<OrderStatus> targets = ALLOWED.get(from);
        return targets != null && targets.contains(to);
    }

    /**
     * 校验流转，不合法直接抛异常
     */
    public static void checkTransition(OrderStatus from, OrderStatus to) {
        if (!canTransition(from, to)) {
            throw new IllegalStateException(
                    "非法状态流转: " + from + " -> " + to);
        }
    }
}
