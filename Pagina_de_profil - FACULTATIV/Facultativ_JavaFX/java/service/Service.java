package com.example.facultativ_javafx.service;

import com.example.facultativ_javafx.domain.Utilizator;
import com.example.facultativ_javafx.exceptions.ServiceException;
import com.example.facultativ_javafx.repository.Repository;
import com.example.facultativ_javafx.utils.PasswordUtils;

import javax.sql.rowset.serial.SerialException;
import java.util.Random;

public class Service {
    private Repository<Utilizator> userRepo;

    public Service(Repository userRepo) {
        this.userRepo = userRepo;
    }

    //Metoda pentru a adauga un nou utilizator
    public void add(String name, String username, String password) {
        Utilizator userv = userRepo.findByUsername(username);
        if (userv != null) {
            throw new ServiceException("Exista deja un utilizator cu acest username!");
        }

        try{
            //"Hashuirea" parolei
            String hashedPassword = PasswordUtils.hashPassword(password);

            //Obtin numere random pentru numarul de postari, numarul de followers si cel de following
            int postsNumber = generateLowRandomNumber();
            int followersNumber = generateRandomNumber();
            int followingNumber = generateRandomNumber();

            //Crearea unui nou utilizator
            Utilizator user = new Utilizator(name, username, hashedPassword, postsNumber, followersNumber, followingNumber);
            userRepo.save(user); //Salvarea utilizatorului in fisier
        }
        catch(Exception e){
            throw new ServiceException(e.getMessage());
        }
    }


    //Metoda pentru autentificare
    public void isLoginDataValid(String username, String password) {
        //Obtin datele utilizatorului care vrea sa se autentifice
        Utilizator user = userRepo.findByUsername(username);

        //Verific daca utilizatorul exista
        if(user == null){
            throw new ServiceException("Nu a fost gasit contul de utilizator asociat username-ului!");
        }

        //Verific daca parola este corecta
        boolean correct;
        try{
             correct = PasswordUtils.verifyPassword(password, user.getHashedPassword());
        }
        catch(Exception e){
            throw new ServiceException(e.getMessage());
        }

        if(!correct){
            throw new ServiceException("Parola este incorecta!");
        }
    }


    //Metoda pentru a obtine date despre utilizatorul logat
    public Utilizator getUser(String username) {
        return userRepo.findByUsername(username);
    }


    //Metoda pentru update a unui utilizator
    public void update(Utilizator user, String username) {
        Utilizator oldUser = userRepo.findByUsername(username);
        user.setHashedPassword(oldUser.getHashedPassword());
        user.setNumberOfFollowers(oldUser.getNumberOfFollowers());
        user.setNumberOfFollowing(oldUser.getNumberOfFollowing());
        user.setNumberOfPosts(oldUser.getNumberOfPosts());
        try{
            userRepo.update(oldUser, user);
        }
        catch(Exception e){
            throw new ServiceException(e.getMessage());
        }
    }



    //Functii utile

    //Metoda pentru generarea unui numar random mai mic de 100000
    private int generateRandomNumber(){
        Random rand = new Random();
        return Math.abs(rand.nextInt(10000));
    }

    //Metoda pentru generarea unui numar random mai mic de 1000
    private int generateLowRandomNumber(){
        Random rand = new Random();
        return Math.abs(rand.nextInt(1000));
    }

}
