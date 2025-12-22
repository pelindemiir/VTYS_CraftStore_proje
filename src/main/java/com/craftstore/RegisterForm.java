package com.craftstore;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

// =================================================
// MÜŞTERİ KAYIT EKRANI
// customer + app_user tablolarına kayıt atar
// =================================================
public class RegisterForm extends JFrame {

    // Form alanları
    private JTextField txtName, txtEmail, txtPhone, txtAddress;
    private JPasswordField txtPassword;

    // Header – mor alt tonlu, dengeli
    private static final Color HEADER_COLOR = new Color(108, 102, 165);
    private static final Color BTN_COLOR = new Color(150, 140, 210);


    public RegisterForm() {

        setTitle("Yeni Müşteri Kayıt");
        setSize(420, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Üst başlık
        JLabel header = new JLabel("Yeni Müşteri Kayıt", SwingConstants.CENTER);
        header.setOpaque(true);
        header.setBackground(HEADER_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 22));
        add(header, BorderLayout.NORTH);

        // Form paneli
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Ad Soyad:"));
        txtName = new JTextField();
        panel.add(txtName);

        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        panel.add(new JLabel("Telefon:"));
        txtPhone = new JTextField();
        panel.add(txtPhone);

        panel.add(new JLabel("Adres:"));
        txtAddress = new JTextField();
        panel.add(txtAddress);

        panel.add(new JLabel("Şifre:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        add(panel, BorderLayout.CENTER);

        // Kayıt butonu
        JButton btnRegister = new JButton("Kayıt Ol");
        btnRegister.setBackground(BTN_COLOR);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.addActionListener(e -> registerUser());

        add(btnRegister, BorderLayout.SOUTH);
    }

    // =================================================
    // KAYIT METODU
    // =================================================
    private void registerUser() {

        String name = txtName.getText().trim();
        String password = new String(txtPassword.getPassword());

        // Zorunlu alan kontrolü
        if (name.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ad ve şifre boş olamaz!");
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {

            // CUSTOMER tablosuna kayıt
            String sqlCustomer =
                    "INSERT INTO customer (customer_name, email, phone, address) VALUES (?, ?, ?, ?)";
            PreparedStatement ps1 = conn.prepareStatement(sqlCustomer);
            ps1.setString(1, name);
            ps1.setString(2, txtEmail.getText());
            ps1.setString(3, txtPhone.getText());
            ps1.setString(4, txtAddress.getText());
            ps1.executeUpdate();

            // APP_USER tablosuna kayıt
            String sqlUser =
                    "INSERT INTO app_user (username, password_hash, role) VALUES (?, ?, 'CUSTOMER')";
            PreparedStatement ps2 = conn.prepareStatement(sqlUser);
            ps2.setString(1, name);
            ps2.setString(2, password); // ⚠ hash yok
            ps2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Kayıt başarılı!");
            dispose();
            new LoginForm().setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Kayıt hatası: " + e.getMessage());
        }
    }
}
