package com.example.retea_de_socializare.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Prietenie extends Entity<Long>{

    private Utilizator utilizator1;
    private Utilizator utilizator2;
    private LocalDateTime friendsfrom;

    // Pentru incarcarea prieteniilor din BD cu frindsfrom corecta
    public Prietenie(Utilizator ut1, Utilizator ut2, LocalDateTime friendsfrom) {
        this.utilizator1 = ut1;
        this.utilizator2 = ut2;
        this.friendsfrom = friendsfrom;
    }

    // Pentru crearea unei noi prietenii unde friendsfrom este ora si data curenta
    public Prietenie(Utilizator ut1, Utilizator ut2) {
        this.utilizator1 = ut1;
        this.utilizator2 = ut2;
        this.friendsfrom = LocalDateTime.now();
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

    public LocalDateTime getFriendsfrom() {
        return friendsfrom;
    }

    public void setFriendsfrom(LocalDateTime friendsfrom) {
        this.friendsfrom = friendsfrom;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Prietenie)) return false;
        Prietenie other = (Prietenie) obj;
        return (utilizator1.equals(other.utilizator1) && utilizator2.equals(other.utilizator2)) ||
                (utilizator1.equals(other.utilizator2) && utilizator2.equals(other.utilizator1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(utilizator1, utilizator2);
    }

}

