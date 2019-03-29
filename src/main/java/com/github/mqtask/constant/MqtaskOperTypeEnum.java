package com.github.mqtask.constant;

public enum MqtaskOperTypeEnum {
    STOP("stop"),
    START("start"),
    ;

    private String value;

    private MqtaskOperTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
