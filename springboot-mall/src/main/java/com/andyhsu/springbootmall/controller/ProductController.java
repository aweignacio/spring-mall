package com.andyhsu.springbootmall.controller;

import com.andyhsu.springbootmall.constant.ProductCategory;
import com.andyhsu.springbootmall.dto.ProductQueryParam;
import com.andyhsu.springbootmall.dto.ProductRequest;
import com.andyhsu.springbootmall.model.Product;
import com.andyhsu.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import util.Page;

import java.util.List;

@RestController
@Valid
public class ProductController {
    @Autowired
    private ProductService productService;

//    @GetMapping("/products")
    //此為分頁功能(無查詢總筆數)
//    public ResponseEntity<List<Product>> getProducts(
//            //查詢條件 Filtering
//            @RequestParam(required = false) ProductCategory category,
//            @RequestParam(required = false) String search,
//            //排序 Sorting
//            @RequestParam(defaultValue = "created_date") String orderBy,
//            @RequestParam(defaultValue = "desc") String sort,
//            //分頁 Pagination
//            //limit、offset對應到的就是sql中的limit、offset用法
//            //limit  > 最多只取得幾筆數據
//            //offset > 從查詢到的數據中，跳過幾筆
//            //使用@max、@min限制可以取得的最大值和最小值，避免資料庫效能不足
//            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
//            @RequestParam(defaultValue = "0") @Min(0) Integer offset
//    ) {
//        ProductQueryParam productQueryParam = new ProductQueryParam();
//        productQueryParam.setCategory(category);
//        productQueryParam.setSearch(search);
//        productQueryParam.setOrderBy(orderBy);
//        productQueryParam.setSort(sort);
//        productQueryParam.setLimit(limit);
//        productQueryParam.setOffset(offset);
//        //查詢商品回傳的是List。
//        List<Product> productList = productService.getProducts(productQueryParam);
//        //當前端請求/products就會消耗資源，不管getProducts是否查詢到/products這個資源都是存在的。
//        //反之，查詢特定ID商品，/products/{productId}，可能會因為是null而沒有這個資源
//        return ResponseEntity.status(HttpStatus.OK).body(productList);
//    }

     @GetMapping("/products")
     //此為分頁+總筆數的用法
     //回傳的是Page類的<Product>
    public ResponseEntity<Page<Product>> getProducts(
            //查詢條件 Filtering
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,
            //排序 Sorting
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,
            //分頁 Pagination
            //limit、offset對應到的就是sql中的limit、offset用法
            //limit  > 最多只取得幾筆數據
            //offset > 從查詢到的數據中，跳過幾筆
            //使用@max、@min限制可以取得的最大值和最小值，避免資料庫效能不足
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ) {
        ProductQueryParam productQueryParam = new ProductQueryParam();
        productQueryParam.setCategory(category);
        productQueryParam.setSearch(search);
        productQueryParam.setOrderBy(orderBy);
        productQueryParam.setSort(sort);
        productQueryParam.setLimit(limit);
        productQueryParam.setOffset(offset);
        //查詢商品回傳的是List。
        List<Product> productList = productService.getProducts(productQueryParam);
        //取得Product總數
        Integer total =productService.countProduct(productQueryParam);
        //分頁
        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList);
        //當前端請求/products就會消耗資源，不管getProducts是否查詢到/products這個資源都是存在的。
        //反之，查詢特定ID商品，/products/{productId}，可能會因為是null而沒有這個資源
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }


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
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //修改商品的數據
        productService.updateProduct(productId, productRequest);
        Product updateProduct = productService.getProcuctById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProductById(productId);

        //商品確定消失不見回傳204，NO_CONTENT給前端
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}

