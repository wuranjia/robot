package com.ichat4j.wrj.robot.tuling.entity;

public class InputText {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public InputText addText(String text) {
        this.text = text;
        return this;
    }
}
