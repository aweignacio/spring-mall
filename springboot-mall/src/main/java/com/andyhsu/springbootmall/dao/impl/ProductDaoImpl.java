package com.andyhsu.springbootmall.dao.impl;

import com.andyhsu.springbootmall.dao.ProductDao;
import com.andyhsu.springbootmall.model.Product;
import com.andyhsu.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ProductDaoImpl implements ProductDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Product getProcuctById(Integer productId) {
        String sql = "SELECT product_id,product_name, category, image_url, " +
                "price, stock, description, created_date, last_modified_date" +
                " FROM product where product_id = :productId";
        Map<String, Object> map = new HashMap<>();

        //"productId"它的主要目的是用來替代 SQL 查詢中的佔位符 :productId
        map.put("productId", productId);
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        return productList.isEmpty() ? null : productList.get(0);

    }
}
