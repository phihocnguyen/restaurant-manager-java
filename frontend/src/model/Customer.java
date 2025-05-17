package model;
public class Customer {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String cccd;
    private String email;
    private Boolean isvip;
    private Boolean isdeleted;

    // Getters và setters (bắt buộc)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getIsvip() { return isvip; }
    public void setIsvip(Boolean isvip) { this.isvip = isvip; }

    public Boolean getIsdeleted() { return isdeleted; }
    public void setIsdeleted(Boolean isdeleted) { this.isdeleted = isdeleted; }
}
