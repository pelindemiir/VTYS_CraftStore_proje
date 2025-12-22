package com.craftstore;

/*
 * =====================================================
 * CartItem
 * - Sepette tutulan tek bir satırı temsil eder
 * - Product + quantity içerir
 * =====================================================
 */
public class CartItem {

    // Sepetteki ürün
    private final Product product;

    // Sepetteki adet
    private int quantity;

    public CartItem(Product product, int quantity) {
        // Ürün referansı set
        this.product = product;
        // Başlangıç adedi set
        this.quantity = quantity;
    }

    // Ürün nesnesini döndür
    public Product getProduct() {
        return product;
    }

    // Adet döndür
    public int getQuantity() {
        return quantity;
    }

    // Ürün ID döndür (kolaylık)
    public int getProductId() {
        return product.getProductId();
    }

    // Birim fiyat döndür
    public double getUnitPrice() {
        return product.getPrice();
    }

    // Satır toplam tutar = birim fiyat * adet
    public double getTotal() {
        return product.getPrice() * quantity;
    }

    // Var olan adedi artır
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    // Adedi doğrudan set et
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
