package com.example.retea_de_socializare.utils.observer;

import com.example.retea_de_socializare.utils.events.Event;

public interface Observable<E extends Event>{
    void addObserver(Observer<E> observer);
    void removeObserver(Observer<E> observer);
    void notifyObservers(E event);
}
