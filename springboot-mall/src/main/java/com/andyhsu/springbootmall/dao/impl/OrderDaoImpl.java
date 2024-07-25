package com.andyhsu.springbootmall.dao.impl;

import com.andyhsu.springbootmall.dao.OrderDao;
import com.andyhsu.springbootmall.dto.OrderQueryParam;
import com.andyhsu.springbootmall.model.Order;
import com.andyhsu.springbootmall.model.OrderItem;
import com.andyhsu.springbootmall.rowmapper.OrderItemRowMapper;
import com.andyhsu.springbootmall.rowmapper.OrderRowMapper;
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
    public List<Order> getOrders(OrderQueryParam orderQueryParam) {
        Map<String, Object> map = new HashMap<>();
        String sql = "SELECT order_id,user_id,total_amount,created_date,last_modified_date FROM `order` WHERE 1=1";
        //查詢條件
        if (orderQueryParam.getUserId() != null) {
            sql = sql + " AND user_id = :userId";
            map.put("userId", orderQueryParam.getUserId());
        }
        //排序，要讓最新一筆訂單顯上在最上面，且不想讓前端去做修改，所以要在Dao寫死，和getProducts有差別，需注意
        sql = sql + " ORDER BY created_date DESC";
        //分頁
        sql = sql + " LIMIT :limit OFFSET :offset";
        map.put("limit", orderQueryParam.getLimit());
        map.put("offset", orderQueryParam.getOffset());

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        return orderList;

    }

    @Override
    public Integer countOrder(OrderQueryParam orderQueryParam) {
        String sql = "SELECT count(*) FROM `order` WHERE 1=1";
        Map<String, Object> map = new HashMap<>();

        //查詢條件
        //查詢訂單總筆數，orderQueryRequest中有userId，必須先確認該userId是否存在
        if (orderQueryParam.getUserId() != null) {
            sql = sql + " AND user_id = :userId";
            map.put("userId", orderQueryParam.getUserId());
        }
        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        return total;

    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        Map<String, Object> map = new HashMap<>();
        String sql = "SELECT oi.order_item_id,oi.order_id,oi.product_id,oi.amount,oi.quantity,p.product_name,p.image_url\n" +
                "FROM order_item oi\n" +
                "LEFT JOIN product p\n" +
                "ON oi.product_id = p.product_id\n" +
                "WHERE oi.order_id = :orderId";
        map.put("orderId", orderId);
        //只要有join多張table，多出來的欄位也要新增在rowMapper
        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());
        return orderItemList;
    }

    @Override
    public Order getOrderByOrderId(Integer orderId) {
        Map<String, Object> map = new HashMap<>();
        String sql = "SELECT order_id,user_id,total_amount,created_date,last_modified_date FROM `order` WHERE order_id = :orderId";
        map.put("orderId", orderId);
        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());
        return orderList.isEmpty() ? null : orderList.get(0);
    }

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
