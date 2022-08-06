package com.project.entity;

public class TicketWithAnswersEntity {

    private String text;
    private String photo;
    private String explanation;
    private Long answer;
    private Long correct;
    private TicketAnswersEntity[] answers;

    public String getText() {
        return text;
    }

    public String getPhoto() {
        return photo;
    }

    public String getExplanation() {
        return explanation;
    }

    public Long getAnswer() {
        return answer;
    }

    public Long getCorrect() {
        return correct;
    }

    public TicketAnswersEntity[] getAnswers() {
        return answers;
    }
}
