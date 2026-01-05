package QLTV.Model;

import QLTV.Domain.NhanVien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DangnhapDAO {

    public NhanVien findByUserPass(String user, String pass) {
        String sql = "SELECT MaNV, TenNV, QueQuan, GioiTinh, NamSinh, VaiTro, Email, Sdt, User, Pass " +
                     "FROM nhanvien WHERE User=? AND Pass=? LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.trim());
            ps.setString(2, pass.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NhanVien(
                            rs.getString("MaNV"),
                            rs.getString("TenNV"),
                            rs.getString("QueQuan"),
                            rs.getString("GioiTinh"),
                            rs.getString("NamSinh"),
                            rs.getString("VaiTro"),
                            rs.getString("Email"),
                            rs.getString("Sdt"),
                            rs.getString("User"),
                            rs.getString("Pass")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
