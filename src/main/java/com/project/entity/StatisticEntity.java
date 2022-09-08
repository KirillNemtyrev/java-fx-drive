package com.project.entity;

import java.util.Date;

public class StatisticEntity {
    private String name;
    private String username;
    private String email;
    private String photo;

    private boolean subscribe;
    private Date subscribeEnd;

    private TicketStatisticEntity ticket;

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return "http://127.0.0.1:8745/resources/" + photo;
    }

    public TicketStatisticEntity getTicket() {
        return ticket;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public Date getSubscribeEnd() {
        return subscribeEnd;
    }
}
