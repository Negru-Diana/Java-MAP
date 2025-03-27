package com.example.retea_de_socializare.controllers.customListView.customMembriGrupListView;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class CustomUserCell extends ListCell<UtilizatorModel> {

    @Override
    protected void updateItem(UtilizatorModel utilizatorModel, boolean b) {
        super.updateItem(utilizatorModel, b);

        if(b || utilizatorModel == null) {
            setText(null);
            setGraphic(null);
        }
        else{
            //CheckBox-ul pentru selectarea utilizatorului
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().bindBidirectional(utilizatorModel.selectedProperty());

            //Username-ul utilizatorului
            Text usernameText = new Text(utilizatorModel.getUtilizator().getUserName());

            //Numele complet plasat intre paranteze ()
            Text fullName = new Text("(" + utilizatorModel.getUtilizator().getFullName() + ")");

            //Aranjarea elementelor intr-un HBox
            HBox container = new HBox(10); //Spatierea intre elemente
            container.getChildren().addAll(checkBox, usernameText, fullName);

            setGraphic(container); //Setez grafica personalizata
        }
    }
}
