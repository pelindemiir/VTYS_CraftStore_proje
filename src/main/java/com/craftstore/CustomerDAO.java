package com.craftstore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * =====================================================
 * CUSTOMER DAO
 * - customer tablosu için CRUD işlemleri
 * =====================================================
 */
public class CustomerDAO {

    // =================================================
    // TÜM MÜŞTERİLERİ GETİR
    // =================================================
    public List<Customer> getAllCustomers() {

        // Müşteri listesi
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect()) {

            // SQL sorgusu
            PreparedStatement ps =
                    conn.prepareStatement("SELECT * FROM customer ORDER BY customer_id");

            ResultSet rs = ps.executeQuery();

            // Sonuçları modele çevir
            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return customers;
    }

    // =================================================
    // YENİ MÜŞTERİ EKLE
    // =================================================
    public void addCustomer(String name, String email, String phone) {

        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO customer (customer_name, email, phone) VALUES (?, ?, ?)"
            );

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);

            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // =================================================
    // MÜŞTERİ GÜNCELLE
    // =================================================
    public void updateCustomer(int id, String name, String email, String phone) {

        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE customer SET customer_name=?, email=?, phone=? WHERE customer_id=?"
            );

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // =================================================
    // MÜŞTERİ SİL
    // =================================================
    public void deleteCustomer(int id) {

        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM customer WHERE customer_id=?"
            );

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
