package com.craftstore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * =====================================================
 * VENDOR DAO
 * - vendor tablosu CRUD işlemleri
 * =====================================================
 */
public class VendorDAO {

    // Tüm vendorları getir
    public List<Vendor> getAllVendors() {

        List<Vendor> list = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT vendor_id, vendor_name FROM vendor ORDER BY vendor_id"
            );

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Vendor(
                        rs.getInt("vendor_id"),
                        rs.getString("vendor_name")
                ));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    // Vendor ekle
    public boolean addVendor(String name) {

        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO vendor (vendor_name) VALUES (?)"
            );

            ps.setString(1, name);
            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("addVendor ERROR: " + e.getMessage());
            return false;
        }
    }

    // Vendor güncelle
    public boolean updateVendor(int id, String name) {

        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE vendor SET vendor_name=? WHERE vendor_id=?"
            );

            ps.setString(1, name);
            ps.setInt(2, id);

            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("updateVendor ERROR: " + e.getMessage());
            return false;
        }
    }

    // Vendor sil
    public boolean deleteVendor(int id) {

        try (Connection conn = DatabaseConnection.connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM vendor WHERE vendor_id=?"
            );

            ps.setInt(1, id);
            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("deleteVendor ERROR: " + e.getMessage());
            return false;
        }
    }
}
