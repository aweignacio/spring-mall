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
    /**
     * 修改商品數據的功能
     * @param productId 要修改商品的ID
     * @param productRequest 前端請求中要修改商品的數據
     */
    void updateProduct(Integer productId,ProductRequest productRequest);
    /**
     * 刪除商品的功能
     * @param producetId  要刪除的商品ID
     */
    void deleteProductById(Integer producetId);
}
