package com.andyhsu.springbootmall.dao;

import com.andyhsu.springbootmall.model.Order;
import com.andyhsu.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {
    
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    Order getOrderByOrderId(Integer orderId);

    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
}
