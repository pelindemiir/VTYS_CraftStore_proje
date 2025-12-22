package com.craftstore;

/*
 * =====================================================
 * SALES REPORT MODEL
 * - ReportDAO sonuçlarını taşır
 * =====================================================
 */
public class SalesReport {

    private String productName;
    private int totalQuantity;
    private double totalRevenue;

    // Bu alan vendor bazlı raporlar için eklenmiş (proje uyumluluğu için bırakıldı)
    private int vendorId;

    // Eski kullanım (vendorId yok)
    public SalesReport(String productName, int totalQuantity, double totalRevenue) {
        this.productName = productName;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
        this.vendorId = 0;
    }

    // Yeni kullanım (vendorId var)
    public SalesReport(String productName, int totalQuantity, double totalRevenue, int vendorId) {
        this.productName = productName;
        this.totalQuantity = totalQuantity;
        this.totalRevenue = totalRevenue;
        this.vendorId = vendorId;
    }

    public String getProductName() { return productName; }
    public int getTotalQuantity() { return totalQuantity; }
    public double getTotalRevenue() { return totalRevenue; }
    public int getVendorId() { return vendorId; }
}
