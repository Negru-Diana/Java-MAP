package com.example.retea_de_socializare.controllers.customListView.customMembriGrupListView;

import com.example.retea_de_socializare.domain.Utilizator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class UtilizatorModel {
    private Utilizator utilizator;
    private BooleanProperty selected; //Legatura cu CheckBox-ul

    public UtilizatorModel(Utilizator utilizator) {
        this.utilizator = utilizator;
        this.selected = new SimpleBooleanProperty(false); //CheckBox-ul este nebifat initial
    }

    public Utilizator getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

}
