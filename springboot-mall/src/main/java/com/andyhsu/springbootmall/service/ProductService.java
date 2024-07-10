package com.andyhsu.springbootmall.service;

import com.andyhsu.springbootmall.model.Product;

public interface ProductService {
    /**
     * 藉由商品ID查詢商品的功能
     * @param productId
     * @return
     */
    public Product getProcuctById(Integer productId);
}
