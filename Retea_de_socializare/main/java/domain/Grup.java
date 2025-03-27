package com.example.retea_de_socializare.domain;

import java.util.List;

public class Grup extends Entity<Long>{

    private String numeGrup; //Numele grupului
    private List<Utilizator> membri; //Lista cu toti utilizatorii care apartin grupului

    public Grup(String numeGrup) {
        this.numeGrup = numeGrup;
    }


    public String getNumeGrup() {
        return numeGrup;
    }

    public void setNumeGrup(String numeGrup) {
        this.numeGrup = numeGrup;
    }

    public List<Utilizator> getMembri() {
        return membri;
    }

    public void setMembri(List<Utilizator> membri) {
        this.membri = membri;
    }
}
