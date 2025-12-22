package com.craftstore;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*
 * =====================================================
 * ORDER MODEL
 * - Sipariş listeleme ve (istersen) oluşturma amaçlı
 * =====================================================
 */
public class Order {

    // Sipariş id
    private int id;

    // Müşteri adı (JOIN ile geliyor)
    private String customerName;

    // Sipariş tarihi
    private LocalDate orderDate;

    // Sipariş toplamı
    private double total;

    // Oluşturma zamanı (opsiyonel)
    private LocalDateTime createdAt;

    // Listeleme için kullanılan constructor
    public Order(int id, String customerName, LocalDate orderDate, double total) {
        this.id = id;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.total = total;
    }

    // Insert/oluşturma tarafı için opsiyonel constructor (mevcut kodu korumak için)
    public Order(int id, String customerName, String date, double total, LocalDateTime createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.orderDate = LocalDate.parse(date);
        this.total = total;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public LocalDate getOrderDate() { return orderDate; }
    public double getTotal() { return total; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
