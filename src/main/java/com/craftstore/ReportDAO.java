package com.craftstore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * =====================================================
 * REPORT DAO
 * - Tarih aralığına göre satış raporu üretir
 * - vendorId = 0 ise tüm satıcılar
 * - vendorId != 0 ise satıcı filtresi uygulanır
 * =====================================================
 */
public class ReportDAO {

    public List<SalesReport> getSalesReportFiltered(String start, String end, int vendorId) {

        List<SalesReport> list = new ArrayList<>();

        // Tarih boş gelirse default aralık ver
        if (start == null || start.isBlank()) start = "2000-01-01";
        if (end == null || end.isBlank()) end = "2100-01-01";

        // Temel SQL (date aralığı)
        String baseSQL = """
            SELECT 
                p.product_name,
                SUM(oi.quantity)::INT AS total_quantity,
                SUM(oi.line_total)::NUMERIC AS total_revenue
            FROM order_item oi
            JOIN product p ON oi.product_id = p.product_id
            JOIN customer_order o ON oi.order_id = o.order_id
            WHERE o.order_date::date BETWEEN ?::date AND ?::date
            """;

        // vendor filtreli / filtresiz final SQL
        String finalSQL = (vendorId == 0)
                ? baseSQL + " GROUP BY p.product_name ORDER BY total_revenue DESC"
                : baseSQL + " AND p.vendor_id = ? GROUP BY p.product_name ORDER BY total_revenue DESC";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(finalSQL)) {

            // 1-2: tarih parametreleri
            ps.setString(1, start);
            ps.setString(2, end);

            // 3: vendor parametresi gerekiyorsa ver
            if (vendorId != 0) {
                ps.setInt(3, vendorId);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new SalesReport(
                        rs.getString("product_name"),
                        rs.getInt("total_quantity"),
                        rs.getDouble("total_revenue")
                ));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("SQL ERROR: " + ex.getMessage());
        }

        return list;
    }
}
