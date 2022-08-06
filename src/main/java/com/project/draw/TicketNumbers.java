package com.project.draw;

import com.project.entity.TicketQuestionsEntity;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class TicketNumbers {

    private Pane pane;
    private Label label;

    public TicketNumbers() {}
    public TicketNumbers(Pane pane, Label label){

        this.pane = pane;
        this.label = label;

    }

    public Label getLabel() {
        return label;
    }
}
