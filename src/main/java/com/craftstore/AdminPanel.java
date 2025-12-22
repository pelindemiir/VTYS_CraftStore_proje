package com.craftstore;

import javax.swing.*;
import java.awt.*;

/*
 * =====================================================
 * ADMIN PANEL
 * - Yönetici ana ekranıdır
 * - Diğer yönetim panellerine geçiş sağlar
 * =====================================================
 */
public class AdminPanel extends JFrame {

    // Soft renkler
    // =================== ANA YAPI (AYNI) ===================

    // Header: koyu antrasit (mor alt tonlu)
    private static final Color HEADER_COLOR = new Color(58, 56, 70);

    // Tek vurgu rengi: soft mor (tüm butonlar)
    private static final Color BTN_PRIMARY  = new Color(132, 115, 190);

    // Success = Primary (bilerek aynı, sade tasarım)
    private static final Color BTN_SUCCESS  = BTN_PRIMARY;

    // Warning: morun açık tonu (yeni renk DEĞİL)
    private static final Color BTN_WARNING  = new Color(190, 175, 225);

    // Danger: morun koyu tonu (yeni renk DEĞİL)
    private static final Color BTN_DANGER   = new Color(108, 90, 165);

    // Arka plan: çok açık, nötr
    private static final Color BG_COLOR     = new Color(247, 247, 251);



    public AdminPanel() {

        // Pencere ayarları
        setTitle("CraftStore - Admin Paneli");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setPreferredSize(new Dimension(900, 60));

        JLabel title = new JLabel("ADMIN PANELİ", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // ================= SOL MENÜ =================
        JPanel menu = new JPanel(new GridLayout(6, 1, 10, 10));
        menu.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        menu.setBackground(BG_COLOR);

        JButton btnProducts  = createButton("Ürün Yönetimi", BTN_PRIMARY);
        JButton btnCustomers = createButton("Müşteri Yönetimi", BTN_WARNING);
        JButton btnOrders    = createButton("Siparişler", BTN_SUCCESS);
        JButton btnReports   = createButton("Satış Raporları", BTN_PRIMARY);
        JButton btnVendors   = createButton("Üreticiler", BTN_WARNING);
        JButton btnLogout    = createButton("Çıkış Yap", BTN_DANGER);

        menu.add(btnProducts);
        menu.add(btnCustomers);
        menu.add(btnOrders);
        menu.add(btnReports);
        menu.add(btnVendors);
        menu.add(btnLogout);

        add(menu, BorderLayout.WEST);

        // ================= ORTA ALAN =================
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Color.WHITE);

        JLabel welcome = new JLabel(
                "<html><center>CraftStore Yönetim Sistemine<br>Hoş Geldiniz</center></html>",
                SwingConstants.CENTER
        );
        welcome.setFont(new Font("Arial", Font.BOLD, 28));
        welcome.setForeground(HEADER_COLOR);

        center.add(welcome, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        // ================= EVENTLER =================
        btnProducts.addActionListener(e -> new ProductManagementPanel().setVisible(true));
        btnCustomers.addActionListener(e -> new CustomerManagementPanel().setVisible(true));
        btnOrders.addActionListener(e -> new OrderManagementPanel().setVisible(true));
        btnReports.addActionListener(e -> new ReportsPanel().setVisible(true));
        btnVendors.addActionListener(e -> new VendorManagementPanel().setVisible(true));

        // Çıkış → LoginForm
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });
    }

    // Tekrarlayan buton kodları tek metoda alındı
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        return btn;
    }
}
