package com.craftstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
 * =====================================================
 * CustomerProductList
 * - CUSTOMER rolü için alışveriş ekranı
 * - Sol: ürün listesi
 * - Sağ: sepet
 * - Alt: sipariş onay
 *
 * NOT:
 * - createOrder(customerId, cart) içinde customerId şimdilik sabit (1)
 *   Gerçek login sonrası customerId bağlanmalıdır.
 * =====================================================
 */
public class CustomerProductList extends JFrame {

    // Ürün tablosu
    private JTable productTable;
    // Sepet tablosu
    private JTable cartTable;

    // Ürün tablo modeli
    private DefaultTableModel productModel;
    // Sepet tablo modeli
    private DefaultTableModel cartModel;

    // DAO’lar
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderCreateDAO orderDAO = new OrderCreateDAO();

    // Sepet listesi
    private final List<CartItem> cart = new ArrayList<>();
    // Ürün cache (tablodaki row -> Product mapping)
    private List<Product> productsCache = new ArrayList<>();

    // Toplam label
    private JLabel lblTotal;

    // Soft renkler
    // Header – mor alt tonlu, yumuşak
    private static final Color HEADER_COLOR = new Color(108, 102, 165);

    // Ana buton – soft mor (tek vurgu)
    private static final Color BTN_PRIMARY  = new Color(150, 140, 210);

    // Danger – aynı morun koyu tonu
    private static final Color BTN_DANGER   = new Color(125, 105, 185);

    // Arka plan – çok açık, nötr
    private static final Color BG_COLOR     = new Color(247, 247, 251);

    public CustomerProductList() {

        // Pencere ayarları
        setTitle("CraftStore | Alışveriş");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Üst panel eklenir
        add(createTopPanel(), BorderLayout.NORTH);
        // Ürün listesi paneli eklenir
        add(createProductPanel(), BorderLayout.CENTER);
        // Sepet paneli eklenir
        add(createCartPanel(), BorderLayout.EAST);
        // Sipariş buton paneli eklenir
        add(createBottomPanel(), BorderLayout.SOUTH);

        // Ürünleri yükle
        loadProducts();
    }

    // =====================================================
    // ÜST PANEL: Başlık + Çıkış
    // =====================================================
    private JPanel createTopPanel() {

        // Panel oluştur
        JPanel panel = new JPanel(new BorderLayout());
        // Arka plan soft
        panel.setBackground(BG_COLOR);

        // Başlık label
        JLabel title = new JLabel("Ürünler", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(HEADER_COLOR);

        // Sağ üst çıkış butonu
        JButton btnLogout = new JButton("Çıkış Yap");
        btnLogout.setBackground(BTN_DANGER);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setOpaque(true);

        // Çıkış event
        btnLogout.addActionListener(e -> {
            // Ekranı kapat
            dispose();
            // Login ekranına dön
            new LoginForm().setVisible(true);
        });

        // Bileşenleri yerleştir
        panel.add(title, BorderLayout.CENTER);
        panel.add(btnLogout, BorderLayout.EAST);

        // Paneli döndür
        return panel;
    }

    // =====================================================
    // SOL: ÜRÜNLER PANELİ
    // =====================================================
    private JPanel createProductPanel() {

        // Ürün tablosu kolonları (kategori burada category_id olarak gösteriliyor)
        productModel = new DefaultTableModel(
                new Object[]{"ID", "Ürün", "Kategori", "Fiyat", "Stok", "Satıcı", "Ekle"}, 0
        );

        // JTable oluştur
        productTable = new JTable(productModel);

        // "Ekle" sütununu buton gibi göster
        productTable.getColumn("Ekle").setCellRenderer(new ButtonRenderer());
        // "Ekle" tıklanınca çalışacak editor
        productTable.getColumn("Ekle").setCellEditor(new ButtonEditor(new JCheckBox(), this));

        // Panel başlığı
        JLabel title = new JLabel("Ürünler");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(HEADER_COLOR);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel oluştur
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Başlığı ekle
        panel.add(title, BorderLayout.NORTH);
        // Tabloyu scroll ile ekle
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Panel döndür
        return panel;
    }

    // =====================================================
    // SAĞ: SEPET PANELİ
    // =====================================================
    private JPanel createCartPanel() {

        // Sepet tablosu kolonları
        cartModel = new DefaultTableModel(
                new Object[]{"Ürün", "Adet", "Toplam", "Sil"}, 0
        );

        // Sepet tablosu
        cartTable = new JTable(cartModel);

        // "Sil" sütunu buton gibi görünsün
        cartTable.getColumn("Sil").setCellRenderer(new ButtonRenderer());
        // "Sil" tıklanınca sepetten kaldır
        cartTable.getColumn("Sil").setCellEditor(new ButtonEditor(new JCheckBox(), this, true));

        // Sepet başlığı
        JLabel title = new JLabel("Sepetim");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(HEADER_COLOR);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Toplam label
        lblTotal = new JLabel("Toplam: 0.00 ₺");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 15));

        // Panel oluştur
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(400, 0));
        panel.setBackground(BG_COLOR);

        // Bileşenleri ekle
        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        panel.add(lblTotal, BorderLayout.SOUTH);

        // Panel döndür
        return panel;
    }

    // =====================================================
    // ALT PANEL: SİPARİŞ ONAY BUTONU
    // =====================================================
    private JPanel createBottomPanel() {

        // Sipariş onay butonu
        JButton btnOrder = new JButton("Siparişi Onayla");
        btnOrder.setFont(new Font("Arial", Font.BOLD, 16));
        btnOrder.setPreferredSize(new Dimension(220, 45));
        btnOrder.setBackground(BTN_PRIMARY);
        btnOrder.setForeground(Color.WHITE);
        btnOrder.setFocusPainted(false);
        btnOrder.setBorderPainted(false);
        btnOrder.setOpaque(true);

        // Sipariş onay event
        btnOrder.addActionListener(e -> confirmOrder());

        // Panel oluştur ve butonu ekle
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.add(btnOrder);

        return panel;
    }

    // =====================================================
    // ÜRÜNLERİ VERİTABANINDAN YÜKLE
    // =====================================================
    private void loadProducts() {

        // Tabloyu temizle
        productModel.setRowCount(0);

        // Satıcı bilgisi dahil ürün listesi
        productsCache = productDAO.getAllProductsWithVendor();

        // Cache’i tabloya bas
        for (Product p : productsCache) {
            productModel.addRow(new Object[]{
                    p.getProductId(),     // ID
                    p.getProductName(),   // ürün adı
                    p.getCategoryId(),    // kategori id (istersen sonra adını getiririz)
                    p.getPrice(),         // fiyat
                    p.getStockQuantity(), // stok
                    p.getVendorName(),    // satıcı adı
                    "Ekle"                // buton metni
            });
        }
    }

    // =====================================================
    // SEPETE EKLE
    // - Üründen adet ister
    // - Stok kontrol eder
    // - Aynı ürün varsa adet birleştirir
    // =====================================================
    public void addToCart(Product product) {

        // Kullanıcıdan adet al
        String input = JOptionPane.showInputDialog(
                this,
                product.getProductName() + " için adet girin (Stok: " + product.getStockQuantity() + "):"
        );

        // İptal edildi ise çık
        if (input == null) return;

        // Adedi parse et
        int quantity;
        try {
            quantity = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Geçersiz sayı!");
            return;
        }

        // Negatif/0 ve stok aşımı kontrol
        if (quantity <= 0 || quantity > product.getStockQuantity()) {
            JOptionPane.showMessageDialog(this, "Yetersiz stok!");
            return;
        }

        // Sepette aynı ürün var mı kontrol
        for (CartItem item : cart) {
            if (item.getProductId() == product.getProductId()) {

                // Yeni toplam adet
                int newQty = item.getQuantity() + quantity;

                // Stok aşımı kontrol
                if (newQty > product.getStockQuantity()) {
                    JOptionPane.showMessageDialog(this, "Yetersiz stok!");
                    return;
                }

                // Adedi güncelle
                item.setQuantity(newQty);

                // Sepeti yenile
                refreshCart();
                return;
            }
        }

        // Sepette yoksa yeni CartItem ekle
        cart.add(new CartItem(product, quantity));

        // Sepeti yenile
        refreshCart();
    }

    // =====================================================
    // SEPET TABLOSUNU YENİLE + TOPLAM HESAPLA
    // =====================================================
    private void refreshCart() {

        // Tabloyu temizle
        cartModel.setRowCount(0);

        // Toplam tutar
        double total = 0;

        // Sepetteki tüm satırları tabloya bas
        for (CartItem item : cart) {
            cartModel.addRow(new Object[]{
                    item.getProduct().getProductName(), // ürün adı
                    item.getQuantity(),                 // adet
                    item.getTotal(),                    // satır toplam
                    "Sil"                               // buton metni
            });
            // Toplama ekle
            total += item.getTotal();
        }

        // Toplam label güncelle
        lblTotal.setText(String.format("Toplam: %.2f ₺", total));
    }

    // =====================================================
    // SİPARİŞ OLUŞTUR
    // - Sepet boşsa engeller
    // - OrderCreateDAO ile transaction yapar
    // =====================================================
    private void confirmOrder() {

        // Sepet boş mu?
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sepet boş!");
            return;
        }

        try {
            // ⚠ Şimdilik sabit müşteri ID (login sonrası bağlanmalı)
            int customerId = 1;

            // Siparişi oluştur
            orderDAO.createOrder(customerId, cart);

            // Başarılı mesaj
            JOptionPane.showMessageDialog(this, "Sipariş başarıyla oluşturuldu!");

            // Sepeti temizle
            cart.clear();

            // Sepeti yenile
            refreshCart();

            // Ürünleri tekrar yükle (stok düşmüş olabilir)
            loadProducts();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    // =====================================================
    // SEPETTEN SİL
    // =====================================================
    public void removeFromCart(int index) {

        // Index geçerli mi?
        if (index >= 0 && index < cart.size()) {
            // Elemanı kaldır
            cart.remove(index);
            // Sepeti yenile
            refreshCart();
        }
    }

    // Sepet tablosundan seçili satır index’i
    public int getCartItemIndex() {
        return cartTable.getSelectedRow();
    }

    // Ürün tablosundan seçili satır index’i
    public int getProductRowIndex() {
        return productTable.getSelectedRow();
    }

    // Ürün satırına göre Product nesnesini döndür
    public Product getProductFromRow(int row) {
        return (row >= 0 && row < productsCache.size())
                ? productsCache.get(row)
                : null;
    }
}
