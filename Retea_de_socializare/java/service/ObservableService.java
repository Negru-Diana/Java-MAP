package com.example.retea_de_socializare.service;

import com.example.retea_de_socializare.domain.*;
import com.example.retea_de_socializare.exceptions.ServiceException;
import com.example.retea_de_socializare.utils.dto.FilterDTO;
import com.example.retea_de_socializare.utils.dto.FriendDTO;
import com.example.retea_de_socializare.utils.events.ChangeEventType;
import com.example.retea_de_socializare.utils.events.EntityChangeEvent;
import com.example.retea_de_socializare.utils.events.Event;
import com.example.retea_de_socializare.utils.observer.Observable;
import com.example.retea_de_socializare.utils.observer.Observer;
import com.example.retea_de_socializare.utils.paging.Page;
import com.example.retea_de_socializare.utils.paging.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class ObservableService implements Observable<EntityChangeEvent> {

    private Service service;
    private List<Observer<EntityChangeEvent>> observers = new ArrayList<>();
    private List<Utilizator> activeUsers = new ArrayList<>();

    public ObservableService(Service service) {
        this.service = service;
    }

    public void addActivUser(String username){
        notifyObservers(new EntityChangeEvent(ChangeEventType.ADD, null));
        activeUsers.add(findByUsername(username));
    }

    public void removeActivUser(String username){
        notifyObservers(new EntityChangeEvent(ChangeEventType.DELETE, null));
        activeUsers.remove(findByUsername(username));
    }

    public boolean isActiveUser(String username){
        return activeUsers.contains(findByUsername(username));
    }

    public void addObserver(Observer<EntityChangeEvent> observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("Observer adăugat!");
        }
    }

    public void removeObserver(Observer<EntityChangeEvent> observer) {
        System.out.println("Vreau sa sterg observerul");
        if (observers.contains(observer)) {
            observers.remove(observer); // Îndepărtează observer-ul din lista de observatori
            System.out.println("Observer eliminat!");
        }
    }

    public void notifyObservers(EntityChangeEvent event) {
        System.out.println(observers.size());
        for (Observer<EntityChangeEvent> observer : observers) {
            System.out.println("Notificare primita si trimisa mai departe: " + event);
            observer.update(event);
        }
    }

    public void addUtilizator(String firstName, String lastName, String userName, String parola) {
        try{
            service.addUtilizator(firstName, lastName, userName, parola);
            System.out.println("Trimit notificare pentru adaugarea unui utilizator");
            notifyObservers(new EntityChangeEvent(ChangeEventType.ADD, null));
        }
       catch (ServiceException e) {
            e.printStackTrace();
       }
    }

    public Utilizator removeUtilizator(String userName) {
        Utilizator removedUser = service.removeUtilizator(userName);
        System.out.println("Trimit notificare pentru stergerea unui utilizator");
        notifyObservers(new EntityChangeEvent(ChangeEventType.DELETE, null));
        return removedUser;
    }

    public void addPrieten(String username1, String username2) {
        service.addPrieten(username1, username2); // Apel delegat
        System.out.println("Trimit notificare pentru adaugarea unui prieten");
        notifyObservers(new EntityChangeEvent(ChangeEventType.ADD, null));
    }

    public Prietenie removePrieten(String username1, String username2) {
        Prietenie removed =  service.removePrieten(username1, username2); // Apel delegat
        System.out.println("Trimit notificare pentru stergerea unui prieten");
        notifyObservers(new EntityChangeEvent(ChangeEventType.DELETE, null));
        return removed;
    }

    public void addCererePrietenie(String usernameCurrent, Utilizator userSelected) {
        service.addCererePrietenie(usernameCurrent, userSelected); // Apel delegat
        System.out.println("Trimit notificare pentru adaugarea unei cereri de prietenie");
        notifyObservers(new EntityChangeEvent(ChangeEventType.ADD, userSelected));
    }

    public void acceptRequest(CererePrietenie request) {
        service.acceptRequest(request); // Apel delegat
        System.out.println("Trimit notificare pentru acceptarea unei cereri de prietenie");
        notifyObservers(new EntityChangeEvent(ChangeEventType.UPDATE, null));
    }

    public void declineRequest(CererePrietenie request) {
        service.declineRequest(request); // Apel delegat
        System.out.println("Trimit notificare pentru refuzarea unei cereri de prietenie");
        notifyObservers(new EntityChangeEvent(ChangeEventType.UPDATE, null));
    }


    public Utilizator findByUsername(String userName){
        return service.findByUsername(userName);
    }

    public List<FriendDTO> getAllFriendsWithDates(String username){
        return service.getAllFriendsWithDates(username);
    }

    public Page<FriendDTO> getAllFriendsWithDatesPage(String username, Pageable pageable){
        return service.getAllFriendsWithDatesPage(username, pageable);
    }

    public Iterable<Utilizator> getPossibleFriends(String username){
        return service.getPossibleFriends(username);
    }

    public Page<Utilizator> getPossibleFriendsPage(String username, Pageable pageable){
        return service.getPossibleFriendsPage(username, pageable);
    }

    public Iterable<Utilizator> getMutualFriends(String currentUsername, Utilizator selectedUser){
        return service.getMutualFriends(currentUsername, selectedUser);
    }

    public Iterable<Utilizator> getSearchPossibleFriends(String username, String searchString){
        return service.getSearchPossibleFriends(username, searchString);
    }

    public Page<Utilizator> getSearchPossibleFriendsPage(String username, Pageable pageable, FilterDTO filter){
        return service.getSearchPossibleFriendsPage(username, pageable, filter);
    }

    public Iterable<CererePrietenie> getRequestsReceived(String username){
        return service.getRequestsReceived(username);
    }

    public Iterable<CererePrietenie> getRequestsSent(String username){
        return service.getRequestsSent(username);
    }

    public void deleteRequest(CererePrietenie request){
        service.deleteRequest(request);
        System.out.println("Trimit notificare pentru stergerea unei cereri de prietenie");
        notifyObservers(new EntityChangeEvent(ChangeEventType.UPDATE, null));
    }

    public boolean autentificare(String username, String password){
        return service.autentificare(username, password);
    }


    public void createGroup(String nume, List<Utilizator> members){
        try {
            service.createGroup(nume,members);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }


    public List<Utilizator> getAllFriends(String username){
        try{
            return service.getAllFriends(username);
        }
        catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }


    public List<Grup> getGrupuriUtilizator(String username){
        return service.getGrupuriUtilizator(username);
    }


    public void saveMessage(String from, Entity to, String message, Long reply){
        try{
            Utilizator fromUt = service.findByUsername(from);
            service.saveMessage(fromUt,to,message,reply);
        }
        catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
        notifyObservers(new EntityChangeEvent(ChangeEventType.ADD, null));
    }


    public List<Message> getMessagesChat(Entity entity, String username){
        return service.getMessagesChat(entity, username);
    }

    public int getAllNumberFriendsWithDatesPage(String username){
        return  service.getAllNumberFriendsWithDatesPage(username);
    }
}
