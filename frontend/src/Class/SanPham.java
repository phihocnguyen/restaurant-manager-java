package Class;

public class SanPham {
    private String maSP;
    private String tenSP;
    private String loaiSP;
    private double giaVon;
    private double giaBan;
    private int soLuongConLai;

    public SanPham(String maSP, String tenSP, String loaiSP, double giaVon, double giaBan, int soLuongConLai) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.loaiSP = loaiSP;
        this.giaVon = giaVon;
        this.giaBan = giaBan;
        this.soLuongConLai = soLuongConLai;
    }

    // Getters
    public String getMaSP() {
        return maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public String getLoaiSP() {
        return loaiSP;
    }

    public double getGiaVon() {
        return giaVon;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public int getSoLuongConLai() {
        return soLuongConLai;
    }

    // Setters
    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public void setLoaiSP(String loaiSP) {
        this.loaiSP = loaiSP;
    }

    public void setGiaVon(double giaVon) {
        this.giaVon = giaVon;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public void setSoLuongConLai(int soLuongConLai) {
        this.soLuongConLai = soLuongConLai;
    }
}
