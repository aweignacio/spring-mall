package com.andyhsu.springbootmall.dao;

import com.andyhsu.springbootmall.model.Product;
import org.springframework.stereotype.Component;


public interface ProductDao {
    /**
     * 藉由商品ID查詢商品的功能
     * @param productId
     * @return
     */
    public Product getProcuctById(Integer productId);
}
