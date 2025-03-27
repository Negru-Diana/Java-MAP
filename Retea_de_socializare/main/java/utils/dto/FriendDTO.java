package com.example.retea_de_socializare.utils.dto;

import com.example.retea_de_socializare.domain.Utilizator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FriendDTO {

    private Utilizator prieten;
    private LocalDateTime friendsFrom;

    public FriendDTO(Utilizator utilizator, LocalDateTime friendsFrom) {
        this.prieten = utilizator;
        this.friendsFrom = friendsFrom;
    }

    public Utilizator getPrieten() {
        return prieten;
    }

    public void setPrieten(Utilizator prieten) {
        this.prieten = prieten;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public String getFriendsFromString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy   HH:mm:ss");
        String friendsFromString = friendsFrom.format(formatter);

        return friendsFromString;
    }

    public String getUsername() {
        return prieten.getUserName();
    }

    public String getFriendsFromDateString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String friendsFromDateString = friendsFrom.format(formatter);

        return friendsFromDateString;
    }
    
}
