package com.craftstore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * =====================================================
 * PRODUCT DAO
 * - product tablosu için CRUD + arama + vendor join
 * - Soft delete işlemi: sp_delete_product
 * =====================================================
 */
public class ProductDAO {

    // =================================================
    // TÜM AKTİF ÜRÜNLER (vendorName boş gelir)
    // =================================================
    public List<Product> getAllProducts() {

        // Liste oluştur
        List<Product> list = new ArrayList<>();

        // SQL: sadece aktif ürünler
        String sql = """
            SELECT product_id, vendor_id, category_id, product_name,
                   description, price, stock_quantity
            FROM product
            WHERE is_active = true
            ORDER BY product_id DESC
        """;

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Satır satır Product üret
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("product_id"),
                        rs.getInt("vendor_id"),
                        rs.getInt("category_id"),
                        rs.getString("product_name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity"),
                        "" // vendorName burada yok
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException("Ürünler alınamadı: " + e.getMessage());
        }

        return list;
    }

    // =================================================
    // ÜRÜN ARAMA (isim içinde arar)
    // =================================================
    public List<Product> searchProducts(String keyword) {

        List<Product> list = new ArrayList<>();

        String sql = """
            SELECT product_id, vendor_id, category_id, product_name,
                   description, price, stock_quantity
            FROM product
            WHERE is_active = true
              AND LOWER(product_name) LIKE ?
            ORDER BY product_id DESC
        """;

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // LIKE parametresi
            ps.setString(1, "%" + keyword.toLowerCase() + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Product(
                            rs.getInt("product_id"),
                            rs.getInt("vendor_id"),
                            rs.getInt("category_id"),
                            rs.getString("product_name"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getInt("stock_quantity"),
                            ""
                    ));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Arama başarısız: " + e.getMessage());
        }

        return list;
    }

    // =================================================
    // SATICIYA GÖRE ÜRÜNLER
    // =================================================
    public List<Product> getProductsByVendor(int vendorId) {

        List<Product> list = new ArrayList<>();

        String sql = """
            SELECT product_id, vendor_id, category_id, product_name,
                   description, price, stock_quantity
            FROM product
            WHERE vendor_id = ? AND is_active = true
            ORDER BY product_id DESC
        """;

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            // vendor id parametresi
            ps.setInt(1, vendorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Product(
                            rs.getInt("product_id"),
                            rs.getInt("vendor_id"),
                            rs.getInt("category_id"),
                            rs.getString("product_name"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getInt("stock_quantity"),
                            ""
                    ));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Vendor ürünleri alınamadı: " + e.getMessage());
        }

        return list;
    }

    // =================================================
    // TÜM ÜRÜNLER + VENDOR ADI (JOIN)
    // =================================================
    public List<Product> getAllProductsWithVendor() {

        List<Product> list = new ArrayList<>();

        String sql = """
            SELECT p.product_id, p.vendor_id, p.category_id, p.product_name,
                   p.description, p.price, p.stock_quantity, v.vendor_name
            FROM product p
            JOIN vendor v ON p.vendor_id = v.vendor_id
            WHERE p.is_active = true
            ORDER BY p.product_id DESC
        """;

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("product_id"),
                        rs.getInt("vendor_id"),
                        rs.getInt("category_id"),
                        rs.getString("product_name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity"),
                        rs.getString("vendor_name")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException("Ürünler alınamadı: " + e.getMessage());
        }

        return list;
    }

    // =================================================
    // ÜRÜN EKLE
    // =================================================
    public void addProduct(int vendorId, int categoryId, String name,
                           String desc, double price, int stock) {

        String sql = """
            INSERT INTO product
            (vendor_id, category_id, product_name, description,
             price, stock_quantity, is_active)
            VALUES (?, ?, ?, ?, ?, ?, true)
        """;

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, vendorId);
            ps.setInt(2, categoryId);
            ps.setString(3, name);
            ps.setString(4, desc);
            ps.setDouble(5, price);
            ps.setInt(6, stock);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Ürün eklenemedi: " + e.getMessage());
        }
    }

    // =================================================
    // ÜRÜN GÜNCELLE
    // =================================================
    public void updateProduct(int id, String name, String desc,
                              double price, int stock, int categoryId) {

        String sql = """
            UPDATE product
            SET product_name = ?, description = ?, price = ?,
                stock_quantity = ?, category_id = ?
            WHERE product_id = ? AND is_active = true
        """;

        try (Connection c = DatabaseConnection.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, desc);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setInt(5, categoryId);
            ps.setInt(6, id);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Ürün güncellenemedi: " + e.getMessage());
        }
    }

    // =================================================
    // SOFT DELETE (PROCEDURE)
    // =================================================
    public void deleteProduct(int productId) {

        // SQL procedure çağrısı
        String sql = "CALL sp_delete_product(?)";

        try (Connection c = DatabaseConnection.connect();
             CallableStatement cs = c.prepareCall(sql)) {

            cs.setInt(1, productId);
            cs.execute();

        } catch (Exception e) {
            throw new RuntimeException("Ürün silinemedi: " + e.getMessage());
        }
    }
}
