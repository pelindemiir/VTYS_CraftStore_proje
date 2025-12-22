package com.craftstore;

/*
 * =====================================================
 * CUSTOMER MODEL
 * - customer tablosundaki bir kaydı temsil eder
 * =====================================================
 */
public class Customer {

    // Müşteri ID (PK)
    private int id;

    // Müşteri adı
    private String name;

    // Email bilgisi
    private String email;

    // Telefon bilgisi
    private String phone;

    // Constructor
    public Customer(int id, String name, String email, String phone) {
        this.id = id;           // ID atanır
        this.name = name;       // Ad atanır
        this.email = email;     // Email atanır
        this.phone = phone;     // Telefon atanır
    }

    // Getter metodları
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    // Setter metodları (güncelleme için)
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
}
