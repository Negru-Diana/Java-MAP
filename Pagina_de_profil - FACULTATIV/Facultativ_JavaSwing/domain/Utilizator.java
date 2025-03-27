package domain;

public class Utilizator {

    private String name; // Numele utilizatorului
    private String username; //Username-ul utilizatorului
    private String hashedPassword; //Parola "hashuita" a utilizatorului
    private String ocupation; //Ocupatia utilizatorului (optionala)
    private String profileDescription; //Descriere porfil utilizator (optionala)
    private int numberOfPosts; //Numarul de postari ale utilizatorului
    private int numberOfFollowers; //Numarul de urmaritori ai utilizatorului
    private int numberOfFollowing; //Numarul de urmariri ale utilizatorului
    private String picture; //Poza de profil a uilizatorului

    //Constructori
    public Utilizator() {}

    public Utilizator(String name, String username, String hashedPassword, String ocupation,
                      String profileDescription, int numberOfPosts, int numberOfFollowers, int numberOfFollowing) {
        this.name = name;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.ocupation = ocupation;
        this.profileDescription = profileDescription;
        this.numberOfPosts = numberOfPosts;
        this.numberOfFollowers = numberOfFollowers;
        this.numberOfFollowing = numberOfFollowing;
    }

    public Utilizator(String name, String username, String hashedPassword, int numberOfPosts, int numberOfFollowers, int numberOfFollowing) {
        this.name = name;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.numberOfPosts = numberOfPosts;
        this.numberOfFollowers = numberOfFollowers;
        this.numberOfFollowing = numberOfFollowing;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getOcupation() {
        return ocupation;
    }

    public void setOcupation(String ocupation) {
        this.ocupation = ocupation;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public int getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }

    public int getNumberOfFollowers() {
        return numberOfFollowers;
    }

    public void setNumberOfFollowers(int numberOfFollowers) {
        this.numberOfFollowers = numberOfFollowers;
    }

    public int getNumberOfFollowing() {
        return numberOfFollowing;
    }

    public void setNumberOfFollowing(int numberOfFollowing) {
        this.numberOfFollowing = numberOfFollowing;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
