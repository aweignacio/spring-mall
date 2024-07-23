package com.andyhsu.springbootmall.service.impl;

import com.andyhsu.springbootmall.dao.OrderDao;
import com.andyhsu.springbootmall.dao.ProductDao;
import com.andyhsu.springbootmall.dto.BuyItem;
import com.andyhsu.springbootmall.dto.CreateOrderRequest;
import com.andyhsu.springbootmall.model.Order;
import com.andyhsu.springbootmall.model.OrderItem;
import com.andyhsu.springbootmall.model.Product;
import com.andyhsu.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public Order getOrderByOrderId(Integer orderId) {
        //先調用getOrderByOrderId，取得訂單總資訊
        Order order = orderDao.getOrderByOrderId(orderId);
        //再調用getOrderItemsByOrderId取得訂單明細
        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);
        //因一整個order訂單會包含orderList，所以要在order中擴充orderList
        order.setOrderItemList(orderItemList);
        return order;

    }

    @Transactional //只要有修改更新多個資料庫"一定一定一定"要使用Transactional註解
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        //Order中有totalAmount的欄位，需要計算總價錢以放入Dao層
        //計算總價錢
        int totalAmount = 0;
        //創建一個orderItemList，原因是要將buyItem購物車購買的的項目，轉成已經完成購買的"訂單紀錄"
        List<OrderItem> orderItemList = new ArrayList<>();
        //掃條碼的概念，把購物車裡面的東西拿出來一個一個掃(遍歷)
        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            //每次掃描可以查到商品ID，藉由商品ID可以查到商品的資料
            Product product = productDao.getProcuctById(buyItem.getProductId());
            //計算當次遍歷的buyItem的花費，並加總
            int amount = product.getPrice() * buyItem.getQuantity();
            totalAmount += amount;

            //將buyItem 轉成 orderItem (將購物車中的項目正式轉變為訂單中的項目)
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }
        //創建訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount);
        //創建訂單明細
        orderDao.createOrderItems(orderId, orderItemList);
        return orderId;
    }
}
