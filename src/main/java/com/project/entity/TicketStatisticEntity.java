package com.project.entity;

public class TicketStatisticEntity {
    private Long total = 0L;
    private Long resolved = 0L;
    private Long undelivered = 0L;
    private Long unresolved = 0L;
    private Float probability = 0F;

    public Long getTotal() {
        return total;
    }

    public Long getResolved() {
        return resolved;
    }

    public Long getUndelivered() {
        return undelivered;
    }

    public Long getUnresolved() {
        return unresolved;
    }

    public Float getProbability() {
        return probability;
    }
}
