package com.ichat4j.wrj.robot.domain;

public class ContactDto
{
    private String nickName;
    private String remarkName;
    private String checked;

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public ContactDto nickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public ContactDto remarkName(String remarkName) {
        this.remarkName = remarkName;
        return this;
    }

    public ContactDto check(String checked) {
        this.checked = checked;
        return this;
    }
}
