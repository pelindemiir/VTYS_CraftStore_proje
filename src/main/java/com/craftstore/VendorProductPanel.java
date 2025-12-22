package com.craftstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/*
 * =====================================================
 * VENDOR PRODUCT PANEL
 * - Satıcı kendi ürünlerini yönetir
 * - add/update/delete
 * =====================================================
 */
public class VendorProductPanel extends JFrame {

    private int vendorId;

    private JTable table;
    private DefaultTableModel model;

    private final ProductDAO productDAO = new ProductDAO();

    // Header – mor alt tonlu, yumuşak
    private static final Color HEADER_COLOR = new Color(108, 102, 165);
    private static final Color BTN_BLUE     = new Color(150, 140, 210);
    private static final Color BTN_RED      = new Color(125, 105, 185);
    private static final Color BTN_GREEN    = new Color(175, 160, 220);


    public VendorProductPanel(int vendorId) {
        this.vendorId = vendorId;

        setTitle("Ürün Yönetimi - Satıcı ID: " + vendorId);
        setSize(820, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(HEADER_COLOR);
        topPanel.setPreferredSize(new Dimension(820, 65));

        JButton btnBack = new JButton("⬅ Geri");
        btnBack.addActionListener(e -> {
            dispose();
            new VendorDashboard(vendorId).setVisible(true);
        });

        JLabel titleLabel = new JLabel("Ürün Yönetimi (Satıcı ID: " + vendorId + ")", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        topPanel.add(btnBack, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // ================= TABLE =================
        model = new DefaultTableModel(new Object[]{"ID", "Ürün Adı", "Fiyat", "Stok"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadProducts();

        // ================= BUTTONS =================
        JPanel buttons = new JPanel();

        JButton btnAdd = createButton("Yeni Ürün", BTN_BLUE);
        JButton btnDelete = createButton("Sil", BTN_RED);
        JButton btnUpdate = createButton("Güncelle", BTN_GREEN);

        btnAdd.addActionListener(e -> addProduct());
        btnDelete.addActionListener(e -> deleteProduct());
        btnUpdate.addActionListener(e -> updateProduct());

        buttons.add(btnAdd);
        buttons.add(btnDelete);
        buttons.add(btnUpdate);

        add(buttons, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        return b;
    }

    // Satıcının ürünlerini yükler
    private void loadProducts() {
        model.setRowCount(0);
        List<Product> list = productDAO.getProductsByVendor(vendorId);
        for (Product p : list) {
            model.addRow(new Object[]{
                    p.getProductId(),
                    p.getProductName(),
                    p.getPrice(),
                    p.getStockQuantity()
            });
        }
    }

    // Yeni ürün ekleme
    private void addProduct() {

        String name = JOptionPane.showInputDialog(this, "Ürün adı:");
        if (name == null || name.isBlank()) return;

        try {
            double price = Double.parseDouble(JOptionPane.showInputDialog(this, "Fiyat:"));
            int stock = Integer.parseInt(JOptionPane.showInputDialog(this, "Stok:"));

            // categoryId şimdilik 1 (senin mevcut kod mantığın)
            productDAO.addProduct(vendorId, 1, name, "", price, stock);

            loadProducts();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Geçersiz değer girdiniz!");
        }
    }

    // Ürün silme
    private void deleteProduct() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) table.getValueAt(row, 0);

        productDAO.deleteProduct(id);

        loadProducts();
    }

    // Ürün güncelleme
    private void updateProduct() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) table.getValueAt(row, 0);

        String newName = JOptionPane.showInputDialog(this, "Yeni ad:");
        if (newName == null || newName.isBlank()) return;

        try {
            double newPrice = Double.parseDouble(JOptionPane.showInputDialog(this, "Yeni fiyat:"));
            int newStock = Integer.parseInt(JOptionPane.showInputDialog(this, "Yeni stok:"));

            productDAO.updateProduct(id, newName, "", newPrice, newStock, 1);

            loadProducts();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Geçersiz değer girdiniz!");
        }
    }
}
