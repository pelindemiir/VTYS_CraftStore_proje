package com.craftstore;

/*
 * =====================================================
 * ORDER ITEM MODEL (listeleme için)
 * - Sipariş detayında gösterilen satırları temsil eder
 * =====================================================
 */
public class OrderItem {

    // Ürün adı
    private String productName;

    // Adet
    private int quantity;

    // Birim fiyat
    private double unitPrice;

    public OrderItem(String productName, int quantity, double unitPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }

    // Satır toplam = unitPrice * quantity
    public double getLineTotal() { return unitPrice * quantity; }
}
