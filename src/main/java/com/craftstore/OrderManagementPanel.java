package com.craftstore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/*
 * =====================================================
 * ORDER MANAGEMENT PANEL (ADMIN)
 * - Sipariş listele
 * - Detay görüntüle
 * - Sil
 * =====================================================
 */
public class OrderManagementPanel extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private final OrderDAO orderDAO = new OrderDAO();

    // Soft renkler
    // Header – mor alt tonlu, yumuşak
    private static final Color HEADER_COLOR = new Color(108, 102, 165);

    // Ana buton – soft mor (eski mavi yerine)
    private static final Color BTN_BLUE     = new Color(150, 140, 210);

    // Kırmızı yerine morun koyu tonu (exit / danger için)
    private static final Color BTN_RED      = new Color(125, 105, 185);

    // Turuncu yerine morun açık tonu (warning / secondary için)
    private static final Color BTN_ORANGE   = new Color(190, 175, 225);

    // Arka plan – nötr, ferah
    private static final Color BG_COLOR     = new Color(247, 247, 251);


    public OrderManagementPanel() {

        setTitle("Sipariş Yönetimi");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_COLOR);
        header.setPreferredSize(new Dimension(900, 70));

        JButton btnBack = new JButton("⬅ Geri");
        btnBack.addActionListener(e -> dispose());
        header.add(btnBack, BorderLayout.WEST);

        JLabel title = new JLabel("SİPARİŞ YÖNETİM PANELİ", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // ================= SOL PANEL =================
        JPanel menu = new JPanel(new GridLayout(3, 1, 10, 10));
        menu.setPreferredSize(new Dimension(200, 0));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        menu.setBackground(BG_COLOR);

        JButton btnDetail  = createButton("Sipariş Detayı", BTN_BLUE);
        JButton btnDelete  = createButton("Siparişi Sil", BTN_RED);
        JButton btnRefresh = createButton("Listeyi Yenile", BTN_ORANGE);

        menu.add(btnDetail);
        menu.add(btnDelete);
        menu.add(btnRefresh);

        add(menu, BorderLayout.WEST);

        // ================= TABLO =================
        model = new DefaultTableModel(new Object[]{"ID", "Müşteri", "Tarih", "Toplam"}, 0);
        table = new JTable(model);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // İlk yükleme
        loadOrders();

        // Eventler
        btnDetail.addActionListener(e -> showOrderDetail());
        btnDelete.addActionListener(e -> deleteOrder());
        btnRefresh.addActionListener(e -> loadOrders());
    }

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

    // Siparişleri tabloya yükler
    private void loadOrders() {
        model.setRowCount(0);
        List<Order> orders = orderDAO.getAllOrders();
        for (Order o : orders) {
            model.addRow(new Object[]{o.getId(), o.getCustomerName(), o.getOrderDate(), o.getTotal()});
        }
    }

    // Detay dialog açar
    private void showOrderDetail() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        int orderId = (int) model.getValueAt(row, 0);

        List<OrderItem> items = orderDAO.getOrderItems(orderId);

        new OrderDetailDialog(this, orderId, items).setVisible(true);
    }

    // Sipariş siler
    private void deleteOrder() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        int orderId = (int) model.getValueAt(row, 0);

        if (JOptionPane.showConfirmDialog(this, "Siparişi silmek istediğinize emin misiniz?")
                == JOptionPane.OK_OPTION) {

            orderDAO.deleteOrder(orderId);
            loadOrders();
        }
    }
}
