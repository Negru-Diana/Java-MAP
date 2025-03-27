package com.example.retea_de_socializare.utils.dto;

import java.util.Optional;

public class FilterDTO {
    private Optional<Long> userId = Optional.empty(); //id-ul unui utilizator
    private Optional<String> searchText = Optional.empty(); //cautare dupa nume.username
    private Optional<String> filterType = Optional.empty(); //tipul de cautare (ex: "friends", "possible friends" etc.)
    private Optional<Boolean> received = Optional.empty(); // pentru cererile de prietenie, daca au fost primite (received =  true) sau trimise (received = false)

    public Optional<Long> getUserId() {
        return userId;
    }

    public void setUserId(Optional<Long> userId) {
        this.userId = userId;
    }

    public Optional<String> getSearchText() {
        return searchText;
    }

    public void setSearchText(Optional<String> searchText) {
        this.searchText = searchText;
    }

    public Optional<String> getFilterType() {
        return filterType;
    }

    public void setFilterType(Optional<String> filterType) {
        this.filterType = filterType;
    }

    public Optional<Boolean> getReceived() {
        return received;
    }

    public void setReceived(Optional<Boolean> received) {
        this.received = received;
    }
}
