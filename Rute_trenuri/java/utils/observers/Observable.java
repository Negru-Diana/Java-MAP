package com.example.examen_db.utils.observers;

import javafx.util.Pair;

public interface Observable{
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(int cate, Pair<String, String> filterPair);
}
