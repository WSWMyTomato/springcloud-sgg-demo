package com.wsw.order.controller;

import com.wsw.order.bean.Order;
import com.wsw.order.properties.OrderProperties;
import com.wsw.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderProperties orderProperties;

    @GetMapping("/config")
    public String config(){
        return "order.timeout:" + orderProperties.getTimeout()+"; autoConfirm:"+orderProperties.getAutoConfirm();
    }
    @Autowired
    private OrderService orderService;
    //创建订单
    @GetMapping("/create")
    public Order createOrder(@RequestParam("userId") Long userId,
                             @RequestParam("productId") Long productId) {
        Order order = orderService.createOrder(productId, userId);
        return order;
    }

}
