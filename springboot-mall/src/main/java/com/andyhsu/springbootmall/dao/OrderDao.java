package com.andyhsu.springbootmall.dao;

import com.andyhsu.springbootmall.dto.OrderQueryParam;
import com.andyhsu.springbootmall.model.Order;
import com.andyhsu.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    List<Order> getOrders(OrderQueryParam orderQueryParam);
    Integer countOrder(OrderQueryParam orderQueryParam);
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);

    Order getOrderByOrderId(Integer orderId);

    Integer createOrder(Integer userId, Integer totalAmount);

    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
}
