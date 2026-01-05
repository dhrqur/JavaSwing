package QLTV.Controller;

import QLTV.Domain.*;
import QLTV.Model.DBConnection;
import QLTV.Model.MuonTraDAO;
import QLTV.Views.FormMuonTra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import QLTV.Model.TheThuVienDAO;


public class MuonTraController {

    private final TheThuVienDAO theDAO = new TheThuVienDAO();
    private final FormMuonTra view;
    private final MuonTraDAO dao = new MuonTraDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public MuonTraController(FormMuonTra view) {
        this.view = view;

        // Load dữ liệu ban đầu
        loadPhieuTable();
        loadSachTable();           // ← Quan trọng: load sách ngay
        loadDocGiaCombo();

        view.getTxtMaPhieu().setText(dao.taoMaMTMoi());
        view.getTxtMaNV().setText("NV01");

        registerEvents();
    }

    private void registerEvents() {
        view.getBtnGiaHan().addActionListener(e -> handleGiaHan());

        view.getCboDocGia().addActionListener(e -> onDocGiaSelected());
        
        view.getBtnSearch().addActionListener(e -> searchPhieu());
        view.getTxtSearch().addActionListener(e -> searchPhieu());

        view.getBtnSearchSach().addActionListener(e -> searchSach());
        view.getTxtSearchSach().addActionListener(e -> searchSach());

        view.getBtnThemPhieu().addActionListener(e -> handleInsertPhieu());
        view.getBtnCapNhatPhieu().addActionListener(e -> handleUpdatePhieu());
        view.getBtnXoaPhieu().addActionListener(e -> handleDeletePhieu());
        view.getBtnLamMoiPhieu().addActionListener(e -> clearAll());

        view.getBtnThemChiTiet().addActionListener(e -> handleAddChiTiet());
        view.getBtnXoaChiTiet().addActionListener(e -> handleDeleteChiTiet());

        view.getTblPhieu().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromPhieuSelected();
        });

        view.getTblSach().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillMaSachFromSelected();
        });
        
        // Listener cập nhật sách đã chọn khi chọn trong bảng
        view.getTblSach().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                capNhatSachDaChon();
            }
        });
    }
    private void capNhatSachDaChon() {
        JComboBox<String> combo = view.getCboSachDaChon();
        combo.removeAllItems();

        int[] selectedRows = view.getTblSach().getSelectedRows();
        for (int row : selectedRows) {
            String maSach = view.getModelSach().getValueAt(row, 0).toString();
            String tenSach = view.getModelSach().getValueAt(row, 1).toString();
            String theLoai = view.getModelSach().getValueAt(row, 2).toString();
            int soLuongCon = (Integer) view.getModelSach().getValueAt(row, 3);

            combo.addItem(maSach + " - " + tenSach + " (" + theLoai + ", Còn: " + soLuongCon + ")");
        }

        // Nếu có sách được chọn, mở combo để thấy danh sách
        if (selectedRows.length > 0) {
            combo.setPopupVisible(true);
            combo.setPopupVisible(false);
        }
    }
    private void loadPhieuTable() {
        List<MuonTra> list = dao.findAll();
        fillPhieuTable(list);
    }

    private void fillPhieuTable(List<MuonTra> list) {
        DefaultTableModel m = view.getModelPhieu();
        m.setRowCount(0);
        for (MuonTra mt : list) {
            m.addRow(new Object[]{
                mt.getMaMT(),
                mt.getMaDG(),
                mt.getTenDG(),
                mt.getMaNV(),
                sdf.format(mt.getNgayMuon()),
                sdf.format(mt.getHanTra()),
                mt.getTrangThai()
            });
        }
    }

    private void searchPhieu() {
        String key = view.getTxtSearch().getText().trim();
        if (key.isEmpty()) {
            loadPhieuTable();
        } else {
            // Tạm thời load tất cả (có thể mở rộng sau)
            loadPhieuTable();
        }
    }

    private void loadDocGiaCombo() {
        try {
            List<DocGiaMuon> list = dao.getAllDocGiaForCombo();
            view.getCboDocGia().removeAllItems();
            for (DocGiaMuon dg : list) {
                view.getCboDocGia().addItem(dg.getMaDG() + " - " + dg.getTenDG());
            }
            System.out.println("Load combo độc giả: " + list.size() + " độc giả");
        } catch (Exception ex) {
            System.err.println("LỖI LOAD COMBO ĐỘC GIẢ: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // ================== SÁCH ==================
    private void loadSachTable() {
        searchSach("");  // Load tất cả khi mở form
    }

    private void searchSach() {
        searchSach(view.getTxtSearchSach().getText().trim());
    }

    private void searchSach(String keyword) {
        try {
            List<SachMuon> list = dao.searchSach(keyword);
            DefaultTableModel m = view.getModelSach();
            m.setRowCount(0);
            for (SachMuon s : list) {
                m.addRow(new Object[]{
                    s.getMaSach(),
                    s.getTenSach(),
                    s.getTheLoai(),
                    s.getSoLuongCon()
                });
            }
            System.out.println("=== LOAD BẢNG SÁCH ===");
            System.out.println("Từ khóa: '" + keyword + "'");
            System.out.println("Tìm thấy: " + list.size() + " sách");
            if (list.isEmpty()) {
                System.out.println("→ Bảng sách trống! Kiểm tra:");
                System.out.println("  1. Bảng 'sach' trong database có dữ liệu chưa?");
                System.out.println("  2. Tên cột có đúng: MaSach, TenSach, TheLoai, SoLuong?");
            }
        } catch (Exception ex) {
            System.err.println("LỖI LOAD BẢNG SÁCH: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi kết nối database khi load sách!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillMaSachFromSelected() {
        int row = view.getTblSach().getSelectedRow();
        if (row >= 0) {
            String maSach = view.getModelSach().getValueAt(row, 0).toString();
            view.getTxtMaSach().setText(maSach);
        }
    }

    private void fillFormFromPhieuSelected() {
        int row = view.getTblPhieu().getSelectedRow();
        if (row < 0) return;

        DefaultTableModel m = view.getModelPhieu();
        String maMT = m.getValueAt(row, 0).toString();
        String maDG = m.getValueAt(row, 1).toString();

        view.getTxtMaPhieu().setText(maMT);
        view.getTxtMaNV().setText(m.getValueAt(row, 3).toString());

        // Chọn độc giả trong combo
        for (int i = 0; i < view.getCboDocGia().getItemCount(); i++) {
            String item = view.getCboDocGia().getItemAt(i);
            if (item.startsWith(maDG + " - ")) {
                view.getCboDocGia().setSelectedIndex(i);
                break;
            }
        }

        try {
            view.getDcNgayMuon().setDate(sdf.parse(m.getValueAt(row, 4).toString()));
            view.getDcNgayTraDK().setDate(sdf.parse(m.getValueAt(row, 5).toString()));
        } catch (Exception ignored) {}

        loadChiTietTable(maMT);
    }

    private void loadChiTietTable(String maMT) {
        List<ChiTietMuonTra> list = dao.getChiTietByMaMT(maMT);
        DefaultTableModel m = view.getModelChiTiet();
        m.setRowCount(0);
        for (ChiTietMuonTra ct : list) {
            m.addRow(new Object[]{ct.getMaSach(), ct.getTenSach(), ct.getSoLuong(), ct.getGhiChu()});
        }
    }

    private MuonTra readPhieuForm() {
        String selected = (String) view.getCboDocGia().getSelectedItem();
        if (selected == null || selected.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn độc giả!");
            return null;
        }
        String maDG = selected.split(" - ")[0].trim();

        String maNV = view.getTxtMaNV().getText().trim();
        Date ngayMuon = view.getDcNgayMuon().getDate();
        Date hanTra = view.getDcNgayTraDK().getDate();

        if (maDG.isEmpty() || maNV.isEmpty() || ngayMuon == null || hanTra == null) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin phiếu!");
            return null;
        }
        if (hanTra.before(ngayMuon)) {
            JOptionPane.showMessageDialog(view, "Hạn trả phải sau ngày mượn!");
            return null;
        }

        return new MuonTra(null, maDG, null, maNV, ngayMuon, hanTra, "Chưa trả", view.getTxtGhiChu().getText());
    }

    private void handleInsertPhieu() {
        MuonTra mt = readPhieuForm();
        if (mt == null) return;

        // Tạo mã phiếu mới
        String maMT = dao.taoMaMTMoi();
        mt.setMaMT(maMT);

        // 1️⃣ Thêm phiếu mượn
        if (dao.insertMuonTra(mt) <= 0) {
            JOptionPane.showMessageDialog(view, "Thêm phiếu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2️⃣ Lấy số lượng chung từ spinner
        int soLuong = (int) view.getSpSoLuong().getValue();

        // 3️⃣ Lấy danh sách sách đã chọn từ JTable
        DefaultTableModel modelSach = view.getModelSach();
        int[] selectedRows = view.getTblSach().getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(view, "Chưa chọn sách để thêm chi tiết!");
            return;
        }

        boolean allInserted = true;

        for (int row : selectedRows) {
            String maSach = modelSach.getValueAt(row, 0).toString();
            String tenSach = modelSach.getValueAt(row, 1).toString();

            ChiTietMuonTra ct = new ChiTietMuonTra(maMT, maSach, tenSach, soLuong, "");

            if (dao.insertChiTiet(ct) <= 0) {
                allInserted = false;
                System.err.println("Không insert được chi tiết cho sách: " + maSach);
            }
        }

        // 4️⃣ Load lại bảng và reset form
        loadPhieuTable();
        clearAll();

        if (allInserted) {
            JOptionPane.showMessageDialog(view, "Thêm phiếu và chi tiết thành công!");
        } else {
            JOptionPane.showMessageDialog(view, "Phiếu đã thêm nhưng một số chi tiết không thêm được!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void handleUpdatePhieu() {
        String maMT = view.getTxtMaPhieu().getText().trim();
        if (maMT.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn phiếu để cập nhật!");
            return;
        }
        MuonTra mt = readPhieuForm();
        if (mt == null) return;
        mt.setMaMT(maMT);

        if (dao.updateMuonTra(mt) > 0) {
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
            loadPhieuTable();
        } else {
            JOptionPane.showMessageDialog(view, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeletePhieu() {
        String maMT = view.getTxtMaPhieu().getText().trim();
        if (maMT.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn phiếu để xóa!");
            return;
        }
        int cf = JOptionPane.showConfirmDialog(view, "Xóa phiếu " + maMT + " và tất cả chi tiết?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (cf == JOptionPane.YES_OPTION && dao.deleteMuonTra(maMT) > 0) {
            JOptionPane.showMessageDialog(view, "Xóa thành công!");
            loadPhieuTable();
            clearAll();
        }
    }

    private void handleAddChiTiet() {
        String maMT = view.getTxtMaPhieu().getText().trim();
        if (maMT.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chưa có mã phiếu mượn!");
            return;
        }
        String maSach = view.getTxtMaSach().getText().trim();
        int soLuong = (int) view.getSpSoLuong().getValue();

        if (maSach.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nhập mã sách!");
            return;
        }
        if (soLuong <= 0) {
            JOptionPane.showMessageDialog(view, "Số lượng phải lớn hơn 0!");
            return;
        }

        Integer conLai = dao.getSoLuongConLai(maSach);
        if (conLai == null) {
            JOptionPane.showMessageDialog(view, "Mã sách không tồn tại!");
            return;
        }
        if (soLuong > conLai) {
            JOptionPane.showMessageDialog(view, "Chỉ còn " + conLai + " cuốn!");
            return;
        }

        String tenSach = dao.getTenSach(maSach);
        ChiTietMuonTra ct = new ChiTietMuonTra(maMT, maSach, tenSach, soLuong, "");

        if (dao.insertChiTiet(ct) > 0) {
            JOptionPane.showMessageDialog(view, "Thêm chi tiết thành công!");
            loadChiTietTable(maMT);
            view.clearChiTietForm();
            view.getSpSoLuong().setValue(1); // reset spinner
        } else {
            JOptionPane.showMessageDialog(view, "Thêm chi tiết thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteChiTiet() {
        int row = view.getTblChiTiet().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Chọn dòng chi tiết để xóa!");
            return;
        }
        String maMT = view.getTxtMaPhieu().getText().trim();
        String maSach = view.getModelChiTiet().getValueAt(row, 0).toString();

        if (dao.deleteChiTiet(maMT, maSach) > 0) {
            JOptionPane.showMessageDialog(view, "Xóa chi tiết thành công!");
            loadChiTietTable(maMT);
        } else {
            JOptionPane.showMessageDialog(view, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void onDocGiaSelected() {
        Object selected = view.getCboDocGia().getSelectedItem();
        if (selected == null) {
            view.getTxtMaThe().setText("");
            setPhieuButtonsEnabled(false);
            return;
        }

        String maDG = selected.toString().split(" - ")[0].trim();

        TheThuVien the = theDAO.findByMaDG(maDG);

        // ❌ Chưa có thẻ
        if (the == null) {
            view.getTxtMaThe().setText("");
            JOptionPane.showMessageDialog(view,
                    "Độc giả chưa có thẻ thư viện!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            setPhieuButtonsEnabled(false);
            return;
        }

        // Hiển thị mã thẻ
        view.getTxtMaThe().setText(the.getMaThe());

        // ❌ Thẻ không hợp lệ
        if (!theDAO.isTheConHan(the)) {
            JOptionPane.showMessageDialog(view,
                    "Thẻ thư viện đã hết hạn hoặc bị khóa!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            setPhieuButtonsEnabled(false);
            return;
        }

        // ✅ OK
        setPhieuButtonsEnabled(true);
    }
    private void handleGiaHan() {
        int row = view.getTblPhieu().getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn phiếu để gia hạn!");
            return;
        }

        DefaultTableModel m = view.getModelPhieu();
        String maMT = m.getValueAt(row, 0).toString();
        String hanTraStr = m.getValueAt(row, 5).toString();

        try {
            // Chuyển từ String sang Date
            Date hanTra = sdf.parse(hanTraStr);

            // Cộng thêm 10 ngày
            long newTime = hanTra.getTime() + 10L * 24 * 60 * 60 * 1000; // 10 ngày
            Date newHanTra = new Date(newTime);

            // Cập nhật vào DB
            if (dao.updateHanTra(maMT, newHanTra) > 0) {
                JOptionPane.showMessageDialog(view, "Gia hạn thành công! Hạn trả mới: " + sdf.format(newHanTra));
                loadPhieuTable(); // reload bảng phiếu
            } else {
                JOptionPane.showMessageDialog(view, "Gia hạn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi gia hạn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setPhieuButtonsEnabled(boolean enabled) {
        view.getBtnThemPhieu().setEnabled(enabled);
        view.getBtnCapNhatPhieu().setEnabled(enabled);
        view.getBtnGiaHan().setEnabled(enabled);
    }

    private void clearAll() {
        view.clearPhieuForm();
        view.clearChiTietForm();
        view.getModelChiTiet().setRowCount(0);
        view.getTxtMaPhieu().setText(dao.taoMaMTMoi());
        view.getCboDocGia().setSelectedIndex(-1);
        view.getTxtMaThe().setText("");          // 🔥 thêm
        setPhieuButtonsEnabled(false);           // 🔥 thêm
        view.getSpSoLuong().setValue(1);
    }

}