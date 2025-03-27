package com.example.examen_db.domain;

public class TrainStation extends Entity<Integer>{
    private String train_id;
    private String departureCityId;
    private String destinationCityId;

    public TrainStation(Integer id, String train_id, String departureCityId, String destinationCityId) {
        super.setId(id);
        this.train_id = train_id;
        this.departureCityId = departureCityId;
        this.destinationCityId = destinationCityId;
    }

    public String getTrain_id() {
        return train_id;
    }

    public void setTrain_id(String train_id) {
        this.train_id = train_id;
    }

    public String getDepartureCityId() {
        return departureCityId;
    }

    public void setDepartureCityId(String departureCityId) {
        this.departureCityId = departureCityId;
    }

    public String getDestinationCityId() {
        return destinationCityId;
    }

    public void setDestinationCityId(String destinationCityId) {
        this.destinationCityId = destinationCityId;
    }

    @Override
    public String toString() {
        return "Station { traindid: " + this.train_id + "}";
    }
}
