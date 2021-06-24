package com.shashank.managemembers.models;

public class history {
    private final String name;
    private final String mobile;
    private final String order;
    private final String time;
    private final String date;
//    private final String memberCode;

    public history(String name, String mobile, String order, String time, String date/*, String memberCode*/) {
        this.name = name;
        this.mobile = mobile;
        this.order = order;
        this.time = time;
        this.date = date;
//        this.memberCode = memberCode;
    }

    public String getNameHistory() {
        return name;
    }

    public String getMobileHistory() {
        return mobile;
    }

    public String getOrderHistory() {
        return order;
    }

    public String getTimeHistory() {
        return time;
    }

    public String getDateHistory() {
        return date;
    }

//    public String getMemberCode() {
//        return memberCode;
//    }
//
}
