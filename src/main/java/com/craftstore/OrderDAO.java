package com.craftstore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * =====================================================
 * ORDER DAO
 * - Sipariş listeleme
 * - Sipariş detay kalemleri
 * - Sipariş silme (transaction)
 * =====================================================
 */
public class OrderDAO {

    // =================================================
    // TÜM SİPARİŞLERİ GETİR
    // =================================================
    public List<Order> getAllOrders() {

        List<Order> orders = new ArrayList<>();

        // Sipariş toplamını order_item üzerinden hesaplar
        String sql = """
            SELECT o.order_id,
                   c.customer_name,
                   o.order_date,
                   SUM(oi.quantity * oi.unit_price) AS total
            FROM customer_order o
            JOIN customer c ON o.customer_id = c.customer_id
            JOIN order_item oi ON o.order_id = oi.order_id
            GROUP BY o.order_id, c.customer_name, o.order_date
            ORDER BY o.order_id DESC
        """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("order_id"),
                        rs.getString("customer_name"),
                        rs.getDate("order_date").toLocalDate(),
                        rs.getDouble("total")
                ));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return orders;
    }

    // =================================================
    // SİPARİŞ KALEMLERİ
    // =================================================
    public List<OrderItem> getOrderItems(int orderId) {

        List<OrderItem> items = new ArrayList<>();

        String sql = """
            SELECT p.product_name,
                   oi.quantity,
                   oi.unit_price
            FROM order_item oi
            JOIN product p ON oi.product_id = p.product_id
            WHERE oi.order_id = ?
        """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                items.add(new OrderItem(
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price")
                ));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return items;
    }

    // =================================================
    // SİPARİŞ SİL (order_item -> customer_order)
    // =================================================
    public void deleteOrder(int orderId) {

        String deleteItems = "DELETE FROM order_item WHERE order_id=?";
        String deleteOrder = "DELETE FROM customer_order WHERE order_id=?";

        try (Connection conn = DatabaseConnection.connect()) {

            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(deleteItems);
                 PreparedStatement ps2 = conn.prepareStatement(deleteOrder)) {

                ps1.setInt(1, orderId);
                ps1.executeUpdate();

                ps2.setInt(1, orderId);
                ps2.executeUpdate();

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
