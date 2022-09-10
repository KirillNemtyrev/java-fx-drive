package com.project.entity;

import java.util.Date;

public class SubscribeEntity {

    private Long id;
    private Long userId;
    private String code;
    private Date date;
    private Long value;

    public String getCode() {
        return code;
    }

    public Long getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }
}
