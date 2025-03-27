package com.example.retea_de_socializare.utils.observer;

import com.example.retea_de_socializare.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E event);
}
