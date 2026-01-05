/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLTV.Views;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author dinhd
 */
public class FormDangnhap extends JFrame {

    private JTextField txtUser = new JTextField();
    private JPasswordField txtPass = new JPasswordField();
    private JButton btnLogin = new JButton("Đăng nhập");
    private JButton btnExit  = new JButton("Thoát");
    private JLabel lbStatus  = new JLabel(" ");

    private static final Color BG      = new Color(245, 247, 250);
    private static final Color CARD    = Color.WHITE;
    private static final Color INPUT_BG= new Color(250, 252, 255);
    private static final Color BORDER  = new Color(220, 225, 230);

    private static final Color PRIMARY = new Color(45, 126, 255);
    private static final Color PRIMARY_HOVER = new Color(30, 110, 245);

    private static final int CARD_W = 340;
    private static final int CARD_H = 420;

    // Inner width = card width - padding left/right (26*2 = 52) => 288, lấy 280 cho đẹp
    private static final int FIELD_W = 280;
    private static final int FIELD_H = 42;

    private static final int BTN_H   = 44;

    public FormDangnhap() {
        setTitle("Đăng nhập - Thư viện UTT");
        setSize(420, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(BG);
        setContentPane(root);

        RoundedPanel card = new RoundedPanel(20);
        card.setBackground(CARD);
        card.setBorder(new EmptyBorder(26, 26, 26, 26));
        card.setPreferredSize(new Dimension(CARD_W, CARD_H));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lbTitle = new JLabel("Admin Login");
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbSub = new JLabel("Đăng nhập bằng tài khoản admin");
        lbSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbSub.setForeground(new Color(120, 120, 120));
        lbSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lbTitle);
        card.add(Box.createVerticalStrut(6));
        card.add(lbSub);
        card.add(Box.createVerticalStrut(26));

        // Username
        card.add(label("Username"));
        card.add(Box.createVerticalStrut(6));
        styleInput(txtUser);
        card.add(txtUser);
        card.add(Box.createVerticalStrut(16));

        // Password
        card.add(label("Password"));
        card.add(Box.createVerticalStrut(6));
        styleInput(txtPass);
        card.add(txtPass);
        card.add(Box.createVerticalStrut(18));

        // Buttons
        stylePrimaryButton(btnLogin);
        styleGhostButton(btnExit);

        card.add(btnLogin);
        card.add(Box.createVerticalStrut(10));
        card.add(btnExit);

        // Status
        card.add(Box.createVerticalStrut(14));
        lbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbStatus.setForeground(new Color(200, 60, 60));
        lbStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lbStatus);

        // Enter để login
        txtPass.addActionListener(e -> btnLogin.doClick());

        root.add(card);
    }

    private JLabel label(String text) {
        JLabel lb = new JLabel(text);
        lb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lb.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lb;
    }

    private void styleInput(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // ✅ Fix lệch + quá dài: đồng bộ width theo inner card
        f.setPreferredSize(new Dimension(FIELD_W, FIELD_H));
        f.setMaximumSize(new Dimension(FIELD_W, FIELD_H));
        f.setMinimumSize(new Dimension(FIELD_W, FIELD_H));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);

        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(10, 12, 10, 12)
        ));
        f.setBackground(INPUT_BG);
    }

    private void stylePrimaryButton(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setForeground(Color.WHITE);
        b.setBackground(PRIMARY);

        // ✅ đồng bộ width như input
        b.setPreferredSize(new Dimension(FIELD_W, BTN_H));
        b.setMaximumSize(new Dimension(FIELD_W, BTN_H));
        b.setMinimumSize(new Dimension(FIELD_W, BTN_H));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);

        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // ✅ hover nhẹ cho đẹp
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(PRIMARY_HOVER); }
            @Override public void mouseExited(MouseEvent e)  { b.setBackground(PRIMARY); }
        });
    }

    private void styleGhostButton(JButton b) {
        b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        b.setFocusPainted(false);
        b.setForeground(PRIMARY);
        b.setBackground(Color.WHITE);
        b.setBorder(BorderFactory.createLineBorder(PRIMARY, 1, true));

        // ✅ đồng bộ width như input
        b.setPreferredSize(new Dimension(FIELD_W, BTN_H));
        b.setMaximumSize(new Dimension(FIELD_W, BTN_H));
        b.setMinimumSize(new Dimension(FIELD_W, BTN_H));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);

        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // ✅ hover nhẹ
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                b.setBackground(new Color(245, 249, 255));
            }
            @Override public void mouseExited(MouseEvent e)  {
                b.setBackground(Color.WHITE);
            }
        });
    }

    public String getUsername() { return txtUser.getText().trim(); }
    public String getPassword() { return new String(txtPass.getPassword()); }
    public JButton getBtnLogin() { return btnLogin; }
    public JButton getBtnExit() { return btnExit; }
    public void showStatus(String msg) { lbStatus.setText(msg == null ? " " : msg); }

    public Object getUser() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getPass() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    static class RoundedPanel extends JPanel {
        private final int radius;
        public RoundedPanel(int radius) { this.radius = radius; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
