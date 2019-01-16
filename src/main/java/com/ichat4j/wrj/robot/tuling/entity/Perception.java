package com.ichat4j.wrj.robot.tuling.entity;

public class Perception {
    private InputText inputText;
    private InputImage inputImage;
    private SelfInfo selfInfo;

    public Perception addInputText(String text){
        this.inputText = new InputText().addText(text);
        return this;
    }


    public InputText getInputText() {
        return inputText;
    }

    public void setInputText(InputText inputText) {
        this.inputText = inputText;
    }

    public InputImage getInputImage() {
        return inputImage;
    }

    public void setInputImage(InputImage inputImage) {
        this.inputImage = inputImage;
    }

    public SelfInfo getSelfInfo() {
        return selfInfo;
    }

    public void setSelfInfo(SelfInfo selfInfo) {
        this.selfInfo = selfInfo;
    }
}
