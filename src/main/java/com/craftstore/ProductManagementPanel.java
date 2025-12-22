package com.craftstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/*
 * =====================================================
 * PRODUCT MANAGEMENT PANEL (ADMIN)
 * - Ürün CRUD + arama + tablo
 * - Tekrarlayan button oluşturma tek metoda alındı
 * =====================================================
 */
public class ProductManagementPanel extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private final ProductDAO productDAO = new ProductDAO();

    private JTextField txtSearch;


    // Header – mor alt tonlu, dengeli
    private static final Color HEADER_COLOR = new Color(108, 102, 165);
    private static final Color BTN_BLUE     = new Color(150, 140, 210);
    private static final Color BTN_GREEN    = new Color(175, 160, 220);
    private static final Color BTN_ORANGE   = new Color(200, 185, 235);
    private static final Color BTN_RED      = new Color(125, 105, 185);
    private static final Color BG_COLOR     = new Color(247, 247, 251);


    public ProductManagementPanel() {

        setTitle("CraftStore - Ürün Yönetimi");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setPreferredSize(new Dimension(1000, 95));

        // Geri butonu
        JButton btnBack = new JButton("⬅ Geri");
        btnBack.addActionListener(e -> dispose());
        header.add(btnBack, BorderLayout.WEST);

        // Başlık
        JLabel title = new JLabel("ÜRÜN YÖNETİM PANELİ", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(title, BorderLayout.CENTER);

        // Arama paneli
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setOpaque(false);

        txtSearch = new JTextField(20);

        JButton btnSearch = new JButton("Ara");
        JButton btnClear  = new JButton("Temizle");

        searchPanel.add(new JLabel("Ürün Adı:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnClear);

        header.add(searchPanel, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        // ================= SOL BUTON PANELİ =================
        JPanel leftPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        leftPanel.setBackground(BG_COLOR);

        JButton btnAdd     = createButton("Yeni Ürün Ekle", BTN_BLUE);
        JButton btnDelete  = createButton("Ürünü Sil", BTN_RED);
        JButton btnUpdate  = createButton("Ürünü Güncelle", BTN_GREEN);
        JButton btnRefresh = createButton("Listeyi Yenile", BTN_ORANGE);

        leftPanel.add(btnAdd);
        leftPanel.add(btnDelete);
        leftPanel.add(btnUpdate);
        leftPanel.add(btnRefresh);

        add(leftPanel, BorderLayout.WEST);

        // ================= TABLO =================
        String[] cols = {"ID", "Satıcı", "Kategori", "Ürün Adı", "Açıklama", "Fiyat", "Stok"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // İlk yükleme
        loadProducts();

        // ================= EVENTLER =================
        btnSearch.addActionListener(e -> searchProducts());
        btnClear.addActionListener(e -> {
            txtSearch.setText("");
            loadProducts();
        });

        btnRefresh.addActionListener(e -> loadProducts());
        btnAdd.addActionListener(e -> addProductDialog());
        btnDelete.addActionListener(e -> deleteProduct());
        btnUpdate.addActionListener(e -> updateProductDialog());
    }

    // Tekrarlayan buton görsel ayarı tek metotta
    private JButton createButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        return b;
    }

    // =================================================
    // ÜRÜNLERİ YÜKLE
    // =================================================
    private void loadProducts() {

        model.setRowCount(0);

        List<Product> list = productDAO.getAllProducts();

        for (Product p : list) {
            model.addRow(new Object[]{
                    p.getProductId(),
                    p.getVendorId(),
                    p.getCategoryId(),
                    p.getProductName(),
                    p.getDescription(),
                    p.getPrice(),
                    p.getStockQuantity()
            });
        }
    }

    // =================================================
    // ARAMA
    // =================================================
    private void searchProducts() {

        String keyword = txtSearch.getText().trim();

        model.setRowCount(0);

        List<Product> list = keyword.isEmpty()
                ? productDAO.getAllProducts()
                : productDAO.searchProducts(keyword);

        for (Product p : list) {
            model.addRow(new Object[]{
                    p.getProductId(),
                    p.getVendorId(),
                    p.getCategoryId(),
                    p.getProductName(),
                    p.getDescription(),
                    p.getPrice(),
                    p.getStockQuantity()
            });
        }
    }

    // =================================================
    // ÜRÜN EKLE DIALOG
    // =================================================
    private void addProductDialog() {

        JTextField name     = new JTextField();
        JTextField desc     = new JTextField();
        JTextField price    = new JTextField();
        JTextField stock    = new JTextField();
        JTextField vendor   = new JTextField();
        JTextField category = new JTextField();

        Object[] input = {
                "Ürün Adı:", name,
                "Açıklama:", desc,
                "Fiyat:", price,
                "Stok:", stock,
                "Satıcı ID:", vendor,
                "Kategori ID:", category
        };

        if (JOptionPane.showConfirmDialog(
                this, input, "Yeni Ürün Ekle", JOptionPane.OK_CANCEL_OPTION)
                == JOptionPane.OK_OPTION) {

            productDAO.addProduct(
                    Integer.parseInt(vendor.getText()),
                    Integer.parseInt(category.getText()),
                    name.getText(),
                    desc.getText(),
                    Double.parseDouble(price.getText()),
                    Integer.parseInt(stock.getText())
            );

            loadProducts();
        }
    }

    // =================================================
    // ÜRÜN GÜNCELLE DIALOG
    // =================================================
    private void updateProductDialog() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Ürün seçin.");
            return;
        }

        int id = (int) table.getValueAt(row, 0);

        JTextField name     = new JTextField(table.getValueAt(row, 3).toString());
        JTextField desc     = new JTextField(table.getValueAt(row, 4).toString());
        JTextField price    = new JTextField(table.getValueAt(row, 5).toString());
        JTextField stock    = new JTextField(table.getValueAt(row, 6).toString());
        JTextField category = new JTextField(table.getValueAt(row, 2).toString());

        // vendor burada updateProduct içine gitmiyor (mevcut kodun mantığı)
        // satıcı değişimi istenirse ayrı fonksiyon gerekir
        Object[] input = {
                "Ürün Adı:", name,
                "Açıklama:", desc,
                "Fiyat:", price,
                "Stok:", stock,
                "Kategori ID:", category
        };

        if (JOptionPane.showConfirmDialog(
                this, input, "Ürünü Güncelle", JOptionPane.OK_CANCEL_OPTION)
                == JOptionPane.OK_OPTION) {

            productDAO.updateProduct(
                    id,
                    name.getText(),
                    desc.getText(),
                    Double.parseDouble(price.getText()),
                    Integer.parseInt(stock.getText()),
                    Integer.parseInt(category.getText())
            );

            loadProducts();
        }
    }

    // =================================================
    // ÜRÜN SİL (SOFT DELETE)
    // =================================================
    private void deleteProduct() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Ürün seçin.");
            return;
        }

        int id = (int) table.getValueAt(row, 0);

        productDAO.deleteProduct(id);

        loadProducts();
    }
}
