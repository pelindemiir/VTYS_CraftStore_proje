package com.craftstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VendorManagementPanel extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private VendorDAO vendorDAO = new VendorDAO();

    private static final Color PURPLE_MAIN  = new Color(125, 115, 195);
    private static final Color PURPLE_LIGHT = new Color(175, 170, 225);
    private static final Color BG_COLOR     = new Color(245, 246, 250);

    public VendorManagementPanel() {

        setTitle("Üretici Yönetimi");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PURPLE_MAIN);
        header.setPreferredSize(new Dimension(900, 80));

        JButton btnBack = purpleButton("← Geri");
        btnBack.addActionListener(e -> dispose());
        header.add(btnBack, BorderLayout.WEST);

        JLabel title = new JLabel("ÜRETİCİ YÖNETİM PANELİ", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // ===== SOL PANEL =====
        JPanel left = new JPanel(new GridLayout(4,1,15,15));
        left.setBackground(BG_COLOR);
        left.setBorder(BorderFactory.createEmptyBorder(30,20,30,20));

        left.add(purpleButton("Yeni Üretici"));
        left.add(purpleButton("Sil"));
        left.add(purpleButton("Güncelle"));
        left.add(purpleButton("Yenile"));

        add(left, BorderLayout.WEST);

        // ===== TABLO =====
        model = new DefaultTableModel(new Object[]{"ID","Üretici Adı"},0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadVendors();
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

    private void loadVendors() {
        model.setRowCount(0);
        List<Vendor> list = vendorDAO.getAllVendors();
        for (Vendor v : list) {
            model.addRow(new Object[]{v.getId(), v.getName()});
        }
    }
}
