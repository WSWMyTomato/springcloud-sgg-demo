package com.wsw.order.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.nacos.shaded.io.grpc.LoadBalancer;
import com.wsw.order.bean.Order;
import com.wsw.order.service.OrderService;
import com.wsw.product.bean.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @SentinelResource(value = "createOrder")
    @Override
    public Order createOrder(Long productId, Long userId) {
        Order order = new Order();
//        Product product = getProductFromRemote(productId);
        Product product = getProductFromRemoteWithLoadBalancer(productId);

        order.setId(1L);
        order.setUserId(userId);
        order.setProductList(Arrays.asList(product));
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getStock())));
        order.setAddress("杭州");
        order.setNickName("wsw");

        return order;
    }

    //远程调用
    private Product getProductFromRemote(Long productId) {
        //获取商品服务所在的所有机器IP+port
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
        ServiceInstance instance = instances.get(0);

        String host = instance.getHost();
        int port = instance.getPort();
        String url = String.format("http://%s:%s/product/%s", host, port, productId);
        log.info("远程请求：{}", url);

        Product product = restTemplate.getForObject(url, Product.class);

        return product;
    }

    //远程调用(负载均衡)
    private Product getProductFromRemoteWithLoadBalancer(Long productId) {
        //获取商品服务所在的所有机器IP+port
        ServiceInstance instance = loadBalancerClient.choose("service-product");

        String host = instance.getHost();
        int port = instance.getPort();
        String url = String.format("http://%s:%s/product/%s", host, port, productId);
        log.info("远程请求：{}", url);

        Product product = restTemplate.getForObject(url, Product.class);

        return product;
    }
}
