package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Table {
    private int id;

    @JsonProperty("tabNum")
    private int tabNum;

    @JsonProperty("tabStatus")
    private boolean tabStatus;

    @JsonProperty("isdeleted")
    private boolean isDeleted;

    public Table() {} // Required for Jackson

    public Table(int id, int tabNum, boolean tabStatus, boolean isDeleted) {
        this.id = id;
        this.tabNum = tabNum;
        this.tabStatus = tabStatus;
        this.isDeleted = isDeleted;
    }

    public int getId() {
        return id;
    }

    public int getTabNum() {
        return tabNum;
    }

    public boolean isTabStatus() {
        return tabStatus;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
    public boolean isActive() {
        return tabStatus; // nếu active = tabStatus
    }

    public boolean isBooked() {
        return false; // hoặc ánh xạ thêm nếu có field riêng
    }
}
