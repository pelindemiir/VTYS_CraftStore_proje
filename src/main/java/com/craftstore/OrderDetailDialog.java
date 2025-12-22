package com.craftstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/*
 * =====================================================
 * ORDER DETAIL DIALOG
 * - Siparişin ürün kalemlerini gösterir
 * =====================================================
 */
public class OrderDetailDialog extends JDialog {

    public OrderDetailDialog(JFrame parent, int orderId, List<OrderItem> items) {

        super(parent, "Sipariş Detayı #" + orderId, true);

        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Ürün", "Adet", "Birim Fiyat", "Toplam"}, 0
        );

        JTable table = new JTable(model);

        // Verileri tabloya bas
        for (OrderItem item : items) {
            model.addRow(new Object[]{
                    item.getProductName(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getLineTotal()
            });
        }

        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnClose = new JButton("Kapat");
        btnClose.addActionListener(e -> dispose());

        JPanel bottom = new JPanel();
        bottom.add(btnClose);

        add(bottom, BorderLayout.SOUTH);
    }
}
