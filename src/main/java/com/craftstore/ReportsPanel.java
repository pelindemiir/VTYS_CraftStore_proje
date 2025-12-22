package com.craftstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportsPanel extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private ReportDAO reportDAO = new ReportDAO();
    private VendorDAO vendorDAO = new VendorDAO();

    private static final Color PURPLE_MAIN  = new Color(125, 115, 195);
    private static final Color PURPLE_LIGHT = new Color(175, 170, 225);
    private static final Color BG_COLOR     = new Color(245, 246, 250);

    public ReportsPanel() {

        setTitle("Satış Raporları");
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PURPLE_MAIN);
        header.setPreferredSize(new Dimension(1000, 80));

        JButton btnBack = purpleButton("← Geri");
        btnBack.addActionListener(e -> dispose());
        header.add(btnBack, BorderLayout.WEST);

        JLabel title = new JLabel("SATIŞ RAPOR PANELİ", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // ===== SOL FİLTRE =====
        JPanel filter = new JPanel(new GridLayout(8,1,10,10));
        filter.setBackground(BG_COLOR);
        filter.setBorder(BorderFactory.createEmptyBorder(30,20,30,20));

        JTextField start = new JTextField("2023-01-01");
        JTextField end = new JTextField("2025-12-31");

        JComboBox<Vendor> cmbVendor = new JComboBox<>();
        cmbVendor.addItem(new Vendor(0,"Tüm Satıcılar"));
        for (Vendor v : vendorDAO.getAllVendors()) cmbVendor.addItem(v);

        JButton btnLoad = purpleButton("Raporu Getir");

        filter.add(new JLabel("Başlangıç"));
        filter.add(start);
        filter.add(new JLabel("Bitiş"));
        filter.add(end);
        filter.add(new JLabel("Satıcı"));
        filter.add(cmbVendor);
        filter.add(btnLoad);

        add(filter, BorderLayout.WEST);

        // ===== TABLO =====
        model = new DefaultTableModel(
                new Object[]{"Ürün","Satılan Adet","Toplam Gelir"},0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnLoad.addActionListener(e -> {
            Vendor v = (Vendor) cmbVendor.getSelectedItem();
            loadReport(start.getText(), end.getText(), v.getId());
        });
    }

    private JButton purpleButton(String t) {
        JButton b = new JButton(t);
        b.setBackground(PURPLE_LIGHT);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        return b;
    }

    private void loadReport(String s, String e, int vid) {
        model.setRowCount(0);
        List<SalesReport> list = reportDAO.getSalesReportFiltered(s,e,vid);
        for (SalesReport r : list) {
            model.addRow(new Object[]{
                    r.getProductName(),
                    r.getTotalQuantity(),
                    r.getTotalRevenue()
            });
        }
    }
}
