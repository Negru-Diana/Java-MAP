package com.example.retea_de_socializare.controllers;

import com.example.retea_de_socializare.controllers.customComboBox.ComboBoxItem;
import com.example.retea_de_socializare.controllers.customListView.customChatListView.MessageView;
import com.example.retea_de_socializare.domain.*;
import com.example.retea_de_socializare.service.ObservableService;
import com.example.retea_de_socializare.utils.dto.FilterDTO;
import com.example.retea_de_socializare.utils.dto.FriendDTO;
import com.example.retea_de_socializare.utils.events.ChangeEventType;
import com.example.retea_de_socializare.utils.events.EntityChangeEvent;
import com.example.retea_de_socializare.utils.observer.Observer;
import com.example.retea_de_socializare.utils.paging.Page;
import com.example.retea_de_socializare.utils.paging.Pageable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ReteaDeSocializareController implements Observer<EntityChangeEvent> {

    private ObservableService service;
    private Stage stage;
    private String username;


    @FXML
    private TabPane socializareTabPane;


    /*
    TAB: MY ACCOUNT
     */

    @FXML
    private Tab myAccountTab;

    @FXML
    private TextField prenumeField;

    @FXML
    private TextField numeField;

    @FXML
    private TextField usernameField;

    private void initMyAccountTab() {
        Utilizator user = service.findByUsername(username);

        if (user != null) {
            prenumeField.setText(user.getFirstName());
            numeField.setText(user.getLastName());
            usernameField.setText(user.getUserName());
        }
        else{
            MessageAlert.showErrorMessage(stage, "A aparut o eroare la incarcarea datelor contului!");
        }

    }


    @FXML
    private void handleLogOutButton(){
        onWindowClose();
    }

    private void onWindowClose(){
        service.removeActivUser(username);
        service.removeObserver(this);
        stage.close();
    }

    private void openLogin(){
        try{
            // Incarc fisierul FXML pentru LoginController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/retea_de_socializare/views/login.fxml"));
            Parent root = loader.load();

            // Adaug fisierul CSS
            root.getStylesheets().add(getClass().getResource("/com/example/retea_de_socializare/css/viewStyle.css").toExternalForm());

            // Obtin instanta controller-ului
            LoginController controller = loader.getController();

            controller.setService(service);
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root));
            controller.setStage(loginStage);

            // Afisez fereastra Login
            loginStage.show();

            // Inchid fereastra curenta (Retea de socializare)
            stage.close();
        }
        catch(IOException e){
            e.printStackTrace();
            MessageAlert.showErrorMessage(stage,"A aparut o eroare la logout!");
        }
    }


    @FXML
    private void handleDeleteAccountButton(){
        boolean response = MessageAlert.showYesNoConfirmationMessage(stage, "Confirm delete account", "Sigur doriti sa stergeti contul?");
        if(response){ // Daca utilizatorul a ales "Yes"
            boolean resp = deleteAccount();
            System.out.println(resp);
            if(resp){
                System.out.println("Contul trebuie sters");

                MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION, "Stergere cont", "Contul a fost sters cu succes!");
                openLogin();
                stage.close();
            }
            else{
                MessageAlert.showErrorMessage(stage, "A aparut o eroare la stergerea contului!");
                return;
            }
        }
    }

    private boolean deleteAccount(){
        try {
            service.removeObserver(this); //Trebuie sters observer-ul inainte de stergerea contului/utilizatorului
            service.removeUtilizator(username);
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            MessageAlert.showErrorMessage(stage, e.getMessage());
        }
        return false;
    }



    /*
    TAB: FRIENDS
     */

    @FXML
    private Tab friendsTab;

    @FXML
    private TableView<FriendDTO> friendsTableView;

    @FXML
    private TableColumn<FriendDTO, String > usernameTableColumn;

    @FXML
    private TableColumn<FriendDTO, String> friendsFromTableColumn;

    @FXML
    private TextField numeFriendField;

    @FXML
    private TextField usernameFriendField;

    @FXML
    private TextField friendsFromField;

    @FXML
    private Label numberOfFriendsLabel;

    @FXML
    private Label numberPageFriendsLabel;

    @FXML
    private Button previousPageFriendButton;

    @FXML
    private Button nextPageFriendButton;


    private ObservableList<FriendDTO> friendsList = FXCollections.observableArrayList();
    private static final int PAGE_SIZE_FRIENDS = 7;
    private int currentPageFriends = 0;


    private void initFriendsTab(){
        //Configurare coloane friendsTableView
        usernameTableColumn.setCellValueFactory(new PropertyValueFactory<>("Username"));
        friendsFromTableColumn.setCellValueFactory(new PropertyValueFactory<>("FriendsFromDateString"));

        // Leg ObservableList de friendsTableView
        friendsTableView.setItems(friendsList);

        // Incarcare prieteni
        loadFriends();

        // Adaug un Listener pentru a prelua datele prietenului selectat
        friendsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                loadFriendDetails(newSelection);
            }
        });
        clearFields();
    }

    private void updateFriendsTab(){
        loadFriends();
        clearFields();
        friendsTableView.refresh();
    }

    private void loadFriendDetails(FriendDTO prieten){
        if(prieten != null){
            Utilizator utilizator = prieten.getPrieten();
            numeFriendField.setText(utilizator.getFullName());
            usernameFriendField.setText(utilizator.getUserName());
            friendsFromField.setText(prieten.getFriendsFromString());
        }
    }

    public void loadFriends(){
        Pageable pageable = new Pageable(currentPageFriends, PAGE_SIZE_FRIENDS);

        Page<FriendDTO> page = service.getAllFriendsWithDatesPage(username, pageable);

        List<FriendDTO> friends = StreamSupport.stream(page.getElementsOfPage().spliterator(), false)
                .collect(Collectors.toList());


        // Afisez numarul de prieteni
        numberOfFriendsLabel.setText("You have " + service.getAllNumberFriendsWithDatesPage(username) + " friends.");

        // Incarc prietenii in TableView (friendsTableView)
        friendsList.clear();
        friendsList.setAll(friends);
        friendsTableView.setItems(friendsList);
        clearFields();

        int numberOfPages = (int) Math.ceil((double) page.getTotalNumberOfElements()/PAGE_SIZE_FRIENDS);

        numberPageFriendsLabel.setText("Page " + (currentPageFriends + 1) + " of " + numberOfPages);

        nextPageFriendButton.setDisable(numberOfPages == currentPageFriends + 1);
        previousPageFriendButton.setDisable(currentPageFriends == 0);
    }


    @FXML
    private void handleDeleteFriendButton(){

        if(numeFriendField.getText().equals("") || usernameFriendField.getText().equals("") || friendsFromField.getText().equals("")){
            MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION, "Stergere prieten", "Trebuie selectat prietenul pe care doriti sa-l stergeti!");
            return;
        }

        boolean result = MessageAlert.showYesNoConfirmationMessage(stage, "Confirmare stergere prieten", "Sigur doriti sa stergeti prietenul?");
        if(result == true){
            try{
                service.removePrieten(username, usernameFriendField.getText());
                MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION, "Stergere prieten", "Prietenul a fost sters cu succes!");
                clearFields();
            }
            catch(Exception e){
                MessageAlert.showErrorMessage(stage, e.getMessage());
            }
        }
    }


    private  void clearFields(){
        numeFriendField.setText("");
        usernameFriendField.setText("");
        friendsFromField.setText("");
    }


    @FXML
    private void handleCreateGroupButton(){
        try{
            // Incarc fisierul FXML pentru CreateGroupController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/retea_de_socializare/views/createModifyGroup.fxml"));
            Parent root = loader.load();

            // Adaug fisierul CSS
            root.getStylesheets().add(getClass().getResource("/com/example/retea_de_socializare/css/viewStyle.css").toExternalForm());

            // Obtin instanta controller-ului
            CreateGroupController controller = loader.getController();

            controller.setUsername(username);
            Stage createGroupStage = new Stage();
            createGroupStage.setTitle("Create group");
            createGroupStage.setScene(new Scene(root));
            controller.setStage(createGroupStage);
            controller.setService(service);

            // Afisez fereastra Login
            createGroupStage.show();
        }
        catch(IOException e){
            e.printStackTrace();
            MessageAlert.showErrorMessage(stage,"A aparut o eroare la incarcarea ferestrei pentru crearea grupului!");
        }
    }

    public void handlePreviousPageFriendButton(ActionEvent actionEvent) {
        currentPageFriends--;
        loadFriends();
    }

    public void handleNextPageFriendButton(ActionEvent actionEvent) {
        currentPageFriends++;
        loadFriends();
    }



    /*
    TAB: ADD NEW FRIENDS
     */

    @FXML
    private Tab addNewFriendsTab;

    @FXML
    private TableView<Utilizator> possibleFriendsTableView;

    @FXML
    private TableColumn<Utilizator, String> namePossibelFriendsTableColumn;

    @FXML
    private TableColumn<Utilizator, String> usernamePossibleFriendsTableColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Label mutualFriendsLabel;

    @FXML
    private ListView<Utilizator> mutualFriendsListView;

    @FXML
    private Label numberPagePossibleFriendsLabel;

    @FXML
    private Button nextPossibleFriendsPageButton;

    @FXML
    private Button previousPossibleFriendsPageButton;

    private ObservableList<Utilizator> possibleFriendsListObservable = FXCollections.observableArrayList();
    private static final int PAGE_SIZE_POSSIBLE_FRIENDS = 8;
    private int currentPagePossibleFriends = 0;

    private String searchText = "";


    private void initAddNewFriendsTab(){
        //Configurarea coloanelor
        namePossibelFriendsTableColumn.setCellValueFactory(new PropertyValueFactory<>("FullName"));
        usernamePossibleFriendsTableColumn.setCellValueFactory(new PropertyValueFactory<>("UserName"));

        //Configurare friendsListView
        mutualFriendsListView.setCellFactory(param -> new ListCell<Utilizator>(){
            @Override
            protected void updateItem(Utilizator item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                }
                else{
                    setText(item.getUserName()); //afisez username-ul
                }
            }
        });



        // Selectia din TableView
        possibleFriendsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showMutualFriends(newValue);
            }
        });

        // Incarc utilizatorii care pot fi posibili prieteni
        updateAddNewFriendsTab();
    }

    private void updateAddNewFriendsTab() {
        Page<Utilizator> page;
        if(searchText.equals("")){
            // Incarc posibilii prieteni din Service
            Pageable pageable = new Pageable(currentPagePossibleFriends, PAGE_SIZE_POSSIBLE_FRIENDS);
            page = service.getPossibleFriendsPage(username, pageable);
        }
        else{
            // Incarc posibilii prieteni din Service dupa search
            Pageable pageable = new Pageable(currentPagePossibleFriends, PAGE_SIZE_POSSIBLE_FRIENDS);
            FilterDTO filterDTO = new FilterDTO();
            filterDTO.setSearchText(Optional.of(searchText));
            page = service.getSearchPossibleFriendsPage(username, pageable, filterDTO);
        }

        List<Utilizator> possibleFriends = StreamSupport.stream(page.getElementsOfPage().spliterator(),false)
                .collect(Collectors.toList());

        int numberOfPages = (int) Math.ceil((double) page.getTotalNumberOfElements()/PAGE_SIZE_POSSIBLE_FRIENDS);
        numberPagePossibleFriendsLabel.setText("Page " + (currentPagePossibleFriends + 1) + " of " + numberOfPages);


        nextPossibleFriendsPageButton.setDisable(numberOfPages <= currentPagePossibleFriends + 1);
        previousPossibleFriendsPageButton.setDisable(currentPagePossibleFriends == 0);

        // Actualizez tabelul cu prietenii posibili
        showPossibleFriends(possibleFriends);
    }

    private void showPossibleFriends(Iterable<Utilizator> possibleFriends) {
        if(service == null || username == null){
            MessageAlert.showErrorMessage(stage,"A aparut o eroare!");
            return;
        }

        try{
            // Obtin posibilii prieteni si ii adaug in lista observabila
            possibleFriendsListObservable.clear();
            possibleFriends.forEach(user -> {
                if(user.getId() != 0){
                    possibleFriendsListObservable.add(user);
                }
            });

            possibleFriendsTableView.setItems(possibleFriendsListObservable);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(stage, e.getMessage());
        }

    }

    private void showMutualFriends(Utilizator selectedUser) {
        if(service == null || username == null || selectedUser == null){
            mutualFriendsLabel.setText("Prieteni comuni:");
            mutualFriendsListView.getItems().clear();
            MessageAlert.showErrorMessage(stage,"Prietenii comuni nu sunt disponibili momentan!");
            return;
        }

        try{
            Iterable<Utilizator> mutualFriendsIterable = service.getMutualFriends(username, selectedUser);

            List<Utilizator> mutualFriends = new ArrayList<>();
            mutualFriendsIterable.forEach(mutualFriends::add);

            mutualFriendsLabel.setText("Mutual friends: " + mutualFriends.size());
            mutualFriendsListView.getItems().clear(); // Sterg lista anterioara
            mutualFriendsListView.setItems(FXCollections.observableList(mutualFriends));
        }
        catch(Exception e){
            mutualFriendsLabel.setText("Prieteni comuni:");
            mutualFriendsListView.getItems().clear();
            MessageAlert.showErrorMessage(stage,"A aparut o eroare la incarcarea prietenilor comuni!");
        }
    }

    @FXML
    private void handleSearchButton(){
        if(service == null || username == null){
            MessageAlert.showErrorMessage(stage,"A aparut o eroare la cautare!");
            return;
        }

        String search = searchField.getText();
        if(search.isEmpty()){
            MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION,"Search friends","Trebuie sa introduceti un nume sau un username dupa care se face cautarea!");
        }
        else{
            try{
                searchText = search;
                currentPagePossibleFriends = 0;
                updateAddNewFriendsTab();
            }
            catch(Exception e){
                MessageAlert.showErrorMessage(stage, e.getMessage());
            }

        }
    }

    @FXML
    private void handleSendRequestButton(){
        Utilizator selecteduser = possibleFriendsTableView.getSelectionModel().getSelectedItem();
        if(selecteduser == null){
            MessageAlert.showErrorMessage(stage, "Trebuie selectat utilizatorul caruia doriti sa ii trimiteti cererea de prietenie!");
            return;
        }
        else{
            try{
                service.addCererePrietenie(username, selecteduser);
                MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION,"Cerere prietenie","Cererea de prietenie a fost trimisa!");
                mutualFriendsListView.getItems().clear();
                mutualFriendsLabel.setText("Mutual friends: ");
            }
            catch(Exception e){
                MessageAlert.showErrorMessage(stage, e.getMessage());
            }
        }
    }

    public void handlePreviousPagePossibleFriendButton(ActionEvent actionEvent) {
        currentPagePossibleFriends--;
        updateAddNewFriendsTab();
    }

    public void handleNextPagePossibleFriendButton(ActionEvent actionEvent) {
        currentPagePossibleFriends++;
        updateAddNewFriendsTab();
    }





    /*
    TAB: FRIEND REQUESTS
     */

    @FXML
    private Tab friendRequestsTab;

    @FXML
    private Label friendRequestsLabel;

    @FXML
    private TableView<CererePrietenie> friendRequeststTableView;

    @FXML
    private TableColumn<CererePrietenie, String> nameRequestsTableColumn;

    @FXML
    private TableColumn<CererePrietenie, String> usernameRequestsTableColumn;

    @FXML
    private TableColumn<CererePrietenie, String> dateRequestsTableColumn;

    @FXML
    private TableColumn<CererePrietenie, String> statusRequestsTableColumn;

    @FXML
    private RadioButton sentRequestsRadioButton;

    @FXML
    private RadioButton receivedRequestsRadioButton;

    @FXML
    private Button acceptRequestButton;

    @FXML
    private Button deleteRequestButton;

    @FXML
    private Button declineRequestButton;


    private ToggleGroup toggleGroup;
    private int lastFriendRequestsCount = 0;


    public void initFriendRequestsTab(){
        //Setez ca butonul deleteRequestButton sa nu fie vizibil
        deleteRequestButton.setVisible(false);

        //Setez ca butoanele acceptRequestButton si declineRequestButton sa fie vizibile
        acceptRequestButton.setVisible(true);
        declineRequestButton.setVisible(true);

        // Creez un ToogleGroup
        toggleGroup = new ToggleGroup();

        // Ascoziez RadioButton-urile cu ToggleGroup
        sentRequestsRadioButton.setToggleGroup(toggleGroup);
        receivedRequestsRadioButton.setToggleGroup(toggleGroup);

        // Setez receivedRadioButton ca implicit
        receivedRequestsRadioButton.setSelected(true);

        //Configurare coloane TableView - FARA NAME si USERNAME
        dateRequestsTableColumn.setCellValueFactory(new PropertyValueFactory<>("DataString"));
        statusRequestsTableColumn.setCellValueFactory(new PropertyValueFactory<>("Status"));

        // Incarc datele in TableView
        showRequestsReceived(); //cererile de prietenie primite
    }

    private void updateFriendRequestsTab(){
        if (receivedRequestsRadioButton.isSelected()) {
            showRequestsReceived();
        } else {
            showRequestsSent();
        }
    }


    private void showRequestsReceived(){
        //Configurare coloane TableView name si username
        usernameRequestsTableColumn.setCellValueFactory(new PropertyValueFactory<>("Utilizator1Username"));
        nameRequestsTableColumn.setCellValueFactory(new PropertyValueFactory<>("Utilizator1FullName"));

        try{
            Iterable<CererePrietenie> cereri = service.getRequestsReceived(username);

            ObservableList<CererePrietenie> observableList = FXCollections.observableArrayList();
            cereri.forEach(observableList::add);

            friendRequestsLabel.setText("Friend requests received: " + observableList.size());

            friendRequeststTableView.setItems(observableList);

        }
        catch(Exception e){
            MessageAlert.showErrorMessage(stage,e.getMessage());
        }
    }

    private void showRequestsSent(){
        //Configurare coloane TableView name si username
        usernameRequestsTableColumn.setCellValueFactory(new PropertyValueFactory<>("Utilizator2Username"));
        nameRequestsTableColumn.setCellValueFactory(new PropertyValueFactory<>("Utilizator2FullName"));

        try{
            Iterable<CererePrietenie> cereri = service.getRequestsSent(username);

            ObservableList<CererePrietenie> observableList = FXCollections.observableArrayList();
            cereri.forEach(observableList::add);

            friendRequestsLabel.setText("Friend requests sent: " + observableList.size());

            friendRequeststTableView.setItems(observableList);

        }
        catch(Exception e){
            MessageAlert.showErrorMessage(stage,e.getMessage());
        }
    }

    @FXML
    private void handleRadioButtons(){
        if(sentRequestsRadioButton.isSelected()){
            //Setez ca butonul deleteRequestButton sa fie vizibil
            deleteRequestButton.setVisible(true);

            //Setez ca butoanele acceptRequestButton si declineRequestButton sa  nu fie vizibile
            acceptRequestButton.setVisible(false);
            declineRequestButton.setVisible(false);

            showRequestsSent();
        }
        else if(receivedRequestsRadioButton.isSelected()){
            //Setez ca butonul deleteRequestButton sa nu fie vizibil
            deleteRequestButton.setVisible(false);

            //Setez ca butoanele acceptRequestButton si declineRequestButton sa fie vizibile
            acceptRequestButton.setVisible(true);
            declineRequestButton.setVisible(true);

            showRequestsReceived();
        }
    }

    @FXML
    private void handleDeclineRequestButton(){
        CererePrietenie cererePrietenie = friendRequeststTableView.getSelectionModel().getSelectedItem();

        if(cererePrietenie == null){
            MessageAlert.showErrorMessage(stage,"Trebuie selectata cererea de prietenie pe care doresti sa o respingi!");
            return;
        }

        try{
            service.declineRequest(cererePrietenie);
            MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION,"Respingere cerere de prietenie","Cererea de prietenie a fost respinsa!");
        } catch (Exception e) {
            MessageAlert.showErrorMessage(stage,e.getMessage());
        }
    }

    @FXML
    private void handleAcceptRequestButton(){
        CererePrietenie cererePrietenie = friendRequeststTableView.getSelectionModel().getSelectedItem();

        if(cererePrietenie == null){
            MessageAlert.showErrorMessage(stage,"Trebuie selectata cererea de prietenie pe care doresti sa o accepti!");
            return;
        }

        try{
            service.acceptRequest(cererePrietenie);
            MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION,"Accepta cerere de prietenie","Cererea de prietenie a fost acceptata!");
        } catch (Exception e) {
            MessageAlert.showErrorMessage(stage,e.getMessage());
        }


    }

    @FXML
    private void handleDeleteRequestButton(){
        CererePrietenie cererePrietenie = friendRequeststTableView.getSelectionModel().getSelectedItem();

        if(cererePrietenie == null){
            MessageAlert.showErrorMessage(stage,"Trebuie selectata cererea de prietenie pe care doresti sa o stergi!");
            return;
        }

        try{
            service.deleteRequest(cererePrietenie);
            MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION,"Sterge cerere de prietenie","Cererea de prietenie a fost stearsa!");
        } catch (Exception e) {
            MessageAlert.showErrorMessage(stage,e.getMessage());
        }

    }


    private void checkForNewFriendRequests(){
        try{
            //Obtin toate cererile de prietenie primite
            Iterable<CererePrietenie> cereri = service.getRequestsReceived(username);

            List<CererePrietenie> cereriNoi = StreamSupport.stream(cereri.spliterator(), false)
                    .filter(cererePrietenie -> cererePrietenie.getStatus().equals("no answer"))
                    .collect(Collectors.toList());

            if(cereriNoi.size() > lastFriendRequestsCount){
                for(CererePrietenie cererePrietenie: cereriNoi){
                    MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION, "Notificare", "Ai primit o noua cerere de prietenie de la " + cererePrietenie.getUtilizator1Username() + "!");
                }
                lastFriendRequestsCount = cereriNoi.size();
            }
        } catch (Exception e) {
            MessageAlert.showErrorMessage(stage, e.getMessage());
        }
    }

    private void getFriendRequestsNumber(){
        Iterable<CererePrietenie> cereri = service.getRequestsReceived(username);

        int numarCereri = (int) StreamSupport.stream(cereri.spliterator(), false)
                .filter(cererePrietenie -> cererePrietenie.getStatus().equals("no answer"))
                .count(); // count() returnează un long, dar putem face cast la int

        lastFriendRequestsCount = numarCereri;
    }


    private void updateTabStyleWithNotification(){
        getFriendRequestsNumber();
        friendRequestsTab.getStyleClass().remove("tab-with-notification"); // Elimina stilul existent
        if(lastFriendRequestsCount != 0){
            friendRequestsTab.getStyleClass().add("tab-with-notification");
        }
    }




    /*
    CHAT TAB
     */

    @FXML
    private Tab chatTab;

    @FXML
    private ListView<MessageView> chatListView;

    @FXML
    private Label statusOrNameChatLabel;

    @FXML
    private TextField messageTextField;

    @FXML
    private ComboBox<ComboBoxItem> friendsOrGroupsComboBox;

    Optional<Entity> chatWith = Optional.empty();
    private MessageView selectedMessageView = null;  // Pentru a ține mesajul selectat



    public void initComboBox(){
        ObservableList<ComboBoxItem> friendsOrGroupsList = FXCollections.observableArrayList();

        friendsOrGroupsList.add(new ComboBoxItem("Choose a friend/group"));

        service.getAllFriends(username).forEach(ut -> friendsOrGroupsList.add(new ComboBoxItem(ut)));
        service.getGrupuriUtilizator(username).forEach(gr -> friendsOrGroupsList.add(new ComboBoxItem(gr)));

        //Setez datele in ComboBox
        friendsOrGroupsComboBox.setItems(friendsOrGroupsList);
        friendsOrGroupsComboBox.setValue(friendsOrGroupsList.get(0));

        friendsOrGroupsComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            handleComboBoxSelection(newValue);  // Apelez functia de gestionare a selectiei
        });
    }


    private void handleComboBoxSelection(ComboBoxItem selectedItem){
        if (selectedItem != null && !selectedItem.getItem().toString().equals("Choose a friend/group")) {
            if(selectedItem.getItem() instanceof Utilizator){
                boolean activ = service.isActiveUser(((Utilizator) selectedItem.getItem()).getUserName());
                String statusActiv;
                if(activ){
                    statusActiv = "online";
                }
                else{
                    statusActiv = "offline";
                }
                statusOrNameChatLabel.setText(((Utilizator) selectedItem.getItem()).getUserName() + ": " + statusActiv );
                chatWith = Optional.of((Utilizator) selectedItem.getItem());

                loadMessages((Utilizator) selectedItem.getItem(), username);
            }
            else if(selectedItem.getItem() instanceof Grup){
                String numeGrup = ((Grup) selectedItem.getItem()).getNumeGrup();
                List<Utilizator> members = ((Grup) selectedItem.getItem()).getMembri();

                int cate= (int) members.stream()
                        .filter(member -> !member.getUserName().equals(username) && service.isActiveUser(member.getUserName()))
                        .count();

                statusOrNameChatLabel.setText("Group: " + numeGrup + "  (" + cate + " online)");
                chatWith = Optional.of((Grup) selectedItem.getItem());

                loadMessages((Grup) selectedItem.getItem(),username);
            }
        }
        else if(selectedItem.getItem().toString().equals("Choose a friend/group")){
            statusOrNameChatLabel.setText("");
            chatWith = Optional.empty();
            chatListView.getItems().clear();
        }
    }


    public void updateChatTab(){
        initComboBox();
        statusOrNameChatLabel.setText(null);
        messageTextField.clear();
        messageTextField.setPromptText("");
        chatListView.getItems().clear();
        selectedMessageView = null;
    }


    @FXML
    private void handleSendMessageButton(){
        String mesaj = messageTextField.getText();
        if(!chatWith.isPresent()){
            MessageAlert.showErrorMessage(stage, "Trebuie sa selectati cu cine doriti sa vorbiti!");
        }
        else if(mesaj.isEmpty()){
            MessageAlert.showErrorMessage(stage, "Trebuie sa introduceti mesajul pe care doriti sa il trimiteti!");
        }
        else{
            try {
                //Daca exista un mesaj selectat pentru reply
                Long replyMessageId = (selectedMessageView != null) ? selectedMessageView.getMessage().getId() : null;

                service.saveMessage(username, chatWith.get(), mesaj, replyMessageId);
                messageTextField.clear();
                messageTextField.setPromptText("");
                selectedMessageView = null;
            }
            catch (Exception e) {
                MessageAlert.showErrorMessage(stage, e.getMessage());
            }
        }
    }


    private void loadMessages(Entity entity, String username){
        try{
            List<Message> messages = service.getMessagesChat(entity, username);

            ObservableList<MessageView> messageViews = FXCollections.observableArrayList();
            chatListView.getItems().clear();

            for(Message message : messages){
                //Verific daca utilizatorul curent a trimis mesajul
                boolean isCurrentUser = message.getFrom().getUserName().equals(username);

                //Creez un MessageView pentru fiecare mesaj
                MessageView messageView = new MessageView(message, isCurrentUser);

                // Adaugă un listener pentru selectarea unui mesaj
                messageView.setOnMouseClicked(event -> {
                    selectedMessageView = messageView;  // Salvează mesajul selectat
                    messageTextField.setPromptText("Replying to: " + message.getMessage());  // Afișează un prompt în TextField
                });

                messageViews.add(messageView);
            }

            chatListView.setItems(messageViews);
            Platform.runLater(() -> chatListView.scrollTo(messages.size() - 1)); //Incep afisarea de la cel mai recent mesaj

        }
        catch (Exception e){
            e.printStackTrace();
            MessageAlert.showErrorMessage(stage, e.getMessage());
        }
    }




    /*
    RETEA DE SOCIALIZARE
     */

    @Override
    public void update(EntityChangeEvent event) {
        System.out.println("update primit");

        if(username == null){
            return;
        }

        updateFriendsTab();
        updateAddNewFriendsTab();
        updateFriendRequestsTab();

        clearFields();
        mutualFriendsListView.getItems().clear();

        // Fortez reimprospatarea tabelului
        friendsTableView.refresh();
        friendRequeststTableView.refresh();

        // Actualizez selectia si statusul utilizatorului selectat
        Platform.runLater(() -> {
            ComboBoxItem selectedItem = friendsOrGroupsComboBox.getValue();
            if (selectedItem != null) {
                // Apelez functia pentru a actualiza statusul
                handleComboBoxSelection(selectedItem);
            }

            if(chatTab.isSelected()){
                chatWith.ifPresent(entity -> loadMessages(entity, username));
            }
        });

        if(event.getType().equals(ChangeEventType.ADD)){
            checkForNewFriendRequests();
        }
        else if(event.getType().equals(ChangeEventType.DELETE)){
            getFriendRequestsNumber();
        }
        updateTabStyleWithNotification();
    }


    public void initializeReteaDeSocializare(){
        ////service.getObservableService().addObserver(this);


        //Initializare tab-uri
        initMyAccountTab();
        initFriendsTab();
        initAddNewFriendsTab();
        initFriendRequestsTab();

        //Adaug un listener pentru schimbarea tab-urilor
        handleTabSelection();

        stage.getScene().getStylesheets().add(getClass().getResource("/com/example/retea_de_socializare/css/tabStyle.css").toExternalForm());

        stage.setOnCloseRequest(event -> onWindowClose());

        updateTabStyleWithNotification();
    }

   private void handleTabSelection(){

        friendsTab.setOnSelectionChanged(event -> {
            if(friendsTab.isSelected()){
                updateFriendsTab();
            }
        });

        addNewFriendsTab.setOnSelectionChanged(event -> {
            if(addNewFriendsTab.isSelected()){
                searchField.clear();
                searchText="";
                currentPagePossibleFriends = 0;
                updateAddNewFriendsTab();
            }
        });

        friendRequestsTab.setOnSelectionChanged(event -> {
            if(friendRequestsTab.isSelected()){
                updateFriendRequestsTab();
            }
        });

        chatTab.setOnSelectionChanged(event -> {
            if(chatTab.isSelected()){
                updateChatTab();
            }
        });
    }


    public void setService(ObservableService service) {
        this.service = service;

        System.out.println("Adăugăm ReteaDeSocializareController ca observator");

        service.addObserver(this);
        initializeReteaDeSocializare();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
