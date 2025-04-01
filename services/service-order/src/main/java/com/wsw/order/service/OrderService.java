package com.wsw.order.service;

import com.wsw.order.bean.Order;

public interface OrderService {
    Order createOrder(Long productId, Long userId);
}
