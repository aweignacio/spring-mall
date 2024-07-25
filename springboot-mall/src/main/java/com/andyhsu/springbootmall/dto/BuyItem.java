package com.andyhsu.springbootmall.dto;

public class BuyItem {
    /**
     *
     在設計訂單功能時，設置一個 BuyItem 類別和 OrderItem 類別的主要目的是為了區分訂單創建過程中的臨時購物車數據和最終保存到數據庫中的訂單數據。以下是這種設計的好處和原因：

     設置 BuyItem 類別的好處：
     清晰的責任分離：BuyItem 類別可以專注於描述用戶在購物車中選擇的商品，而 OrderItem 類別則專注於描述已創建的訂單的具體項目。
     防止數據混亂：在創建訂單的過程中，BuyItem 只需要包含商品的基本信息（如商品 ID 和數量），而 OrderItem 則需要更多信息（如訂單 ID，商品名稱等），將它們分開可以防止數據混亂。
     更好的擴展性：如果將來需要擴展功能，比如在創建訂單時需要進行一些額外的處理（如計算折扣），使用 BuyItem 可以避免對 OrderItem 類別的修改。
     */

    private  Integer productId;
    private  Integer quantity;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
