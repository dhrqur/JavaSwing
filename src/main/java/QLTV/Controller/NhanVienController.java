/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template*/

package QLTV.Controller;

import QLTV.Domain.NhanVien;
import QLTV.Model.NhanVienDAO;
import QLTV.Views.FormNhanVien;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class NhanVienController {

    private final FormNhanVien view;
    private final NhanVienDAO dao = new NhanVienDAO();

    public NhanVienController(FormNhanVien view) {
        this.view = view;
        registerEvents();
        loadTable();
        view.setMaNV(dao.taoMaNVMoi());
    }

    private void registerEvents() {
        view.getBtnThem().addActionListener(e -> handleInsert());
        view.getBtnSua().addActionListener(e -> handleUpdate());
        view.getBtnXoa().addActionListener(e -> handleDelete());

        view.getBtnLamMoi().addActionListener(e -> {
            view.clearForm();
            view.setMaNV(dao.taoMaNVMoi());
        });

        view.getBtnSearch().addActionListener(e -> handleSearch());
        view.getTxtSearch().addActionListener(e -> handleSearch());

        view.getBtnNhapFile().addActionListener(e -> importCSVToTable());
        view.getBtnXuatFile().addActionListener(e -> exportTableToCSV());

        view.getTblNV().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromSelectedRow();
        });
    }

    private void loadTable() {
        fillTable(dao.findAll());
    }

    private void fillTable(List<NhanVien> list) {
        DefaultTableModel m = view.getModel();
        m.setRowCount(0);
        for (NhanVien nv : list) {
            m.addRow(new Object[]{
                nv.getMaNV(), nv.getTenNV(), nv.getQueQuan(), nv.getGioiTinh(),
                nv.getNamSinh(), nv.getVaiTro(), nv.getEmail(), nv.getSdt(), nv.getUser()
            });
        }
    }

    private void handleSearch() {
        String key = view.getTxtSearch().getText().trim();
        if (key.isEmpty()) { loadTable(); return; }
        fillTable(dao.search(key));
    }

    private void fillFormFromSelectedRow() {
        int row = view.getTblNV().getSelectedRow();
        if (row < 0) return;

        DefaultTableModel m = view.getModel();
        view.setForm(
                String.valueOf(m.getValueAt(row, 0)),
                String.valueOf(m.getValueAt(row, 1)),
                String.valueOf(m.getValueAt(row, 2)),
                String.valueOf(m.getValueAt(row, 3)),
                String.valueOf(m.getValueAt(row, 4)),
                String.valueOf(m.getValueAt(row, 5)),
                String.valueOf(m.getValueAt(row, 6)),
                String.valueOf(m.getValueAt(row, 7)),
                String.valueOf(m.getValueAt(row, 8))
        );
    }

    private boolean isValidNamSinh(String ns) {
        return Pattern.matches("^\\d{4}$", ns) || Pattern.matches("^\\d{2}/\\d{2}/\\d{4}$", ns);
    }

    private NhanVien readForm(String ma, boolean isUpdate) {
        String ten = view.getTenNV();
        String que = view.getQueQuan();
        String gt = view.getGioiTinh();
        String namSinh = view.getNamSinh();
        String vt = view.getVaiTro();
        String email = view.getEmail();
        String sdt = view.getSdt();
        String user = view.getUser();
        String pass = view.getPassRaw(); // PLAIN

        if (ten.isEmpty() || que.isEmpty() || gt.isEmpty() || namSinh.isEmpty() || vt.isEmpty()
                || email.isEmpty() || sdt.isEmpty() || user.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Không được để trống!");
            return null;
        }

        if (!isValidNamSinh(namSinh)) {
            JOptionPane.showMessageDialog(view, "Năm sinh không hợp lệ! Nhập YYYY hoặc dd/MM/YYYY");
            return null;
        }

        if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$", email)) {
            JOptionPane.showMessageDialog(view, "Email không hợp lệ!");
            return null;
        }

        if (!Pattern.matches("^\\d{10,11}$", sdt)) {
            JOptionPane.showMessageDialog(view, "SĐT không hợp lệ!");
            return null;
        }

        if (!isUpdate) {
            if (pass.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Không được để trống Pass!");
                return null;
            }
        } else {
            // update: nếu không nhập pass => giữ pass cũ
            if (pass.isEmpty()) {
                String oldPass = "";
                for (NhanVien nv : dao.findAll()) {
                    if (nv.getMaNV().equals(ma)) { oldPass = nv.getPass(); break; }
                }
                if (oldPass.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Không lấy được Pass cũ. Hãy nhập Pass mới!");
                    return null;
                }
                pass = oldPass;
            }
        }

        if (pass.length() < 4) {
            JOptionPane.showMessageDialog(view, "Pass tối thiểu 4 ký tự!");
            return null;
        }

        return new NhanVien(ma, ten, que, gt, namSinh, vt, email, sdt, user, pass);
    }

    private void handleInsert() {
        String ma = view.getMaNV();
        if (ma.isEmpty()) ma = dao.taoMaNVMoi();

        NhanVien nv = readForm(ma, false);
        if (nv == null) return;

        String ten = view.getTenNV();
        if (!ten.isEmpty() && dao.checkTrungTenNV(ten)) {
            JOptionPane.showMessageDialog(view, "Tên nhân viên đã tồn tại!");
            return;
        }

        if (dao.existsEmail(nv.getEmail(), "")) { JOptionPane.showMessageDialog(view, "Email đã tồn tại!"); return; }
        if (dao.existsSdt(nv.getSdt(), "")) { JOptionPane.showMessageDialog(view, "SĐT đã tồn tại!"); return; }
        if (dao.existsUser(nv.getUser(), "")) { JOptionPane.showMessageDialog(view, "User đã tồn tại!"); return; }

        int ok = dao.insert(nv);
        if (ok > 0) {
            JOptionPane.showMessageDialog(view, "Thêm thành công!");
            loadTable();
            view.clearForm();
            view.setMaNV(dao.taoMaNVMoi());
        } else {
            JOptionPane.showMessageDialog(view, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        String ma = view.getMaNV();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn 1 dòng để sửa!");
            return;
        }

        String ten = view.getTenNV();
        if (!ten.isEmpty() && dao.checkTrungTenNVKhacMa(ten, ma)) {
            JOptionPane.showMessageDialog(view, "Tên nhân viên đã tồn tại!");
            return;
        }

        NhanVien nv = readForm(ma, true);
        if (nv == null) return;

        if (dao.existsEmail(nv.getEmail(), ma)) { JOptionPane.showMessageDialog(view, "Email đã tồn tại!"); return; }
        if (dao.existsSdt(nv.getSdt(), ma)) { JOptionPane.showMessageDialog(view, "SĐT đã tồn tại!"); return; }
        if (dao.existsUser(nv.getUser(), ma)) { JOptionPane.showMessageDialog(view, "User đã tồn tại!"); return; }

        int ok = dao.update(nv);
        if (ok > 0) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadTable();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int row = view.getTblNV().getSelectedRow();
        String ma = view.getMaNV();

        if (ma.isEmpty() && row >= 0) ma = String.valueOf(view.getModel().getValueAt(row, 0));
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn 1 dòng để xóa.");
            return;
        }

        int cf = JOptionPane.showConfirmDialog(view, "Xóa " + ma + " ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (cf != JOptionPane.YES_OPTION) return;

        int ok = dao.delete(ma);
        if (ok > 0) {
            JOptionPane.showMessageDialog(view, "Xóa thành công!");
            loadTable();
            view.clearForm();
            view.setMaNV(dao.taoMaNVMoi());
        } else {
            JOptionPane.showMessageDialog(view, "Nhân viên này đang được tham chiếu", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // CSV: MaNV,TenNV,QueQuan,GioiTinh,NamSinh,VaiTro,Email,Sdt,User,Pass
    private void importCSVToTable() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Chọn file CSV Nhân viên");
        if (fc.showOpenDialog(view) != JFileChooser.APPROVE_OPTION) return;

        List<NhanVien> dbList = dao.findAll();
        int insert = 0, skip = 0, dup = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; }
                if (line.trim().isEmpty()) continue;

                String[] p = line.split(",", -1);
                if (p.length < 10) { skip++; continue; }

                String ma = p[0].trim();
                String ten = p[1].trim();
                String que = p[2].trim();
                String gt = p[3].trim();
                String ns = p[4].trim();
                String vt = p[5].trim();
                String email = p[6].trim();
                String sdt = p[7].trim();
                String user = p[8].trim();
                String pass = p[9].trim();

                if (ma.isEmpty() || ten.isEmpty() || que.isEmpty() || gt.isEmpty() || ns.isEmpty() || vt.isEmpty()
                        || email.isEmpty() || sdt.isEmpty() || user.isEmpty() || pass.isEmpty()) { skip++; continue; }

                if (!isValidNamSinh(ns)) { skip++; continue; }
                if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$", email)) { skip++; continue; }
                if (!Pattern.matches("^\\d{10,11}$", sdt)) { skip++; continue; }
                if (pass.length() < 4) { skip++; continue; }

                boolean dupMa = false;
                for (NhanVien nv : dbList) if (nv.getMaNV().equals(ma)) { dupMa = true; break; }

                if (dupMa || dao.existsEmail(email, "") || dao.existsSdt(sdt, "") || dao.existsUser(user, "")) {
                    dup++; continue;
                }

                NhanVien nv = new NhanVien(ma, ten, que, gt, ns, vt, email, sdt, user, pass);
                dao.insert(nv);
                dbList.add(nv);
                insert++;
            }

            loadTable();
            view.clearForm();
            view.setMaNV(dao.taoMaNVMoi());

            JOptionPane.showMessageDialog(view,
                    "Import xong!\nThêm: " + insert + "\nBỏ qua: " + skip + "\nTrùng: " + dup);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi nhập file!");
        }
    }

    private void exportTableToCSV() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Chọn nơi lưu CSV Nhân viên");
        int choose = fc.showSaveDialog(view);
        if (choose != JFileChooser.APPROVE_OPTION) return;

        java.io.File file = fc.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new java.io.File(file.getAbsolutePath() + ".csv");
        }

        List<NhanVien> list = dao.findAll();

        try (java.io.PrintWriter pw = new java.io.PrintWriter(
                new java.io.OutputStreamWriter(new java.io.FileOutputStream(file), java.nio.charset.StandardCharsets.UTF_8))) {

            pw.print('\uFEFF');
            pw.println("MaNV,TenNV,QueQuan,GioiTinh,NamSinh,VaiTro,Email,Sdt,User,Pass");

            for (NhanVien nv : list) {
                pw.println(csv(nv.getMaNV()) + "," + csv(nv.getTenNV()) + "," + csv(nv.getQueQuan()) + "," +
                        csv(nv.getGioiTinh()) + "," + csv(nv.getNamSinh()) + "," + csv(nv.getVaiTro()) + "," +
                        csv(nv.getEmail()) + "," + csv(nv.getSdt()) + "," + csv(nv.getUser()) + "," + csv(nv.getPass()));
            }

            JOptionPane.showMessageDialog(view, "Xuất CSV thành công!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Xuất CSV thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String csv(String s) {
        if (s == null) s = "";
        if (s.contains(",") || s.contains("\"")) {
            s = s.replace("\"", "\"\"");
            s = "\"" + s + "\"";
        }
        return s;
    }
}
