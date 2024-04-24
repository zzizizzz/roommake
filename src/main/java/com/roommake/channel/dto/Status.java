package com.roommake.channel.dto;

import lombok.Getter;

@Getter
public enum Status {
    BLOCK("block"), DELETE("delete");

    private final String status;

    private Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
