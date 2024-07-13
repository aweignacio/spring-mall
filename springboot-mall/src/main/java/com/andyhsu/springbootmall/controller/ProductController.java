package com.andyhsu.springbootmall.controller;

import com.andyhsu.springbootmall.dto.ProductRequest;
import com.andyhsu.springbootmall.model.Product;
import com.andyhsu.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product procuct = productService.getProcuctById(productId);
        if (procuct != null) {
            return ResponseEntity.status(HttpStatus.OK).body(procuct);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> creatProduct(@RequestBody
                                                @Valid ProductRequest productRequest) {
        //因dao層的createProduct方法會返回一個 新增完商品的ID，所以用integer來接住
        Integer productId = productService.creatProduct(productRequest);
        //再利用這個productId，使用getProductId方法來獲得商品數據
        Product product = productService.getProcuctById(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest) {
       //檢查product是否存在
        Product product = productService.getProcuctById(productId);
        if(product == null){
             return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //修改商品的數據
        productService.updateProduct(productId,productRequest);
        Product updateProduct = productService.getProcuctById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }
}

