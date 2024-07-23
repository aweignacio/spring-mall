package com.andyhsu.springbootmall.service;

import com.andyhsu.springbootmall.dto.CreateOrderRequest;
import com.andyhsu.springbootmall.model.Order;

public interface OrderService {

    Order getOrderByOrderId(Integer orderId);
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
