package com.project.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class TicketHistoryEntity {

    private Long id;

    private String uuid;
    private Long userId;
    private Long correct;
    private Long attempts = 1L;
    private String ticket;

    private ArrayList<Long> answers;

    private String ticketStatus;
    private String ticketResultStatus;

    private Date ticketDateStart;
    private Date ticketDateEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCorrect() {
        return correct;
    }

    public Long getAttempts() {
        return attempts;
    }

    public String getTicket() {
        return ticket;
    }

    public ArrayList<Long> getAnswers() {
        return answers;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public String getTicketResultStatus() {
        return ticketResultStatus;
    }

    public Date getTicketDateStart() {
        return ticketDateStart;
    }

    public Date getTicketDateEnd() {
        return ticketDateEnd;
    }
}
