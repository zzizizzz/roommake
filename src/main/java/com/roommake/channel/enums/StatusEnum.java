package com.roommake.channel.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {
    BLOCK("block"), DELETE("delete");

    private final String status;

    private StatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
