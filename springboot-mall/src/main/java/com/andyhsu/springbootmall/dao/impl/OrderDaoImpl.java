package com.andyhsu.springbootmall.dao.impl;

import com.andyhsu.springbootmall.dao.OrderDao;
import com.andyhsu.springbootmall.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {
        Map<String, Object> map = new HashMap<>();
        String sql = "INSERT INTO `order`(user_id, total_amount, created_date, last_modified_date)\n" +
                "VALUES (:userId, :totalAmount, :createdDate, :lastModifiedDate)";
        map.put("userId", userId);
        map.put("totalAmount", totalAmount);
        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        int orderId = keyHolder.getKey().intValue();

        return orderId;
    }

    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {
        //1.一般for loop寫法，將orderItems的sql數據一條一條插入，效率較差
//        for (OrderItem orderItem : orderItemList){
//            String sql = "INSERT INTO order_item(order_id, product_id, quantity, amount) VALUES (:orderId,:productId,:quantity,:amount)";
//            Map<String,Object> map = new HashMap<>();
//            map.put("orderId",orderId);
//            map.put("productId",orderItem.getProductId());
//            map.put("quantity",orderItem.getQuantity());
//            map.put("amount",orderItem.getAmount());
//            namedParameterJdbcTemplate.update(sql,map);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////
        //2.使用batchUpdate，批次更新，效率較高
        String sql = "INSERT INTO order_item(order_id, product_id, quantity, amount) VALUES (:orderId,:productId,:quantity,:amount)";
        // new MapSqlParameterSource[orderItem.size()] >>代表 這個陣列放了?個orderItem的清單
        MapSqlParameterSource[] mapSqlParameterSource = new MapSqlParameterSource[orderItemList.size()];
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            mapSqlParameterSource[i] = new MapSqlParameterSource();
            mapSqlParameterSource[i].addValue("orderId", orderId);
            mapSqlParameterSource[i].addValue("productId", orderItem.getProductId());
            mapSqlParameterSource[i].addValue("quantity", orderItem.getQuantity());
            mapSqlParameterSource[i].addValue("amount", orderItem.getAmount());
        }
        namedParameterJdbcTemplate.batchUpdate(sql, mapSqlParameterSource);
    }
}
