package com.craftstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VendorOrderPanel extends JFrame {

    private final int vendorId;
    private JTable table;
    private DefaultTableModel model;
    private ReportDAO reportDAO = new ReportDAO();

    // 🎨 MOR TEMA
    private static final Color HEADER_COLOR = new Color(128, 104, 176);
    private static final Color BG_COLOR     = new Color(245, 246, 250);

    public VendorOrderPanel(int vendorId) {
        this.vendorId = vendorId;

        setTitle("Siparişlerim | Satıcı ID: " + vendorId);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setPreferredSize(new Dimension(900, 70));

        JButton btnBack = new JButton("← Geri");
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> dispose());
        header.add(btnBack, BorderLayout.WEST);

        JLabel title = new JLabel(
                "Siparişlerim / Satışlar (ID: " + vendorId + ")",
                SwingConstants.CENTER
        );
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // ================= TABLE =================
        model = new DefaultTableModel(
                new Object[]{"No", "Ürün", "Adet", "Toplam"}, 0
        );
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BG_COLOR);
        add(scrollPane, BorderLayout.CENTER);

        loadOrders();
    }

    private void loadOrders() {
        model.setRowCount(0);

        List<SalesReport> orders = reportDAO.getSalesReportFiltered(
                "2000-01-01",
                "2100-01-01",
                vendorId
        );

        int no = 1;
        for (SalesReport r : orders) {
            model.addRow(new Object[]{
                    no++,
                    r.getProductName(),
                    r.getTotalQuantity(),
                    r.getTotalRevenue()
            });
        }
    }
}
