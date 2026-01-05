/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLTV.Views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
/**
 *
 * @author khanh
 */
public class FormThongKe extends JPanel {
    private final Color PRIMARY = new Color(30, 136, 229);
    private final Color BG = new Color(245, 247, 250);
    private final Color CARD = Color.WHITE;

    // ===== BUTTON =====
    private JButton btnSachMuonNhieu = new JButton("Sách mượn nhiều nhất");
    private JButton btnDGMuonNhieu = new JButton("Độc giả mượn nhiều");
    private JButton btnQuaHan = new JButton("Phiếu mượn quá hạn");

    // ===== COUNTER =====
    private JLabel lbTongSach = new JLabel("0");
    private JLabel lbTongDG = new JLabel("0");
    private JLabel lbDangMuon = new JLabel("0");

    // ===== TABLE =====
    private JTable table;
    private DefaultTableModel model;

    public FormThongKe() {
        setLayout(new BorderLayout(15, 15));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createTop(), BorderLayout.NORTH);
        add(createTable(), BorderLayout.CENTER);

        styleButtons();
    }

    // ===== TOP =====
    private JPanel createTop() {
        JPanel p = new JPanel(new GridLayout(2, 1, 15, 15));
        p.setOpaque(false);
        p.add(createCounter());
        p.add(createButtons());
        return p;
    }

    // ===== COUNTER =====
    private JPanel createCounter() {
        JPanel p = new JPanel(new GridLayout(1, 3, 15, 15));
        p.setOpaque(false);
        p.add(counterBox("TỔNG SÁCH", lbTongSach));
        p.add(counterBox("TỔNG ĐỘC GIẢ", lbTongDG));
        p.add(counterBox("SÁCH ĐANG MƯỢN", lbDangMuon));
        return p;
    }

    private JPanel counterBox(String title, JLabel value) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lbTitle = new JLabel(title);
        lbTitle.setForeground(Color.GRAY);
        lbTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));

        value.setFont(new Font("Segoe UI", Font.BOLD, 28));
        value.setForeground(new Color(33, 33, 33));

        p.add(lbTitle, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    // ===== BUTTON =====
    private JPanel createButtons() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        p.setOpaque(false);
        p.add(btnSachMuonNhieu);
        p.add(btnDGMuonNhieu);
        p.add(btnQuaHan);
        return p;
    }

    private void styleButtons() {
        JButton[] btns = {btnSachMuonNhieu, btnDGMuonNhieu, btnQuaHan};
        for (JButton b : btns) {
            b.setBackground(PRIMARY);
            b.setForeground(Color.BLACK);
            b.setFont(new Font("Segoe UI", Font.BOLD, 13));
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        }
    }
    static class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }
    }
    // ===== TABLE =====
    private JScrollPane createTable() {
    // GIỮ NGUYÊN CÁC TRƯỜNG CỦA BẠN
    model = new DefaultTableModel(new Object[]{"", "", ""}, 0) {
        @Override public boolean isCellEditable(int row, int col) { return false; }
    };
    table = new JTable(model);

    // ===== LOOK & FEEL giống ảnh =====
    table.setRowHeight(28);
    table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    table.setBackground(Color.WHITE);
    table.setForeground(new Color(33, 33, 33));

    // kẻ ô mảnh
    table.setShowHorizontalLines(true);
    table.setShowVerticalLines(true);
    table.setGridColor(new Color(210, 210, 210));

    // chọn dòng xanh nhạt
    table.setSelectionBackground(new Color(224, 242, 254));
    table.setSelectionForeground(new Color(33, 33, 33));

    // full trắng cả vùng trống
    table.setFillsViewportHeight(true);

    // không cho kéo đổi vị trí cột
    JTableHeader header = table.getTableHeader();
    header.setReorderingAllowed(false);

    // Header giống ảnh: trắng, chữ đậm, có viền dưới/viền phải
    header.setPreferredSize(new Dimension(header.getPreferredSize().width, 30));
    header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    header.setBackground(Color.WHITE);
    header.setForeground(new Color(33, 33, 33));

    header.setDefaultRenderer((tbl, value, isSelected, hasFocus, row, column) -> {
        JLabel lb = new JLabel(value == null ? "" : value.toString());
        lb.setOpaque(true);
        lb.setBackground(Color.WHITE);
        lb.setForeground(new Color(33, 33, 33));
        lb.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lb.setHorizontalAlignment(SwingConstants.LEFT);

        // padding + kẻ viền mảnh như ảnh
        lb.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(210, 210, 210)),
                BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        return lb;
    });

    // Cell renderer: padding + viền mảnh như ảnh
    table.setDefaultRenderer(Object.class, (tbl, value, isSelected, hasFocus, row, col) -> {
        JLabel lb = new JLabel(value == null ? "" : value.toString());
        lb.setOpaque(true);
        lb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lb.setForeground(new Color(33, 33, 33));
        lb.setBackground(isSelected ? new Color(224, 242, 254) : Color.WHITE);

        lb.setHorizontalAlignment(SwingConstants.LEFT);

        lb.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(210, 210, 210)),
                BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        return lb;
    });

    // Scrollpane viền xám nhạt, nền trắng
    JScrollPane sp = new JScrollPane(table);
    sp.getViewport().setBackground(Color.WHITE);
    sp.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));

    return sp;
}

    // ===== GETTER =====
    public JButton getBtnSachMuonNhieu() { return btnSachMuonNhieu; }
    public JButton getBtnDGNhieu() { return btnDGMuonNhieu; }
    public JButton getBtnQuaHan() { return btnQuaHan; }
    public DefaultTableModel getModel() { return model; }

    // ===== SETTER =====
    public void setTongSach(int n) { lbTongSach.setText(String.valueOf(n)); }
    public void setTongDG(int n) { lbTongDG.setText(String.valueOf(n)); }
    public void setDangMuon(int n) { lbDangMuon.setText(String.valueOf(n)); }

}
