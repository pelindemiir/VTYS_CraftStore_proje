package com.craftstore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/*
 * =====================================================
 * ORDER CREATE DAO
 * - Sepetten sipariş oluşturur (transaction)
 * - order_item insert sonrası trigger stok düşürür
 *
 * NOT:
 * - "tek vendor varsayımı" vardır (cart[0].vendorId)
 * =====================================================
 */
public class OrderCreateDAO {

    public void createOrder(int customerId, List<CartItem> cart) throws Exception {

        // Sepet boş kontrolü
        if (cart == null || cart.isEmpty()) {
            throw new Exception("Sepet boş!");
        }

        try (Connection conn = DatabaseConnection.connect()) {

            // Transaction başlat
            conn.setAutoCommit(false);

            // VendorId: ilk ürünün satıcısı (tek satıcı varsayımı)
            int vendorId = cart.get(0).getProduct().getVendorId();

            // Toplam tutarı hesapla
            double totalAmount = 0;
            for (CartItem item : cart) {
                totalAmount += item.getTotal();
            }

            // =============================
            // 1) customer_order insert
            // =============================
            int orderId;

            try (PreparedStatement psOrder = conn.prepareStatement(
                    """
                    INSERT INTO customer_order
                    (customer_id, vendor_id, order_date, total_amount, status)
                    VALUES (?, ?, NOW(), ?, 'PENDING')
                    RETURNING order_id
                    """
            )) {

                psOrder.setInt(1, customerId);
                psOrder.setInt(2, vendorId);
                psOrder.setDouble(3, totalAmount);

                ResultSet rs = psOrder.executeQuery();
                rs.next();
                orderId = rs.getInt("order_id");
            }

            // =============================
            // 2) order_item insert (batch)
            // =============================
            try (PreparedStatement psItem = conn.prepareStatement(
                    """
                    INSERT INTO order_item
                    (order_id, product_id, quantity, unit_price, line_total)
                    VALUES (?, ?, ?, ?, ?)
                    """
            )) {

                for (CartItem item : cart) {

                    psItem.setInt(1, orderId);
                    psItem.setInt(2, item.getProduct().getProductId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setDouble(4, item.getProduct().getPrice());
                    psItem.setDouble(5, item.getTotal());

                    psItem.addBatch();
                }

                // Batch çalıştır
                psItem.executeBatch();
            }

            // Trigger (trg_process_order_item) burada stok düşürür
            conn.commit();
        }
    }
}
