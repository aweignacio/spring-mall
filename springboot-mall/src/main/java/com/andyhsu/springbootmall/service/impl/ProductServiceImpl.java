package com.andyhsu.springbootmall.service.impl;

import com.andyhsu.springbootmall.dao.ProductDao;
import com.andyhsu.springbootmall.dto.ProductRequest;
import com.andyhsu.springbootmall.model.Product;
import com.andyhsu.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> getProducts() {
        return productDao.getProducts();
    }

    @Override
    public Product getProcuctById(Integer productId) {
        return productDao.getProcuctById(productId);

    }

    @Override
    public Integer creatProduct(ProductRequest productRequest) {
        return productDao.creatProduct(productRequest);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId, productRequest);
    }

    @Override
    public void deleteProductById(Integer producetId) {
        productDao.deleteProductById(producetId);
    }
}
