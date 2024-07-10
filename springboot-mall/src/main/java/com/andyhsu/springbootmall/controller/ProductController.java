package com.andyhsu.springbootmall.controller;

import com.andyhsu.springbootmall.model.Product;
import com.andyhsu.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/products/{productId}")
     public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
        Product procuct = productService.getProcuctById(productId);
        if(procuct != null){
            return ResponseEntity.status(HttpStatus.OK).body(procuct);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
