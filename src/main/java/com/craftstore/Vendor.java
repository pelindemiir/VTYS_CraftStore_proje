package com.craftstore;

/*
 * =====================================================
 * VENDOR MODEL
 * - vendor tablosu kaydı
 * - JComboBox içinde düzgün görünmesi için toString override
 * =====================================================
 */
public class Vendor {

    private int id;
    private String name;

    public Vendor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        // ComboBox içinde görünen text
        return name;
    }
}
