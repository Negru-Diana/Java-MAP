package com.example.retea_de_socializare.controllers.customComboBox;

import com.example.retea_de_socializare.domain.Grup;
import com.example.retea_de_socializare.domain.Utilizator;
import javafx.scene.control.ComboBox;

public class ComboBoxItem {

    private Object item;

    public ComboBoxItem(Object item) {
        this.item = item;
    }

    public Object getItem() {
        return item;
    }

    @Override
    public String toString() {
        if(item instanceof Utilizator){
            return ((Utilizator) item).getUserName();
        }
        else if(item instanceof Grup){
            return ((Grup) item).getNumeGrup();
        }
        return item.toString();
    }

}
