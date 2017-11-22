package com.ecommerceapp.data_model;


import org.json.JSONArray;
import org.json.JSONObject;

public class ProductList {

    private String productTitle;
    private int productId, categoryId;
    private JSONArray variants;
    private int viewCount, orderCount, shareCount;

    public ProductList(String productTitle, int productId, int categoryId, JSONArray variants, int viewCount,
                       int orderCount, int shareCount) {
        this.productTitle = productTitle;
        this.productId = productId;
        this.categoryId = categoryId;
        this.variants = variants;
        this.viewCount = viewCount;
        this.orderCount = orderCount;
        this.shareCount = shareCount;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public JSONArray getVariants() {
        return variants;
    }

    public int getProductId() {
        return productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getViewCount() {
        return viewCount;
    }

    public int getShareCount() {
        return shareCount;
    }

    public int getOrderCount() {
        return orderCount;
    }
}
