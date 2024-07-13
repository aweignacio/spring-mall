package com.andyhsu.springbootmall.dao.impl;

import com.andyhsu.springbootmall.dao.ProductDao;
import com.andyhsu.springbootmall.dto.ProductRequest;
import com.andyhsu.springbootmall.model.Product;
import com.andyhsu.springbootmall.rowmapper.ProductRowMapper;
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

    @Override
    public Integer creatProduct(ProductRequest productRequest) {
        Map<String,Object> map = new HashMap<>();
        String sql ="INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date) VALUES\n" +
                "                    (:productName,:category,:imageUrl,:price,:stock,:description,:createdDate,:lastModifiedDate)\n";

        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().name());//注意:enum要將取得的類型轉為字符串
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());
        Date now = new Date();
        map.put("createdDate",now);
        map.put("lastModifiedDate",now);
        //keyholder可以獲取自動生成的流水編號ID
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map),keyHolder);
        int productId = keyHolder.getKey().intValue();
        return productId;

    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        Map<String,Object> map = new HashMap<>();
        String sql = "update product set product_name = :productName,category = :category,image_url = :imageUrl," +
                " price = :price, stock = :stock , description = :description, last_modified_date = :lastModifiedDate where product_id = :productId";

        map.put("productId",productId);
        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().name()); //再次提醒需要將enum類用name方法轉為字符串
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());
        Date now = new Date();
        map.put("lastModifiedDate",now);
        namedParameterJdbcTemplate.update(sql,map);
    }
}
