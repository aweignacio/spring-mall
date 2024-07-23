package com.andyhsu.springbootmall.rowmapper;


import com.andyhsu.springbootmall.model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemRowMapper implements RowMapper<OrderItem> {
    @Override
    public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setOrderItemId(rs.getInt("order_item_id"));
        orderItem.setAmount(rs.getInt("amount"));
        orderItem.setQuantity(rs.getInt("quantity"));

        //擴充的，因查詢出來的訂單紀錄要包含product中的name、image呈現給使用者看
        //需要在model層下的orderItem擴充這兩個資料
        orderItem.setProductName("product_name");
        orderItem.setImageUrl("image_url");
        return orderItem;
    }
}
