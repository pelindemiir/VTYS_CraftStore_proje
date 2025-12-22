package com.craftstore;

/*
 * =====================================================
 * PRODUCT MODEL
 * - product tablosundaki bir kaydı temsil eder
 * =====================================================
 */
public class Product {

    // Alanlar
    private int productId;
    private int vendorId;
    private int categoryId;
    private String productName;
    private String description;
    private double price;
    private int stockQuantity;
    private String vendorName; // JOIN ile gelen satıcı adı

    // Constructor
    public Product(int productId, int vendorId, int categoryId,
                   String productName, String description,
                   double price, int stockQuantity, String vendorName) {

        this.productId = productId;
        this.vendorId = vendorId;
        this.categoryId = categoryId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.vendorName = vendorName;
    }

    // Getter metodları
    public int getProductId() { return productId; }
    public int getVendorId() { return vendorId; }
    public int getCategoryId() { return categoryId; }
    public String getProductName() { return productName; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public String getVendorName() { return vendorName; }
}
