package com.andyhsu.springbootmall.controller;

import com.andyhsu.springbootmall.dto.CreateOrderRequest;
import com.andyhsu.springbootmall.dto.OrderQueryParam;
import com.andyhsu.springbootmall.model.Order;
import com.andyhsu.springbootmall.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import util.Page;

import java.util.List;

@RestController
@Valid
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<Order> createOrder(@PathVariable Integer userId,
                                             //訂單裡面一定會有從購物車購買的"buyItem"，不一定只購買一個，所以需要List<buyItem>
                                             @RequestBody @Valid CreateOrderRequest createOrderRequest) {

        Integer orderId = orderService.createOrder(userId, createOrderRequest);

        Order order = orderService.getOrderByOrderId(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(@PathVariable Integer userId,
                                                 //分頁 Pagination
                                                 @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
                                                 @RequestParam(defaultValue = "0") @Min(0) Integer offset) {
        OrderQueryParam orderQueryParam = new OrderQueryParam();
        orderQueryParam.setLimit(limit);
        orderQueryParam.setOffset(offset);
        //返回查詢的訂單列表
        List<Order> orderList = orderService.getOrders(orderQueryParam);
        //查詢訂單總筆數
        Integer total = orderService.countOrder(orderQueryParam);


        //要返回給前端的分頁訊息
        Page<Order> page = new Page<>();
        page.setLimit(orderQueryParam.getLimit());
        page.setOffset(orderQueryParam.getOffset());
        page.setTotal(total);
        page.setResults(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

}
