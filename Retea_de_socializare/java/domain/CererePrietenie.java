package com.example.retea_de_socializare.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class CererePrietenie extends Entity<Long>{
    private Utilizator utilizator1; // Utilizatorul care a trimis cererea de prietenie
    private Utilizator utilizator2; // Utilizatorul care trebuie sa primeasca cererea de prietenie
    private LocalDateTime data; // Data si ora cand s-a trimis cererea de prietenie
    // Status cerere de prietenie: 0 - fara raspuns; 1 - acceptata; 2 - respinsa
    private String status; // Statusul prieteniei

    public CererePrietenie(Utilizator ut1, Utilizator ut2, LocalDateTime data, String status) {
        this.utilizator1 = ut1;
        this.utilizator2 = ut2;
        this.data = data;
        this.status = status;
    }

    public String getUtilizator1FullName(){
        return utilizator1.getFullName();
    }

    public String getUtilizator2FullName(){
        return utilizator2.getFullName();
    }

    public String getUtilizator1Username(){
        return utilizator1.getUserName();
    }

    public String getUtilizator2Username(){
        return utilizator2.getUserName();
    }

    public Utilizator getUtilizator1() {
        return utilizator1;
    }

    public void setUtilizator1(Utilizator utilizator1) {
        this.utilizator1 = utilizator1;
    }

    public Utilizator getUtilizator2() {
        return utilizator2;
    }

    public void setUtilizator2(Utilizator utilizator2) {
        this.utilizator2 = utilizator2;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getDataString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm");
        String dataString = data.format(formatter);

        return dataString;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CererePrietenie)) return false;
        CererePrietenie other = (CererePrietenie) obj;
        return (utilizator1.equals(other.utilizator1) && utilizator2.equals(other.utilizator2)) ||
                (utilizator2.equals(other.utilizator1) && utilizator1.equals(other.utilizator2));
    }

    @Override
    public int hashCode() {
        return Objects.hash(utilizator1, utilizator2);
    }
}

