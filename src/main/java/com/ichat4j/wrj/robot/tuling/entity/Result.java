package com.ichat4j.wrj.robot.tuling.entity;

public class Result {
    String groupType;
    String resultType;
    ResultValue values;

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public ResultValue getValues() {
        return values;
    }

    public void setValues(ResultValue values) {
        this.values = values;
    }
}
