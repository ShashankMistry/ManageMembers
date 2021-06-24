package com.shashank.managemembers.models;

import android.widget.SearchView;

public class members {
    private final String name;
    private final String mobile;
    private final String joinDate;
    private final String dob;
    private final String drinksList;
    private final String memberCode;

    public members(String memberCode, String name, String mobile, String joinDate, String dob, String drinksList) {
        this.name = name;
        this.mobile = mobile;
        this.joinDate = joinDate;
        this.drinksList = drinksList;
        this.memberCode = memberCode;
        this.dob = dob;
    }

    public String getDob() {
        return dob;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public String getDrinksList() {
        return drinksList;
    }

    public String getMemberCode() {
        return memberCode;
    }

}
