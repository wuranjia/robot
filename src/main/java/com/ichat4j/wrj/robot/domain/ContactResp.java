package com.ichat4j.wrj.robot.domain;

import java.util.List;

public class ContactResp {
    private String code;
    List<ContactDto> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ContactDto> getData() {
        return data;
    }

    public void setData(List<ContactDto> data) {
        this.data = data;
    }
}
