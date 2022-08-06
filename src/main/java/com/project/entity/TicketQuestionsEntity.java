package com.project.entity;

public class TicketQuestionsEntity {
    private String text;
    private String photo;

    private TicketAnswersEntity[] answers;

    public String getText() {
        return text;
    }

    public String getPhoto() {
        return photo;
    }

    public TicketAnswersEntity[] getAnswers() {
        return answers;
    }
    //private Long number;
}
