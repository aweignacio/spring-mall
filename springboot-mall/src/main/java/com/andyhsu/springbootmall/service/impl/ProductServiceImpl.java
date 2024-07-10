package com.andyhsu.springbootmall.service.impl;

import com.andyhsu.springbootmall.dao.ProductDao;
import com.andyhsu.springbootmall.model.Product;
import com.andyhsu.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Override
    public Product getProcuctById(Integer productId) {
        return productDao.getProcuctById(productId);

    }
}
