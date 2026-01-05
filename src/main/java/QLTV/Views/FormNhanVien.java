package QLTV.Views;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class FormNhanVien extends JPanel {

    private JTextField txtSearch = new JTextField();
    private JButton btnSearch = new JButton("Tìm");

    private JTextField txtMaNV = new JTextField();
    private JTextField txtTenNV = new JTextField();
    private JTextField txtQueQuan = new JTextField();
    private JComboBox<String> cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
    private JTextField txtNamSinh = new JTextField(); // NEW
    private JComboBox<String> cboVaiTro = new JComboBox<>(new String[]{"Quản lý", "Thủ thư"});
    private JTextField txtEmail = new JTextField();
    private JTextField txtSdt = new JTextField();

    private JTextField txtUser = new JTextField();
    private JPasswordField txtPass = new JPasswordField();

    private JButton btnThem = new JButton("Thêm");
    private JButton btnSua = new JButton("Sửa");
    private JButton btnXoa = new JButton("Xóa");
    private JButton btnLamMoi = new JButton("Làm mới");
    private JButton btnNhapFile = new JButton("Nhập file (CSV)");
    private JButton btnXuatFile = new JButton("Xuất file (CSV)");

    private JTable tblNV;
    private DefaultTableModel model;

    public FormNhanVien() {
        setLayout(new BorderLayout());
        setBackground(new Color(235, 242, 250));
        setBorder(new EmptyBorder(12, 12, 12, 12));

        add(createHeader(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
    }

    private JComponent createHeader() {
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel title = new JLabel("QUẢN LÝ NHÂN VIÊN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(20, 40, 70));

        JPanel searchBox = new JPanel(new BorderLayout(8, 0));
        searchBox.setOpaque(false);

        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(260, 36));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 220), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        txtSearch.setBackground(Color.WHITE);

        stylePrimary(btnSearch);
        btnSearch.setPreferredSize(new Dimension(80, 36));

        searchBox.add(txtSearch, BorderLayout.CENTER);
        searchBox.add(btnSearch, BorderLayout.EAST);

        header.add(title, BorderLayout.WEST);
        header.add(searchBox, BorderLayout.EAST);

        return header;
    }

    private JComponent createBody() {
        JPanel body = new JPanel(new BorderLayout(12, 12));
        body.setOpaque(false);

        body.add(createFormCard(), BorderLayout.WEST);
        body.add(createTableCard(), BorderLayout.CENTER);

        return body;
    }

    private JComponent createFormCard() {
        JPanel card = new RoundedPanel(18);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(360, 0));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel lb = new JLabel("Thông tin nhân viên");
        lb.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lb.setForeground(new Color(30, 50, 90));

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 2, 6, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int r = 0;
        r = addRow(form, gbc, r, "Mã NV", txtMaNV);
        r = addRow(form, gbc, r, "Tên NV", txtTenNV);
        r = addRow(form, gbc, r, "Quê quán", txtQueQuan);
        r = addRow(form, gbc, r, "Giới tính", cboGioiTinh);
        r = addRow(form, gbc, r, "Năm sinh", txtNamSinh); // NEW
        r = addRow(form, gbc, r, "Vai trò", cboVaiTro);
        r = addRow(form, gbc, r, "Email", txtEmail);
        r = addRow(form, gbc, r, "SĐT", txtSdt);
        r = addRow(form, gbc, r, "User", txtUser);
        r = addRow(form, gbc, r, "Pass", txtPass);

        JScrollPane spForm = new JScrollPane(form);
        spForm.setBorder(null);
        spForm.getViewport().setOpaque(false);
        spForm.setOpaque(false);
        spForm.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spForm.getVerticalScrollBar().setUnitIncrement(16);

        JPanel actions = new JPanel(new GridLayout(3, 2, 10, 10));
        actions.setOpaque(false);
        actions.setBorder(new EmptyBorder(12, 0, 0, 0));

        stylePrimary(btnThem);
        stylePrimary(btnSua);
        styleDanger(btnXoa);
        styleGhost(btnLamMoi);
        styleAccent(btnNhapFile);
        styleAccent(btnXuatFile);

        actions.add(btnThem);
        actions.add(btnSua);
        actions.add(btnXoa);
        actions.add(btnLamMoi);
        actions.add(btnNhapFile);
        actions.add(btnXuatFile);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(lb, BorderLayout.NORTH);
        top.add(new JSeparator(), BorderLayout.SOUTH);

        card.add(top, BorderLayout.NORTH);
        card.add(spForm, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);

        styleInput(txtMaNV);
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(new Color(230, 230, 230));

        styleInput(txtTenNV);
        styleInput(txtQueQuan);
        styleCombo(cboGioiTinh);
        styleInput(txtNamSinh); // NEW
        styleCombo(cboVaiTro);
        styleInput(txtEmail);
        styleInput(txtSdt);
        styleInput(txtUser);
        stylePassword(txtPass);

        return card;
    }

    private int addRow(JPanel form, GridBagConstraints gbc, int row, String text, JComponent field) {
        JLabel lb = new JLabel(text);
        lb.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        gbc.gridx = 0; gbc.gridy = row;
        form.add(lb, gbc);

        gbc.gridy = row + 1;
        form.add(field, gbc);

        return row + 2;
    }

    private JComponent createTableCard() {
        JPanel card = new RoundedPanel(18);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel lb = new JLabel("Danh sách nhân viên");
        lb.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lb.setForeground(new Color(30, 50, 90));

        String[] cols = {"Mã NV", "Tên NV", "Quê quán", "Giới tính", "Năm sinh", "Vai trò", "Email", "SĐT", "User"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        tblNV = new JTable(model);
        tblNV.setRowHeight(28);
        tblNV.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblNV.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblNV.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane sp = new JScrollPane(tblNV);
        sp.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240)));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(lb, BorderLayout.NORTH);
        top.add(new JSeparator(), BorderLayout.SOUTH);

        card.add(top, BorderLayout.NORTH);
        card.add(sp, BorderLayout.CENTER);

        return card;
    }

    // ===== styles =====
    private void styleInput(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setPreferredSize(new Dimension(0, 36));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 230), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        f.setBackground(new Color(250, 252, 255));
    }

    private void stylePassword(JPasswordField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setPreferredSize(new Dimension(0, 36));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 230), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        f.setBackground(new Color(250, 252, 255));
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setPreferredSize(new Dimension(0, 36));
        cb.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 230), 1, true),
                new EmptyBorder(4, 8, 4, 8)
        ));
        cb.setBackground(new Color(250, 252, 255));
        cb.setOpaque(true);
    }

    private void stylePrimary(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(45, 126, 255));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 36));
    }

    private void styleDanger(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(220, 70, 70));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 36));
    }

    private void styleGhost(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setForeground(new Color(45, 126, 255));
        b.setBackground(Color.WHITE);
        b.setBorder(BorderFactory.createLineBorder(new Color(45, 126, 255), 1, true));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 36));
    }

    private void styleAccent(JButton b) {
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(46, 170, 125));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(0, 36));
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

    // ===== getters =====
    public JTextField getTxtSearch() { return txtSearch; }
    public JButton getBtnSearch() { return btnSearch; }

    public JButton getBtnThem() { return btnThem; }
    public JButton getBtnSua() { return btnSua; }
    public JButton getBtnXoa() { return btnXoa; }
    public JButton getBtnLamMoi() { return btnLamMoi; }
    public JButton getBtnNhapFile() { return btnNhapFile; }
    public JButton getBtnXuatFile() { return btnXuatFile; }

    public JTable getTblNV() { return tblNV; }
    public DefaultTableModel getModel() { return model; }

    public String getMaNV() { return txtMaNV.getText().trim(); }
    public String getTenNV() { return txtTenNV.getText().trim(); }
    public String getQueQuan() { return txtQueQuan.getText().trim(); }
    public String getGioiTinh() { return cboGioiTinh.getSelectedItem() == null ? "" : cboGioiTinh.getSelectedItem().toString(); }
    public String getNamSinh() { return txtNamSinh.getText().trim(); } // NEW
    public String getVaiTro() { return cboVaiTro.getSelectedItem() == null ? "" : cboVaiTro.getSelectedItem().toString(); }
    public String getEmail() { return txtEmail.getText().trim(); }
    public String getSdt() { return txtSdt.getText().trim(); }
    public String getUser() { return txtUser.getText().trim(); }
    public String getPassRaw() { return new String(txtPass.getPassword()).trim(); }

    public void setMaNV(String ma) { txtMaNV.setText(ma); }

    public void clearForm() {
        txtMaNV.setText("");
        txtTenNV.setText("");
        txtQueQuan.setText("");
        txtNamSinh.setText("");
        txtEmail.setText("");
        txtSdt.setText("");
        txtUser.setText("");
        txtPass.setText("");
        if (cboGioiTinh.getItemCount() > 0) cboGioiTinh.setSelectedIndex(0);
        if (cboVaiTro.getItemCount() > 0) cboVaiTro.setSelectedIndex(0);
    }

    // Pass không fill lại (tránh lộ)
    public void setForm(String maNV, String tenNV, String queQuan, String gioiTinh, String namSinh,
                        String vaiTro, String email, String sdt, String user) {
        txtMaNV.setText(maNV);
        txtTenNV.setText(tenNV);
        txtQueQuan.setText(queQuan);
        cboGioiTinh.setSelectedItem(gioiTinh);
        txtNamSinh.setText(namSinh);
        cboVaiTro.setSelectedItem(vaiTro);
        txtEmail.setText(email);
        txtSdt.setText(sdt);
        txtUser.setText(user);
        txtPass.setText("");
    }
}
