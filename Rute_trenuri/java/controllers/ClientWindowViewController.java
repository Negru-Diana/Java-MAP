package com.example.examen_db.controllers;

import com.example.examen_db.domain.City;
import com.example.examen_db.domain.TrainStation;
import com.example.examen_db.service.ObservableService;
import com.example.examen_db.utils.MessageAlert;
import com.example.examen_db.utils.observers.Observer;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientWindowViewController implements Observer {
    private ObservableService service;
    private Stage stage;

    @FXML
    private ComboBox<String> departureComboBox;

    @FXML
    private ComboBox<String> destinationComboBox;

    @FXML
    private CheckBox directRoutesCheckBox;

    @FXML
    private Label routesLabel;

    @FXML
    private Label labelUsers;


    private final double PRICE_PER_STATION = 10.0;

    @Override
    public void update(int cate, Pair<String, String> filterPair) {
        if(departureComboBox.getValue().equals(filterPair.getKey()) && destinationComboBox.getValue().equals(filterPair.getValue())){
            labelUsers.setText("" + String.valueOf(cate-1) + "other users(s) are looking at the same route!");
        }
    }

    private void initView(){
        directRoutesCheckBox.setSelected(false);
        loadCitiesComboBox(); //Incarc orasele in ComboBox
        routesLabel.setText("");
        labelUsers.setText("");
    }

    public void loadCitiesComboBox(){
        Iterable<City> cities = service.getAllCities();

        departureComboBox.getItems().clear();
        destinationComboBox.getItems().clear();

        for(City city : cities){
            departureComboBox.getItems().add(city.getName());
            destinationComboBox.getItems().add(city.getName());
        }

    }

    public void handleSearchButton() {
        routesLabel.setText(""); // Resetez textul afisat

        String departureCity = departureComboBox.getValue(); //Orasul de plecare
        String destinationCity = destinationComboBox.getValue(); //Orasul destinatie

        if (departureCity == null || destinationCity == null) {
            MessageAlert.showErrorMessage(stage, "Trebuie sa selectati orasul de plecare si destinatia!");
            return;
        }
        if(departureCity.equals(destinationCity)){
            MessageAlert.showErrorMessage(stage, "Orasul destinatie si orasul de plecare trebuie sa fie diferite!");
            return;
        }

        //Daca s-a selectat optiunea de doar rute directe (CheckBox)
        if (directRoutesCheckBox.isSelected()) {
            // Afisez rutele directe
            String directRoutes = getDirectRoutes(departureCity, destinationCity);
            routesLabel.setText(directRoutes.isEmpty() ? "No direct routes found." : directRoutes);
        } else {  //Altfel
            // Afisare rute directe si indirecte
            String allRoutes = getAllRoutes(departureCity, destinationCity);
            routesLabel.setText(allRoutes.isEmpty() ? "No routes found." : allRoutes);
        }

        service.addFilter(departureCity,destinationCity);
    }

    //Metoda pentru a gasi rutele directe
    private String getDirectRoutes(String departureCity, String destinationCity) {
        StringBuilder directRoutes = new StringBuilder();

        // Grupez statiile dupa id-ul trenurilor si lista statiilor prin care trece trenul
        Map<String, List<TrainStation>> stationsByTrain = StreamSupport.stream(service.getAllTrainStations().spliterator(), false)
                .collect(Collectors.groupingBy(TrainStation::getTrain_id));

        //Parcurg pentru fiecare tren statiile prin care trece
        for (Map.Entry<String, List<TrainStation>> entry : stationsByTrain.entrySet()) {
            String trainId = entry.getKey(); //Obtin id-ul trenului
            List<TrainStation> stations = entry.getValue(); //Lista de statii prin care trece trenul

            // Sortez statiile in ordinea trecerii trenului
            stations.sort(Comparator.comparing(TrainStation::getDepartureCityId));

            //Creez lista oraselor (cu nume, nu cu id-uri) prin care trece trenul
            List<String> citiesInRoute = stations.stream()
                    .map(station -> service.getCityNameById(station.getDepartureCityId())) // Convertesc ID-urile in nume
                    .collect(Collectors.toList());
            citiesInRoute.add(service.getCityNameById(stations.get(stations.size() - 1).getDestinationCityId()));

            if (citiesInRoute.contains(departureCity) && citiesInRoute.contains(destinationCity)) {
                int departureIndex = citiesInRoute.indexOf(departureCity);
                int destinationIndex = citiesInRoute.indexOf(destinationCity);

                if (departureIndex < destinationIndex) {
                    directRoutes.append(departureCity);

                    // Calculez numarul de statii intre orasul de plecare si cel de destinatie
                    int numberOfStations = destinationIndex - departureIndex;

                    for (int i = departureIndex + 1; i <= destinationIndex; i++) {
                        directRoutes.append(" - ").append(trainId).append(" -> ").append(citiesInRoute.get(i));
                    }

                    // Calculez pretul
                    double routePrice = PRICE_PER_STATION * (numberOfStations);
                    directRoutes.append(", price: ").append(routePrice);

                    directRoutes.append("\n");
                }
            }
        }

        return directRoutes.toString();
    }




    //Metoda pentru a gasi toate rutele posibile
    private String getAllRoutes(String departureCity, String destinationCity) {
        Set<String> allRoutes = new HashSet<>();
        findRoutesRecursive(departureCity, destinationCity, new StringBuilder(), allRoutes, new HashSet<>(), 0);

        // Convertesc setul intr-un singur string pentru afisare
        return allRoutes.stream().collect(Collectors.joining("\n"));
    }


    //Metoda pentru a gasi recursiv toate rutele posibile (directe si indirecte)
    private void findRoutesRecursive(String currentCity, String destinationCity, StringBuilder currentRoute,
                                     Set<String> allRoutes, Set<String> visitedCities, int stationCount) {
        // Daca orasul curent a fost deja vizitat in ruta curenta, sar peste el
        if (visitedCities.contains(currentCity)) {
            return;
        }

        // Adaug orasul curent la setul de orașe vizitate
        visitedCities.add(currentCity);

        // Daca este primul oras din ruta, il adaugam in ruta curenta
        if (currentRoute.length() == 0) {
            currentRoute.append(currentCity); // Primul oraș, nu pune "->"
        }

        for (TrainStation ts : service.getAllTrainStations()) {
            String departureCityName = service.getCityNameById(ts.getDepartureCityId());
            String nextCityName = service.getCityNameById(ts.getDestinationCityId());

            // Verific daca orasul curent este punctul de plecare al trenului
            if (departureCityName.equals(currentCity)) {
                // Construiesc segmentul rutei
                String routeSegment = " - " + ts.getTrain_id() + " -> " + nextCityName;
                StringBuilder newRoute = new StringBuilder(currentRoute);
                newRoute.append(routeSegment);

                // Incrementez numarul de statii
                int newStationCount = stationCount + 1;

                // Dacă am ajuns la destinație, adăugăm ruta în setul `allRoutes`
                if (nextCityName.equals(destinationCity)) {
                    // Calculez pretul rutei
                    double routePrice = PRICE_PER_STATION * newStationCount;
                    // Adaug prețul la ruta finala
                    newRoute.append(", price: ").append(routePrice);
                    allRoutes.add(newRoute.toString());
                } else {
                    // Continui recursiv din urmatorul oraș
                    findRoutesRecursive(nextCityName, destinationCity, newRoute, allRoutes, visitedCities, newStationCount);
                }
            }
        }

        // Scoatem orașul curent din setul de orașe vizitate (backtracking)
        visitedCities.remove(currentCity);
    }



    public void setService(ObservableService service) {
        this.service = service;

        service.addObserver(this);
        initView();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
