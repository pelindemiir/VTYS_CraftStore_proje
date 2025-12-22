package com.craftstore;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

/*
 * =====================================================
 * ButtonRenderer
 * - JTable içinde hücreyi buton gibi göstermek için kullanılır
 * - "Ekle" ve "Sil" butonlarının görsel tarafıdır (render)
 * =====================================================
 */
public class ButtonRenderer extends JButton implements TableCellRenderer {

    // Soft renkler
    private static final Color BTN_COLOR = new Color(165, 150, 210);

    public ButtonRenderer() {
        // Buton şeffaf olmamalı ki arka plan rengi görünsün
        setOpaque(true);
        // Soft mavi arka plan
        setBackground(BTN_COLOR);
        // Yazı rengi beyaz
        setForeground(Color.WHITE);
        // Kenarlık çizimi kapalı
        setBorderPainted(false);
        // Fokus çizimi kapalı
        setFocusPainted(false);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        // Hücredeki metin null değilse ekrana bas
        setText(value == null ? "" : value.toString());
        // Renderer olarak kendisini döndür
        return this;
    }
}
