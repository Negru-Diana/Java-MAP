package com.example.facultativ_javafx.repository;

import com.example.facultativ_javafx.domain.Utilizator;
import com.example.facultativ_javafx.exceptions.RepoException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UtilizatorRepository implements Repository<Utilizator> {
    private final String FILE_PATH = "D:\\UBB INFO\\UBB INFO - ANUL II (2024-2025)\\Semestrul 3\\MAP (Metode Avansate de Programare)\\Laborator\\Laborator 12 - FACULTATIV\\Utilizatori_1.txt";

    //FORMAT FISIER:
    //Nume;Username;Hashed Password;Ocupation;Profile Description;Picture;Number of Posts;Number of Followers;Number of Following


    public UtilizatorRepository() {}

    @Override
    public void save(Utilizator entity) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))){
            String line = String.join(";",
                    entity.getName(),
                    entity.getUsername(),
                    entity.getHashedPassword(),
                    entity.getOcupation() == null ? "" : entity.getOcupation(),
                    //Inlocuiesc "\n" cu "\\n" pentru descriere
                    entity.getProfileDescription() == null ? "" : entity.getProfileDescription().replace("\n", "\\n"),
                    entity.getPicture() == null ? "" : entity.getPicture(),
                    String.valueOf(entity.getNumberOfPosts()),
                    String.valueOf(entity.getNumberOfFollowers()),
                    String.valueOf(entity.getNumberOfFollowing())
            );
            writer.write(line);
            writer.newLine();
        }
        catch(IOException e){
            throw new RepoException("A aparut o problema la crearea contului!");
        }
    }

    @Override
    public List<Utilizator> findAll() {
        List<Utilizator> users = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(";");
                if(split.length < 9) {
                    continue;
                }

                Utilizator user = new Utilizator();
                user.setName(split[0]);
                user.setUsername(split[1]);
                user.setHashedPassword(split[2]);
                user.setOcupation(split[3].isEmpty() ? null : split[3]);
                //Inlocuiesc "\\n" cu "\n" pentru descriere
                user.setProfileDescription(split[4].isEmpty() ? null : split[4].replace("\\n", "\n"));
                user.setPicture(split[5].isEmpty() ? null : split[5]);
                user.setNumberOfPosts(Integer.parseInt(split[6]));
                user.setNumberOfFollowers(Integer.parseInt(split[7]));
                user.setNumberOfFollowing(Integer.parseInt(split[8]));

                users.add(user);
            }
        }
        catch(IOException e){
            throw new RepoException("A aparut o problema la login!");
        }

        return users;
    }

    @Override
    public Utilizator findByUsername(String username) {
        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(";");
                if(split.length < 9) {
                    continue;
                }

                if(split[1].equals(username)) {
                    Utilizator user = new Utilizator();
                    user.setName(split[0]);
                    user.setUsername(split[1]);
                    user.setHashedPassword(split[2]);
                    user.setOcupation(split[3].isEmpty() ? null : split[3]);
                    //Inlocuiesc "\\n" cu "\n" pentru descriere
                    user.setProfileDescription(split[4].isEmpty() ? null : split[4].replace("\\n", "\n"));
                    user.setPicture(split[5].isEmpty() ? null : split[5]);
                    user.setNumberOfPosts(Integer.parseInt(split[6]));
                    user.setNumberOfFollowers(Integer.parseInt(split[7]));
                    user.setNumberOfFollowing(Integer.parseInt(split[8]));

                    return user;
                }
            }
        }
        catch(IOException e){
            throw new RepoException("A aparut o problema la gasirea contului de utilizator!");
        }
        return null;
    }

    @Override
    public void update(Utilizator entity, Utilizator newEntity) {
        File inputFile = new File(FILE_PATH);
        File tempFile = new File(FILE_PATH + ".tmp");
        boolean updated;

        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))){

            String line;
            updated = false;

            while((line = reader.readLine()) != null){
                String[] split = line.split(";");
                if(split[1].equals(entity.getUsername())) {
                    // SCriu utilizatorul actualizat
                    line = String.join(";",
                            newEntity.getName(),
                            newEntity.getUsername(),
                            newEntity.getHashedPassword(),
                            newEntity.getOcupation() == null ? "" : newEntity.getOcupation(),
                            newEntity.getProfileDescription() == null ? "" : newEntity.getProfileDescription().replace("\n", "\\n"),
                            newEntity.getPicture() == null ? "" : newEntity.getPicture(),
                            String.valueOf(newEntity.getNumberOfPosts()),
                            String.valueOf(newEntity.getNumberOfFollowers()),
                            String.valueOf(newEntity.getNumberOfFollowing())
                    );
                    updated = true;
                }

                writer.write(line);
                writer.newLine();
            }
        }
        catch(IOException e){
            throw new RepoException("A aparut o problema la salvarea modificarilor!");
        }

        if(!updated){
            throw new RepoException("A aparut o problema la salvarea modificarilor!");
        }

        // Inlocuiesc fisierul vechi cu cel nou
        if(!inputFile.delete()){
            throw new RepoException("A aparut o problema la salvarea modificarilor! (Fisierul original nu poate fi sters)");
        }

        if (!tempFile.renameTo(inputFile)) {
            throw new RepoException("A aparut o problema la salvarea modificarilor! (Fisierul temporar nu poate fi redenumit)");
        }
    }
}
