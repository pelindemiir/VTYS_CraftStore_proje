package com.craftstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerManagementPanel extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private CustomerDAO customerDAO = new CustomerDAO();

    // 🎨 MOR TEMA
    private static final Color PURPLE_MAIN  = new Color(125, 115, 195);
    private static final Color PURPLE_LIGHT = new Color(175, 170, 225);
    private static final Color BG_COLOR     = new Color(245, 246, 250);

    public CustomerManagementPanel() {

        setTitle("Müşteri Yönetimi");
        setSize(1000, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PURPLE_MAIN);
        header.setPreferredSize(new Dimension(1000, 80));

        JButton btnBack = purpleButton("← Geri");
        btnBack.addActionListener(e -> dispose());
        header.add(btnBack, BorderLayout.WEST);

        JLabel title = new JLabel("MÜŞTERİ YÖNETİM PANELİ", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // ================= SOL PANEL =================
        JPanel left = new JPanel(new GridLayout(4, 1, 15, 15));
        left.setBackground(BG_COLOR);
        left.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JButton btnAdd     = purpleButton("Yeni Müşteri");
        JButton btnDelete  = purpleButton("Müşteri Sil");
        JButton btnUpdate  = purpleButton("Güncelle");
        JButton btnRefresh = purpleButton("Yenile");

        left.add(btnAdd);
        left.add(btnDelete);
        left.add(btnUpdate);
        left.add(btnRefresh);

        add(left, BorderLayout.WEST);

        // ================= TABLO =================
        model = new DefaultTableModel(
                new Object[]{"ID", "Ad", "E-mail", "Telefon"}, 0
        );
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ================= EVENTLER =================
        btnAdd.addActionListener(e -> addCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnRefresh.addActionListener(e -> loadCustomers());

        loadCustomers();
    }

    // ================= BUTON =================
    private JButton purpleButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(PURPLE_LIGHT);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        return b;
    }

    // ================= VERİ =================
    private void loadCustomers() {
        model.setRowCount(0);
        List<Customer> list = customerDAO.getAllCustomers();
        for (Customer c : list) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    c.getEmail(),
                    c.getPhone()
            });
        }
    }

    // ================= CRUD =================
    private void addCustomer() {
        JTextField name  = new JTextField();
        JTextField email = new JTextField();
        JTextField phone = new JTextField();

        Object[] input = {
                "Ad:", name,
                "Email:", email,
                "Telefon:", phone
        };

        if (JOptionPane.showConfirmDialog(
                this, input, "Yeni Müşteri",
                JOptionPane.OK_CANCEL_OPTION
        ) == JOptionPane.OK_OPTION) {

            customerDAO.addCustomer(
                    name.getText(),
                    email.getText(),
                    phone.getText()
            );
            loadCustomers();
        }
    }

    private void deleteCustomer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen müşteri seçin!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        if (JOptionPane.showConfirmDialog(
                this, "Müşteri silinsin mi?",
                "Onay", JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION) {

            customerDAO.deleteCustomer(id);
            loadCustomers();
        }
    }

    private void updateCustomer() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen müşteri seçin!");
            return;
        }

        int id = (int) model.getValueAt(row, 0);

        JTextField name  = new JTextField(model.getValueAt(row, 1).toString());
        JTextField email = new JTextField(model.getValueAt(row, 2).toString());
        JTextField phone = new JTextField(model.getValueAt(row, 3).toString());

        Object[] input = {
                "Ad:", name,
                "Email:", email,
                "Telefon:", phone
        };

        if (JOptionPane.showConfirmDialog(
                this, input, "Müşteri Güncelle",
                JOptionPane.OK_CANCEL_OPTION
        ) == JOptionPane.OK_OPTION) {

            customerDAO.updateCustomer(
                    id,
                    name.getText(),
                    email.getText(),
                    phone.getText()
            );
            loadCustomers();
        }
    }
}
