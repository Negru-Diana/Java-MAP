package com.example.retea_de_socializare.controllers.customListView.customChatListView;

import com.example.retea_de_socializare.domain.Grup;
import com.example.retea_de_socializare.domain.Message;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;


public class MessageView extends HBox {
    private Message message;

    public MessageView(Message message, boolean isCurrentUser) {
        this.message = message;

        //Determin pozitionarea
        setAlignment(isCurrentUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        //Stilizare generala
        setPadding(new Insets(5));
        setSpacing(5);

        //Adaug mesajul intr-un VBox
        VBox messageBox = createMessageBox(message, isCurrentUser);
        getChildren().add(messageBox);
    }

    public Message getMessage() {
        return message;
    }

    private VBox createMessageBox(Message message, boolean isCurrentUser) {
        VBox box = new VBox();
        box.setPadding(new Insets(8));
        box.setSpacing(5);
        box.setStyle("-fx-background-color: " + (isCurrentUser ? "#efc1fb" : "#c8adf6") + "; -fx-background-radius: 8;");

        //Nume expeditor (pentru grupuri)
        if(message.getTo() instanceof Grup){
            Label senderName = new Label(isCurrentUser ? "You" : message.getFrom().getFullName());
            senderName.setFont(Font.font("Arial", FontPosture.ITALIC, 10));
            senderName.setTextFill(Color.BLACK);
            box.getChildren().add(senderName);
        }

        //Mesajul propriu-zis
        Label messageLabel = new Label(message.getMessage());
        messageLabel.setFont(Font.font("Arial", 14));
        messageLabel.setTextFill(Color.BLACK);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(200);
        box.getChildren().add(messageLabel);

        //Mesajul la care se face reply, daca exista
        if(message.getReply() != null && message.getReplyText() != null){
            Label replyLabel = new Label("Reply: " + message.getReplyText());
            replyLabel.setFont(Font.font("Arial", 12));
            replyLabel.setTextFill(Color.GRAY);
            replyLabel.setWrapText(true);
            replyLabel.setMaxWidth(200);
            box.getChildren().add(replyLabel);
        }

        //Data si ora mesajului
        Label timeLabel = new Label(message.getDataString());
        timeLabel.setFont(Font.font("Arial", 8));
        timeLabel.setTextFill(Color.GRAY);
        box.getChildren().add(timeLabel);

        return box;
    }
}
