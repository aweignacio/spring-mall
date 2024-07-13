package com.andyhsu.springbootmall.dao;

import com.andyhsu.springbootmall.dto.ProductRequest;
import com.andyhsu.springbootmall.model.Product;


public interface ProductDao {
    /**
     * 藉由商品ID查詢商品的功能
     * @param productId 要查詢的商品ID
     * @return 返回所查詢的商品數據
     */
    Product getProcuctById(Integer productId);
    /**
     *新增商品的功能
     * @param productRequest 要新增的商品資歷
     * @return 返回一個productId，代表新增的商品Id
     */
    Integer creatProduct(ProductRequest productRequest);
}
