package com.example.retea_de_socializare.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Message extends Entity<Long> {

    private Utilizator from; //Utilizatorul care trimite mesajul
    private Entity to; //Utilizatorul care primeste mesajul
    private String message; //Mesajul trimis
    private LocalDateTime data; //Data si ora la care s-a trimis mesajul
    private Long reply; //Id-ul mesajului la care se raspunde
    private String replyText; //Mesajul la care se da reply

    //Contructor pentru mesaj nou
    public Message(Utilizator from, Entity to, String message, Long reply) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = LocalDateTime.now();
        this.reply = reply;
        this.replyText = null;
    }


    public Utilizator getFrom() {
        return from;
    }

    public void setFrom(Utilizator from) {
        this.from = from;
    }

    public Entity getTo() {
        return to;
    }

    public void setTo(Entity to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getDataString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM  HH:mm");
        String dataString = data.format(formatter);

        return dataString;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Long getReply() {
        return reply;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }
}
