package com.ox5un5h1n3.zulo.data.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String productKey;
    private String productOwnerUid;
    private String productName;
    private String productPrice;
    private String productDescription;
    private double productLat;
    private double productLng;
    private Boolean isProductReserve;
    private Boolean isRequestApproved;
    private String productImage;
    private String customerName;
    private String customerPhoneNo;
    private String ownerName;
    private Boolean isProductDisplay;

    public Product() {}

    public Product(String productKey, String productOwnerUid, String productName, String productPrice, String productDescription, double productLat, double productLng, Boolean isProductReserve, Boolean isRequestApproved, String productImage, String customerName, String customerPhoneNo, String ownerName, Boolean isProductDisplay) {
        this.productKey = productKey;
        this.productOwnerUid = productOwnerUid;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productLat = productLat;
        this.productLng = productLng;
        this.isProductReserve = isProductReserve;
        this.isRequestApproved = isRequestApproved;
        this.productImage = productImage;
        this.customerName = customerName;
        this.customerPhoneNo = customerPhoneNo;
        this.ownerName = ownerName;
        this.isProductDisplay = isProductDisplay;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getProductLat() {
        return productLat;
    }

    public void setProductLat(double productLat) {
        this.productLat = productLat;
    }

    public double getProductLng() {
        return productLng;
    }

    public void setProductLng(double productLng) {
        this.productLng = productLng;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductOwnerUid() {
        return productOwnerUid;
    }

    public void setProductOwnerUid(String productOwnerUid) {
        this.productOwnerUid = productOwnerUid;
    }

    public Boolean getProductReserve() {
        return isProductReserve;
    }

    public void setProductReserve(Boolean productReserve) {
        isProductReserve = productReserve;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNo() {
        return customerPhoneNo;
    }

    public void setCustomerPhoneNo(String customerPhoneNo) {
        this.customerPhoneNo = customerPhoneNo;
    }

    public Boolean getRequestApproved() {
        return isRequestApproved;
    }

    public void setRequestApproved(Boolean requestApproved) {
        isRequestApproved = requestApproved;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Boolean getProductDisplay() {
        return isProductDisplay;
    }

    public void setProductDisplay(Boolean productDisplay) {
        isProductDisplay = productDisplay;
    }
}
