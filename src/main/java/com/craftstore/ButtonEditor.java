package com.craftstore;

import java.awt.*;
import javax.swing.*;

/*
 * =====================================================
 * ButtonEditor
 * - JTable içinde butona tıklanınca aksiyon çalıştırır (editor)
 * - CustomerProductList sınıfına callback yapar:
 *   -> addToCart(...)
 *   -> removeFromCart(...)
 * =====================================================
 */
public class ButtonEditor extends DefaultCellEditor {

    // Görünen buton
    private final JButton button;
    // Hücrede görünen yazı
    private String label;
    // Tıklama kontrolü
    private boolean isPushed;

    // Parent ekran referansı (sepete ekleme / silme işlemleri buradan çağrılır)
    private final CustomerProductList parent;

    // Bu editor "Sil" butonu mu?
    private final boolean isRemoveButton;

    // Soft renkler
    // ADD – morun açık, yumuşak tonu
    private static final Color BTN_ADD = new Color(165, 150, 210);

    // REMOVE – morun koyu, sıcak tonu
    private static final Color BTN_REMOVE = new Color(165, 120, 165);


    // Varsayılan: ekle butonu
    public ButtonEditor(JCheckBox checkBox, CustomerProductList parent) {
        this(checkBox, parent, false);
    }

    // Hem ekle hem sil destekler
    public ButtonEditor(JCheckBox checkBox, CustomerProductList parent, boolean isRemoveButton) {
        // DefaultCellEditor parent constructor
        super(checkBox);

        // Parent referansı set edilir
        this.parent = parent;

        // Sil mi ekle mi?
        this.isRemoveButton = isRemoveButton;

        // Buton oluşturulur
        button = new JButton();
        button.setOpaque(true);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        // Buton rengi mode’a göre belirlenir
        button.setBackground(isRemoveButton ? BTN_REMOVE : BTN_ADD);

        // Butona basılınca edit bitirilir -> getCellEditorValue tetiklenir
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int col) {

        // Hücredeki metin alınır
        label = value == null ? "" : value.toString();
        // Buton üstündeki yazı set edilir
        button.setText(label);
        // Tıklandı bayrağı
        isPushed = true;

        // Butonu döndür
        return button;
    }

    @Override
    public Object getCellEditorValue() {

        // Eğer gerçekten tıklandıysa çalıştır
        if (isPushed) {

            // Sil butonu ise seçili sepet satırını sil
            if (isRemoveButton) {
                parent.removeFromCart(parent.getCartItemIndex());
            } else {
                // Ekle butonu ise seçili ürün satırını sepete ekle
                Product p = parent.getProductFromRow(parent.getProductRowIndex());
                // Null kontrol (seçim yoksa)
                if (p != null) parent.addToCart(p);
            }
        }

        // Bayrağı sıfırla
        isPushed = false;

        // Hücre değeri olarak label döndür
        return label;
    }
}
