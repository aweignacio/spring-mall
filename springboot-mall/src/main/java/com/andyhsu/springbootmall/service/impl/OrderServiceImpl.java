package com.andyhsu.springbootmall.service.impl;

import com.andyhsu.springbootmall.dao.OrderDao;
import com.andyhsu.springbootmall.dao.ProductDao;
import com.andyhsu.springbootmall.dao.UserDao;
import com.andyhsu.springbootmall.dto.BuyItem;
import com.andyhsu.springbootmall.dto.CreateOrderRequest;
import com.andyhsu.springbootmall.dto.OrderQueryParam;
import com.andyhsu.springbootmall.model.Order;
import com.andyhsu.springbootmall.model.OrderItem;
import com.andyhsu.springbootmall.model.Product;
import com.andyhsu.springbootmall.model.User;
import com.andyhsu.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;

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

        //創建訂單前，要先檢查前端傳來的user是否存在，或是是否是黑名單。
        User user = userDao.getUserByUserId(userId);
        if (user == null) {
            log.warn("該帳號不存在");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //Order中有totalAmount的欄位，需要計算總價錢以放入Dao層
        //計算總價錢
        int totalAmount = 0;
        //創建一個orderItemList，原因是要將buyItem購物車購買的的項目，轉成已經完成購買的"訂單紀錄"
        List<OrderItem> orderItemList = new ArrayList<>();
        //掃條碼的概念，把購物車裡面的東西拿出來一個一個掃(遍歷)
        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            //每次掃描可以查到商品ID，藉由商品ID可以查到商品的資料
            Product product = productDao.getProcuctById(buyItem.getProductId());
            //檢查Product是否存在，以及是否有足夠的庫存
            if (product == null) {
                log.warn("該{}商品不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            if (product.getStock() < buyItem.getQuantity()) {
                log.warn("商品 {} 庫存不足，無法購買。剩餘庫存 {} ",
                        buyItem.getProductId(), product.getStock());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            //通過以上確認，該商品購買成功。需扣除庫存商品。
            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());


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

    @Override
    public Integer countOrder(OrderQueryParam orderQueryParam) {
        return orderDao.countOrder(orderQueryParam);
    }

    @Override
    public List<Order> getOrders(OrderQueryParam orderQueryParam) {
        //查詢所有訂單紀錄
        List<Order> orderList = orderDao.getOrders(orderQueryParam);

        //要查詢每一筆訂單紀錄的"訂單明細"
        for (Order order : orderList) {
            //調用getOrderItemsByOrderId取得每一筆訂單的訂單明細
            List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrderId());
            //再將獲得的訂單明細set到該筆訂單中，就有完整的訂單json object了
            //因一整個order訂單會包含orderList，所以要在order中擴充orderList
            order.setOrderItemList(orderItemList);
        }
        return orderList;
    }
}
