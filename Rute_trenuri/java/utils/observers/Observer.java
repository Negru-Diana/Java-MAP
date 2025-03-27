package com.example.examen_db.utils.observers;

import javafx.util.Pair;

public interface Observer {
    void update(int cate, Pair<String, String> filterPair);
}
