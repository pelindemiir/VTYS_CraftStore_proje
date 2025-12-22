package com.craftstore;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// =================================================
// GİRİŞ EKRANI
// Kullanıcı adı + şifre kontrol edilir
// Role göre yönlendirme yapılır
// =================================================
public class LoginForm extends JFrame {

    // Kullanıcıdan alınacak alanlar
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    // Soft renk paleti
    // Header – mor alt tonlu, dengeli
    private static final Color HEADER_COLOR = new Color(108, 102, 165);

    // Ana buton – soft mor (tek vurgu)
    private static final Color BTN_MAIN = new Color(150, 140, 210);

    // Çıkış – aynı morun koyu tonu (kırmızıya kaçmadan)
    private static final Color BTN_EXIT = new Color(125, 105, 185);


    public LoginForm() {

        // Pencere ayarları
        setTitle("CraftStore - Giriş");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== ÜST BAŞLIK =====
        JLabel header = new JLabel("CraftStore Giriş", SwingConstants.CENTER);
        header.setOpaque(true);
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        add(header, BorderLayout.NORTH);

        // ===== FORM =====
        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        form.add(new JLabel("Kullanıcı Adı:"));
        txtUsername = new JTextField();
        form.add(txtUsername);

        form.add(new JLabel("Şifre:"));
        txtPassword = new JPasswordField();
        form.add(txtPassword);

        add(form, BorderLayout.CENTER);

        // ===== BUTONLAR =====
        JButton btnLogin = new JButton("Giriş Yap");
        btnLogin.setBackground(BTN_MAIN);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener(e -> login());

        JButton btnRegister = new JButton("Üye Ol");
        btnRegister.setBackground(new Color(108, 102, 165));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.addActionListener(e -> {
            dispose();
            new RegisterForm().setVisible(true);
        });

        JButton btnExit = new JButton("Çıkış");
        btnExit.setBackground(BTN_EXIT);
        btnExit.setForeground(Color.WHITE);
        btnExit.addActionListener(e -> System.exit(0));

        JPanel bottom = new JPanel();
        bottom.add(btnLogin);
        bottom.add(btnRegister);
        bottom.add(btnExit);

        add(bottom, BorderLayout.SOUTH);
    }

    // =================================================
    // GİRİŞ KONTROL METODU
    // =================================================
    private void login() {

        // Alanlardan veri alınır
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        // Boş kontrolü
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Alanlar boş olamaz!");
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {

            // Kullanıcı kontrol SQL'i
            String sql =
                    "SELECT role, vendor_id FROM app_user WHERE username=? AND password_hash=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password); // ⚠ demo amaçlı hash yok

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                // Kullanıcının rolü alınır
                String role = rs.getString("role");
                int vendorId = rs.getInt("vendor_id");

                // Role göre yönlendirme
                if ("ADMIN".equalsIgnoreCase(role)) {
                    new AdminPanel().setVisible(true);
                } else if ("VENDOR".equalsIgnoreCase(role)) {
                    new VendorDashboard(vendorId).setVisible(true);
                } else {
                    new CustomerProductList().setVisible(true);
                }

                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Hatalı giriş!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB hatası: " + e.getMessage());
        }
    }
}
