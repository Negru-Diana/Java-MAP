package com.example.retea_de_socializare.controllers;

import com.example.retea_de_socializare.controllers.customListView.customMembriGrupListView.CustomUserCell;
import com.example.retea_de_socializare.controllers.customListView.customMembriGrupListView.UtilizatorModel;
import com.example.retea_de_socializare.domain.Utilizator;
import com.example.retea_de_socializare.service.ObservableService;
import com.example.retea_de_socializare.utils.events.EntityChangeEvent;
import com.example.retea_de_socializare.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateGroupController implements Observer<EntityChangeEvent> {

    private ObservableService service;
    private Stage stage;
    private String username;


    @FXML
    private ListView<UtilizatorModel> groupMembersListViewCheckBox;

    @FXML
    private TextField numeGrupTextField;

    @FXML
    private RadioButton createGroupRadioButton;

    @FXML
    private RadioButton modifyGroupRadioButton;

    @FXML
    private Button createGroupButton;

    @FXML
    private Button modifyGroupButton;

    @FXML
    private Button deleteGroupButton;



    private ObservableList<UtilizatorModel> utilizatorModels;
    private ObservableList<Utilizator> friends;

    private ToggleGroup toggleGroup;


    @FXML
    public void initView() {
        //Creez un ToggleGroup pentru RadioButtons
        toggleGroup = new ToggleGroup();

        //Setez ToggleGroup pentru RadioButtons
        createGroupRadioButton.setToggleGroup(toggleGroup);
        modifyGroupRadioButton.setToggleGroup(toggleGroup);

        //!!!!!!!
        createGroupRadioButton.setVisible(false);
        modifyGroupRadioButton.setVisible(false);

        createGroupRadioButton.setSelected(true); //Setez ca implicit createGroupRadioButton

        //Setez vizibilitatea butoanelor
        createGroupButton.setVisible(true);
        modifyGroupButton.setVisible(false);
        deleteGroupButton.setVisible(false);


        //Lista originala de prieteni
        friends = FXCollections.observableArrayList(service.getAllFriends(username));

        //Convertesc in lista de UtilizatorModel
        utilizatorModels = friends.stream()
                .map(UtilizatorModel::new)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));


        //Setez elementele in ListView
        groupMembersListViewCheckBox.setItems(utilizatorModels);

        //Personalizez ListView-ul
        groupMembersListViewCheckBox.setCellFactory(listView -> new CustomUserCell());

        stage.setOnCloseRequest(event -> service.removeObserver(this));
    }

    private void clearField(){
        numeGrupTextField.clear();
    }


    @FXML
    private void handleCreateGroupButton(){
        String numeGrup = numeGrupTextField.getText();
        if(numeGrup.isEmpty()){
            MessageAlert.showErrorMessage(stage,"Grupul trebuie sa aiba un nume!");
        }
        else {
            try{
                List<Utilizator> groupMembers = getSelectedUsers();
                groupMembers.add(service.findByUsername(username));
                service.createGroup(numeGrup,groupMembers);
                clearField();
                resetCheckBox();
                MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION,"Create group","Grupul a fost creat cu succes!");
            }
            catch(Exception e){
                MessageAlert.showErrorMessage(stage,e.getMessage());
            }
        }
    }


    private List<Utilizator> getSelectedUsers() {
        return groupMembersListViewCheckBox.getItems().stream()
                .filter(UtilizatorModel::isSelected)  // Filtrăm utilizatorii selectați
                .map(UtilizatorModel::getUtilizator)  // Obținem utilizatorii
                .collect(Collectors.toList());
    }


    private void resetCheckBox() {
        for (UtilizatorModel model : utilizatorModels) {
            model.setSelected(false);  // Deselecteaza checkbox-urile
        }

        groupMembersListViewCheckBox.refresh();
    }



    @Override
    public void update(EntityChangeEvent event) {
        ObservableList<Utilizator> updatedFriends  = FXCollections.observableArrayList(service.getAllFriends(username));

        friends.setAll(updatedFriends);

        utilizatorModels.setAll(updatedFriends.stream()
                .map(UtilizatorModel::new)
                .collect(Collectors.toList()));


    }

    public void setService(ObservableService service) {
        this.service = service;

        service.addObserver(this);

        initView();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
