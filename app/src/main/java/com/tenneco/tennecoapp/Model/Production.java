package com.tenneco.tennecoapp.Model;

public class Production {
    private String productId;
    private String productName;
    private int total;

    public Production() {
    }

    public Production(String productId, String productName, int total) {
        this.productId = productId;
        this.productName = productName;
        this.total = total;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
