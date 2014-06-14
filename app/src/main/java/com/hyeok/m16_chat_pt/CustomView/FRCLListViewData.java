package com.hyeok.m16_chat_pt.CustomView;

public class FRCLListViewData {
    private String name;
    private String status;
    private boolean is_online;

    public FRCLListViewData(String name, String status, boolean is_online) {
        this.name = name;
        this.status = status;
        this.is_online = is_online;
    }

    public String getname() {
        return name;
    }

    public String getstatus() {
        return status;
    }

    public boolean getis_online() {
        return is_online;
    }
}
