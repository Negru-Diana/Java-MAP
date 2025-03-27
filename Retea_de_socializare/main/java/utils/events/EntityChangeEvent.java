package com.example.retea_de_socializare.utils.events;

import com.example.retea_de_socializare.domain.Entity;
import com.example.retea_de_socializare.domain.Utilizator;

public class EntityChangeEvent extends jdk.jfr.Event implements Event {
    private ChangeEventType type;
    private Utilizator data;
    private Utilizator oldData;

    // Constructor pentru ADD sau DELETE
    public EntityChangeEvent(ChangeEventType type, Utilizator data) {
        this.type = type;
        this.data = data;
        this.oldData = null;
    }

    // Constructor pentru UPDATE
    public EntityChangeEvent(ChangeEventType type, Utilizator data, Utilizator oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Utilizator getData() {
        return data;
    }

    public Utilizator getOldData() {
        return oldData;
    }
}
