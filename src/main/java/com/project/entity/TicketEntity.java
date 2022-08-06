package com.project.entity;

import java.util.Date;

public class TicketEntity {
    private String uuid;
    private Date dateStart;
    private Date dateEnd;
    private TicketQuestionsEntity[] ticket;

    public String getUuid() {
        return uuid;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public TicketQuestionsEntity[] getTicket() {
        return ticket;
    }

    public TicketQuestionsEntity getTicket(int index){
        return ticket[index];
    }
}
