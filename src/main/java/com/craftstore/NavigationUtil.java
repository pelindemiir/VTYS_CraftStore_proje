package com.craftstore;

import javax.swing.*;

/*
 * =====================================================
 * NAVIGATION UTIL
 * - Ekranlar arası geçiş için yardımcı sınıf
 * - Projede zorunlu değildir
 * =====================================================
 */
public class NavigationUtil {

    // Mevcut pencereyi kapatıp Login ekranına döner
    public static void logout(JFrame currentFrame) {
        currentFrame.dispose();
        new LoginForm().setVisible(true);
    }

    // Admin panele geri dönüş
    public static void goBackToAdmin(JFrame currentFrame) {
        currentFrame.dispose();
        new AdminPanel().setVisible(true);
    }

    // Uygulamayı tamamen kapatır
    public static void exitApp() {
        System.exit(0);
    }
}
