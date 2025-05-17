package model;

public class Employee {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String cccd;
    private String role;
    private String startDate;
    private int workedDays;
    private double salary;
    private boolean isdeleted;

    // Getters và setters (có thể dùng Lombok nếu muốn gọn hơn)
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

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public int getWorkedDays() { return workedDays; }
    public void setWorkedDays(int workedDays) { this.workedDays = workedDays; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public boolean isIsdeleted() { return isdeleted; }
    public void setIsdeleted(boolean isdeleted) { this.isdeleted = isdeleted; }
}
