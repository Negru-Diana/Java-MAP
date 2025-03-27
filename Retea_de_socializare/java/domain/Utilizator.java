package com.example.retea_de_socializare.domain;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Utilizator  extends Entity<Long>{

    private String firstName;
    private String lastName;
    private String userName;
    private String parola;
    private ArrayList<Utilizator> friends;

    public Utilizator(String firstName, String lastName, String userName, String parola) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.parola = parola;
        this.friends = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() { return userName; }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) { this.userName = userName; }

    public ArrayList<Utilizator> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Utilizator> friends) {
        this.friends = friends;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        //Java 8 features
        String friendsList = friends.stream()
                .map(friend -> friend.getFirstName() + " " + friend.getLastName())
                .collect(Collectors.joining(", ", "", ""));

        return "Utilizator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", parola='" + parola + '\'' +
                ", friends=" + (friendsList.isEmpty() ? "fara prieteni" : friendsList) +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(!(obj instanceof Utilizator)){
            return false;
        }
        Utilizator that = (Utilizator) obj;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getUserName().equals(that.getUserName()) &&
                getParola().equals(that.getParola());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }
}
