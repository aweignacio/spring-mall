package com.andyhsu.springbootmall.dao;

import com.andyhsu.springbootmall.model.Product;


public interface ProductDao {
    /**
     * 藉由商品ID查詢商品的功能
     * @param productId
     * @return
     */
    Product getProcuctById(Integer productId);
}
