package com.craftstore;

import javax.swing.*;
import java.awt.*;

/*
 * =====================================================
 * VENDOR DASHBOARD
 * - TEK MOR TEMA
 * - Mavi YOK
 * - Üretici ana paneli
 * =====================================================
 */
public class VendorDashboard extends JFrame {

    private int vendorId;

    // ===== TEK MOR TEMA =====
    private static final Color HEADER_COLOR = new Color(128, 104, 176); // koyu mor
    private static final Color BTN_COLOR    = new Color(165, 145, 210); // orta mor
    private static final Color BG_COLOR     = new Color(248, 246, 252); // açık arka plan

    public VendorDashboard(int vendorId) {
        this.vendorId = vendorId;

        setTitle("Üretici Paneli | Satıcı ID: " + vendorId);
        setSize(520, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setPreferredSize(new Dimension(520, 85));

        JLabel title = new JLabel(
                "Üretici Paneline Hoş Geldiniz (ID: " + vendorId + ")",
                SwingConstants.CENTER
        );
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // ================= ORTA PANEL =================
        JPanel center = new JPanel(new GridLayout(3, 1, 22, 22));
        center.setBackground(BG_COLOR);
        center.setBorder(BorderFactory.createEmptyBorder(40, 90, 40, 90));

        JButton btnProducts = createButton("Ürünlerimi Yönet");
        JButton btnOrders   = createButton("Siparişlerim");
        JButton btnLogout   = createButton("Çıkış Yap");

        center.add(btnProducts);
        center.add(btnOrders);
        center.add(btnLogout);

        add(center, BorderLayout.CENTER);

        // ================= EVENTLER =================
        btnProducts.addActionListener(e ->
                new VendorProductPanel(vendorId).setVisible(true)
        );

        btnOrders.addActionListener(e ->
                new VendorOrderPanel(vendorId).setVisible(true)
        );

        btnLogout.addActionListener(e -> {
            for (Window w : Window.getWindows()) {
                w.dispose();
            }

            new LoginForm().setVisible(true);
        });

    }

    // ================= MOR BUTON =================
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(BTN_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(260, 46));
        return btn;
    }
}
