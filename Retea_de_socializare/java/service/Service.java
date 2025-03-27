package com.example.retea_de_socializare.service;

import com.example.retea_de_socializare.domain.*;
import com.example.retea_de_socializare.domain.validators.CererePrietenieValidator;
import com.example.retea_de_socializare.domain.validators.PrietenieValidator;
import com.example.retea_de_socializare.exceptions.ServiceException;
import com.example.retea_de_socializare.repository.Repository;
import com.example.retea_de_socializare.repository.database.GrupDBRepository;
import com.example.retea_de_socializare.repository.database.MessageDBRepository;
import com.example.retea_de_socializare.repository.database.PrietenieDBRepository;
import com.example.retea_de_socializare.repository.database.UtilizatorDBRepository;
import com.example.retea_de_socializare.utils.dto.FilterDTO;
import com.example.retea_de_socializare.utils.dto.FriendDTO;
import com.example.retea_de_socializare.utils.events.ChangeEventType;
import com.example.retea_de_socializare.utils.events.EntityChangeEvent;
import com.example.retea_de_socializare.utils.events.Event;
import com.example.retea_de_socializare.utils.observer.Observer;
import com.example.retea_de_socializare.utils.paging.Page;
import com.example.retea_de_socializare.utils.paging.Pageable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service{

    private Repository userRepo;
    private Repository friendshipRepo;
    private Repository requestsRepo;
    private Repository groupsRepo;
    private Repository messageRepo;


    public Service(Repository userRepo, Repository friendshipRepo, Repository requestsRepo, Repository groupsRepo, Repository messageRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        this.requestsRepo = requestsRepo;
        this.groupsRepo = groupsRepo;
        this.messageRepo = messageRepo;
    }



    /*
    ADD/REMOVE UTILIZATOR
     */

    public void addUtilizator(String firstName, String lastName, String userName, String parola) {
        try{
            Utilizator newUt = new Utilizator(firstName, lastName, userName, parola);
            newUt.setId(getRandomId());

            if(findByUsername(newUt.getUserName()) != null) {
                throw new ServiceException("Utilizatorul exista deja sau username-ul este deja utilizat!\n");
            }

            userRepo.save(newUt);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }


    public Utilizator removeUtilizator(String userName) {

        // Găsește utilizatorul pe baza username-ului
        Utilizator foundUt = findByUsername(userName);

        if (foundUt == null) {
            throw new ServiceException("Utilizatorul nu a fost găsit.");
        }

        // 1. Șterge toate prieteniile asociate utilizatorului
        List<Prietenie> friendshipsToRemove = new ArrayList<>();
        friendshipRepo.findAll().forEach(entity -> {
            if (entity instanceof Prietenie) {
                Prietenie prietenie = (Prietenie) entity;
                if (prietenie.getUtilizator1().equals(foundUt) || prietenie.getUtilizator2().equals(foundUt)) {
                    friendshipsToRemove.add(prietenie);
                }
            }
        });

        // Elimina toate prieteniile gasite
        friendshipsToRemove.forEach(prietenie -> removePrieten(prietenie.getUtilizator1().getUserName(), prietenie.getUtilizator2().getUserName()));

        // 2. Șterge toate cererile de prietenie asociate utilizatorului
        List<CererePrietenie> requestsToRemove = new ArrayList<>();
        requestsRepo.findAll().forEach(entity -> {
            if (entity instanceof CererePrietenie) {
                CererePrietenie cerere = (CererePrietenie) entity;
                if (cerere.getUtilizator1().equals(foundUt) || cerere.getUtilizator2().equals(foundUt)) {
                    requestsToRemove.add(cerere);
                }
            }
        });

        // Șterge cererile de prietenie
        requestsToRemove.forEach(cerere -> requestsRepo.delete(cerere.getId()));

        //3. Actualizeaza mesajele (msgfrom, msgto -> utilizator "unknown")
        ((MessageDBRepository) messageRepo).updateMessagesFromUser(foundUt.getId(), 0L);

        // 4. Sterg utilizatorul din grupuri
        ((GrupDBRepository) groupsRepo).deleteUserFromGroups(foundUt.getId());



        // 5. Șterge utilizatorul din repository
        userRepo.delete(foundUt.getId());

        // Returnează utilizatorul șters pentru eventuale verificări
        return foundUt;
    }




    /*
    ADD/REMOVE PRIETEN
     */

    public void addPrieten(String username1, String username2) {
        Utilizator u1 = findByUsername(username1);
        Utilizator u2 = findByUsername(username2);
        Prietenie p = new Prietenie(u1,u2);
        p.setId(getRandomId());

        PrietenieValidator validator = new PrietenieValidator();
        validator.validate(p);

        if(existaPrietnie(u1,u2)){
            throw new ServiceException("Prietenia exista deja!");
        }

        friendshipRepo.save(p);
    }



    public Prietenie removePrieten(String username1, String username2) {
        Utilizator u1 = findByUsername(username1);
        Utilizator u2 = findByUsername(username2);

        if (u1 == null || u2 == null) {
            throw new ServiceException("Utilizatorii nu au fost gasiti!");
        }

        Prietenie p = findPrietenie(u1,u2);
        if(p == null) {
            throw new ServiceException("Prietenia nu a fost gasita.");
        }

        if(friendshipRepo.delete(p.getId()) != null){
            List<Utilizator> friends1 = u1.getFriends();
            List<Utilizator> friends2 = u2.getFriends();

            if (friends1 != null) {
                friends1.remove(u2);
            }
            if (friends2 != null) {
                friends2.remove(u1);
            }

            userRepo.update(u1);
            userRepo.update(u2);

            CererePrietenie cerere = findCererePrietenie(u1,u2);
            cerere.setStatus("no answer");
            deleteRequest(cerere);

            return p;
        }
        else{
            return null;
        }
    }




    /*
    ADD/REMOVE CERERE PRIETENIE
     */

    public void addCererePrietenie(String usernameCurrent, Utilizator userSelected){
        try{
            Utilizator userCurrent = findByUsername(usernameCurrent);

            LocalDateTime data = LocalDateTime.now();
            String status = "no answer";

            CererePrietenie cerere = new CererePrietenie(userCurrent, userSelected, data, status);
            cerere.setId(getRandomId());

            CererePrietenieValidator validator = new CererePrietenieValidator();
            validator.validate(cerere);

            if(existaCerere(userCurrent, userSelected) == true){
                CererePrietenie newCerere = findCererePrietenie(userCurrent,userSelected);
                if(newCerere.getStatus().equals("declined") && newCerere != null){
                    try{
                        requestsRepo.delete(newCerere.getId());
                    }
                    catch(Exception e){
                        e.printStackTrace();
                        throw new ServiceException("A aparut o eroare la trimiterea cererii de prietenie!\n");
                    }
                }
                else{
                    throw new ServiceException("Exista deja o cerere de prietenie intre cei doi utilizatori!");
                }
            }

            requestsRepo.save(cerere);

        }
        catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }


    public void deleteRequest(CererePrietenie request){
        if(!request.getStatus().equals("no answer")){
            throw new ServiceException("Cererea de prietenie nu mai poate fi stearsa!");
        }

        try{
            requestsRepo.delete(request.getId());

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("A aparut o problema la stergerea cererii de prietenie!");
        }
    }




    /*
    LOGIN
     */
    public boolean autentificare(String username, String password) {
        Utilizator ut = findByUsername(username);
        if (ut == null) {
            return false;
        }
        else if(password.equals(ut.getParola())){
            return true;
        }
        return false;
    }


    /*
    METODE AJUTATOARE PENTRU 'ADD NEW FRIENDS'
     */

    public Iterable<Utilizator> getPossibleFriends(String username) {
        Utilizator ut = findByUsername(username);

        if(ut == null){
            throw new ServiceException("A aparut o eroare la incarcarea posibililor prieteni!");
        }

        List<Utilizator> allUsers = new ArrayList<>();
        getAllUtilizatori().forEach(allUsers::add); //Obtin toti utilizatorii

        List<FriendDTO> friends = getAllFriendsWithDates(username);
        Set<Utilizator> existingFriends = new HashSet<>();
        for(FriendDTO friend : friends){ //Obtin toti Utilizatorii cu care este prieten deja
            existingFriends.add(friend.getPrieten());
        }

        // Returnez toti posibilii prieteni (fara prietenii actuali si fara el insusi)
        return allUsers.stream()
                .filter(user -> !existingFriends.contains(user))
                .filter(user -> !user.equals(ut))
                .toList();
    }


    public Page<Utilizator> getPossibleFriendsPage(String username, Pageable pageable){
        // Obține utilizatorul curent după username
        Utilizator foundUt = findByUsername(username);

        if (foundUt == null) {
            throw new ServiceException("Utilizatorul nu a fost găsit!");
        }

        // Creează un obiect FilterDTO cu id-ul utilizatorului curent
        FilterDTO filter = new FilterDTO();
        filter.setUserId(Optional.of(foundUt.getId()));

        // Apelăm repository-ul pentru a obține utilizatorii care nu sunt prieteni cu utilizatorul curent
        Page<Utilizator> possibleFriendsPage = ((UtilizatorDBRepository)userRepo).findAllOnPage(pageable, filter);

        return possibleFriendsPage;
    }


    public Iterable<Utilizator> getMutualFriends(String currentUsername, Utilizator selectedUser){
        List<FriendDTO> friendsCrt = getAllFriendsWithDates(currentUsername);
        List<FriendDTO> friendsSelected = getAllFriendsWithDates(selectedUser.getUserName());

        Set<Utilizator> friendsCrtSet = friendsCrt.stream()
                .map(FriendDTO::getPrieten)
                .collect(Collectors.toSet());

        Set<Utilizator> friendsSelectedSet = friendsSelected.stream()
                .map(FriendDTO::getPrieten)
                .collect(Collectors.toSet());

        friendsCrtSet.retainAll(friendsSelectedSet);

        return friendsCrtSet;
    }

    public Iterable<Utilizator> getSearchPossibleFriends(String username, String searchString){
        Iterable<Utilizator> possibleFriends = getPossibleFriends(username);

        String lowerCaseSearchString = searchString.toLowerCase();

        return StreamSupport.stream(possibleFriends.spliterator(), false)
                .filter(utilizator -> utilizator.getFullName().toLowerCase().contains(lowerCaseSearchString) ||
                        utilizator.getUserName().toLowerCase().contains(lowerCaseSearchString))
                .collect(Collectors.toList());
    }


    public Page<Utilizator> getSearchPossibleFriendsPage(String username, Pageable pageable, FilterDTO filter){
        Utilizator foundUt = findByUsername(username);

        if (foundUt == null) {
            throw new ServiceException("Utilizatorul nu a fost gasit!");
        }

        filter.setUserId(Optional.of(foundUt.getId()));

        // Apelăm repository-ul pentru a obține utilizatorii care nu sunt prieteni cu utilizatorul curent si corespund cautarii
        Page<Utilizator> possibleFriendsPage = ((UtilizatorDBRepository)userRepo).findAllOnPage(pageable, filter);

        return possibleFriendsPage;
    }


    /*
    METODE PENTRU 'FRIEND REQUESTS'
     */

    public Iterable<CererePrietenie> getRequestsReceived(String username){
        Utilizator ut = findByUsername(username);

        if (ut == null) {
            throw new IllegalArgumentException("A aparut o problema la incarcarea cererilor de prietenie primite!");
        }

        Iterable<CererePrietenie> allRequests = requestsRepo.findAll();
        List<CererePrietenie> receivedRequests = StreamSupport.stream(allRequests.spliterator(),false)
                .filter(request -> request.getUtilizator2().equals(ut))
                .collect(Collectors.toList());

        return receivedRequests;
    }

    public Iterable<CererePrietenie> getRequestsSent(String username){
        Utilizator ut = findByUsername(username);

        if (ut == null) {
            throw new IllegalArgumentException("A aparut o problema la incarcarea cererilor de prietenie trimise!");
        }

        Iterable<CererePrietenie> allRequests = requestsRepo.findAll();
        List<CererePrietenie> sentRequests = StreamSupport.stream(allRequests.spliterator(),false)
                .filter(request -> request.getUtilizator1().equals(ut))
                .collect(Collectors.toList());

        return sentRequests;
    }


    public void declineRequest(CererePrietenie request){
        if(!request.getStatus().equals("no answer")){
            throw new ServiceException("S-a oferit deja un raspuns pentru aceasta cerere de prietenie!");
        }

        try{
            CererePrietenie oldRequest = request;
            request.setStatus("declined");
            requestsRepo.update(request);
        } catch (Exception e) {
            throw new ServiceException("A aparut o problema la respingerea cererii de prietenie!");
        }
    }


    public void acceptRequest(CererePrietenie request){
        if(!request.getStatus().equals("no answer")){
            throw new ServiceException("S-a oferit deja un raspuns pentru aceasta cerere de prietenie!");
        }

        try{
            CererePrietenie oldRequest = request;
            request.setStatus("accepted");
            requestsRepo.update(request);

            addPrieten(request.getUtilizator1Username(),request.getUtilizator2Username());
        } catch (Exception e) {
            throw new ServiceException("A aparut o problema la acceptarea cererii de prietenie!");
        }
    }



    /*
    CREATE GROUP
     */

    public void createGroup(String nume, List<Utilizator> members){
        Grup grup = new Grup(nume);
        grup.setId(getRandomId());
        grup.setMembri(members);

        try {
            groupsRepo.save(grup);
        }
        catch (Exception e){
            throw  new ServiceException(e.getMessage());
        }

    }


    public List<Grup> getGrupuriUtilizator(String username){
        Iterable<Grup> grupuri = groupsRepo.findAll();

        List<Grup> grupuriUtilizator = new ArrayList<>();
        for(Grup grup : grupuri){
            if(grup.getMembri() != null && grup.getMembri().stream()
                    .anyMatch(utilizator -> utilizator.getUserName().equals(username))){
                grupuriUtilizator.add(grup);
            }
        }

        return grupuriUtilizator;
    }


    /*
    MESSAGE
     */

    public void saveMessage(Utilizator from, Entity to, String message, Long reply){
        Message mesaj = new Message(from, to, message, reply);
        mesaj.setId(getRandomId());

        if(reply != null){
            Optional<Message> replyMsg = messageRepo.findOne(reply);

            replyMsg.ifPresent(r -> mesaj.setReplyText(r.getMessage()));
        }

        try{
            messageRepo.save(mesaj);
        }
        catch (Exception e){
            throw  new ServiceException(e.getMessage());
        }
    }

    public List<Message> getMessagesChat(Entity entity, String username){
        try{
            Iterable<Message> allMessages = messageRepo.findAll();
            List<Message> messages = new ArrayList<>();

            Utilizator curentUt = findByUsername(username);

            // Dacă entity este un utilizator
            if (entity instanceof Utilizator) {
                Utilizator user = (Utilizator) entity;
                // Filtrăm mesajele private între curentUt și user
                for (Message message : allMessages) {
                    // Mesajele private trebuie să fie între curentUt și user
                    if ((message.getFrom().equals(curentUt) && message.getTo().equals(user)) ||
                            (message.getTo().equals(curentUt) && message.getFrom().equals(user))) {
                        messages.add(message);  // Adăugăm mesajul la listă
                    }
                }
            }
            // Dacă entity este un grup
            else if (entity instanceof Grup) {
                Grup group = (Grup) entity;
                // Filtrăm mesajele care au ca destinație grupul respectiv
                for (Message message : allMessages) {
                    // Verificăm dacă mesajul a fost trimis într-un grup
                    if (message.getTo() instanceof Grup && ((Grup) message.getTo()).getId().equals(group.getId())) {
                        messages.add(message);  // Adăugăm mesajul la listă
                    }
                }
                System.out.println(messages.size());
            }

            return messages;
        }
        catch (Exception e){
            throw  new ServiceException(e.getMessage());
        }
    }


    // METODE FOLOSITE PENTRU FUNCTIILE PRINCIPALE

    public Iterable<Utilizator> getAllUtilizatori() {
        return userRepo.findAll();
    }

    public List<FriendDTO> getAllFriendsWithDates(String username){
        Utilizator foundUt = findByUsername(username);  // gasesc utilizatorul cu username-ul dat

        if(foundUt == null){
            throw  new ServiceException("A aparut o problema la incarcarea prietenilor!");
        }

        List<FriendDTO> friendDTOS = new ArrayList<>();

        friendshipRepo.findAll().forEach(entity -> {
            if(entity instanceof Prietenie){
                Prietenie prietenie = (Prietenie) entity;

                if(prietenie.getUtilizator1().equals(foundUt)){
                    friendDTOS.add(new FriendDTO(prietenie.getUtilizator2(), prietenie.getFriendsfrom()));
                }
                else if(prietenie.getUtilizator2().equals(foundUt)){
                    friendDTOS.add(new FriendDTO(prietenie.getUtilizator1(), prietenie.getFriendsfrom()));
                }
            }
        });

        return friendDTOS;
    }

    public Page<FriendDTO> getAllFriendsWithDatesPage(String username, Pageable pageable) {
        Utilizator foundUt = findByUsername(username);

        if (foundUt == null) {
            throw new ServiceException("A aparut o problema la incarcarea prietenilor in format paginat!");
        }

        FilterDTO filterDTO = new FilterDTO();
        filterDTO.setUserId(Optional.of(foundUt.getId()));
        filterDTO.setFilterType(Optional.of("friends"));

        Page<Prietenie> prieteniePage = ((PrietenieDBRepository) friendshipRepo).findAllOnPage(pageable, filterDTO);

        List<FriendDTO> friendDTOS = StreamSupport.stream(prieteniePage.getElementsOfPage().spliterator(), false)
                .map(prietenie -> {
                    Utilizator friend = prietenie.getUtilizator1().equals(foundUt)
                            ? prietenie.getUtilizator2()
                            : prietenie.getUtilizator1();

                    return new FriendDTO(friend, prietenie.getFriendsfrom());
                }).collect(Collectors.toList());

        return new Page<>(friendDTOS, prieteniePage.getTotalNumberOfElements());

    }

    public int getAllNumberFriendsWithDatesPage(String username){
        Utilizator foundUt = findByUsername(username);

        if(foundUt == null){
            throw new ServiceException("A aparut o problema la incarcarea prietenilor in format paginat!");
        }

        FilterDTO filterDTO = new FilterDTO();
        filterDTO.setUserId(Optional.of(foundUt.getId()));
        filterDTO.setFilterType(Optional.of("friends"));
        return ((PrietenieDBRepository)friendshipRepo).count(filterDTO);
    }



    public List<Utilizator> getAllFriends(String username){
        Utilizator foundUt = findByUsername(username);  // gasesc utilizatorul cu username-ul dat

        if(foundUt == null){
            throw  new ServiceException("A aparut o problema la incarcarea prietenilor!");
        }

        List<Utilizator> friends = new ArrayList<>();
        friendshipRepo.findAll().forEach(entity -> {
            if(entity instanceof Prietenie){
                Prietenie prietenie = (Prietenie) entity;

                if(prietenie.getUtilizator1().equals(foundUt)){
                    friends.add(prietenie.getUtilizator2());
                }
                else if(prietenie.getUtilizator2().equals(foundUt)){
                    friends.add(prietenie.getUtilizator1());
                }
            }
        });

        return friends;
    }


    public Utilizator findByUsername(String userName) {
        //   AtomicReference este un instrument puternic pentru gestionarea referințelor în aplicații multi-threaded,
        //asigurându-se că modificările sunt realizate în mod sigur și eficient. Aceasta ajută la evitarea problemelor de sincronizare
        AtomicReference<Utilizator> foundUtilizator = new AtomicReference<>(); // Use AtomicReference

        userRepo.findAll().forEach(entity -> {
            if (entity instanceof Utilizator) {
                Utilizator utilizator = (Utilizator) entity;
                if (utilizator.getUserName().equalsIgnoreCase(userName)) {
                    foundUtilizator.set(utilizator); // Set the found user
                }
            }
        });

        return foundUtilizator.get();
    }


    private Set<Long> existingIds = new HashSet<>();

    private Long getRandomId(){
        Long randomId;
        int attempts = 0;
        final int maxAttempts = 1000;

        do {
            randomId = (long) (Math.random() * Long.MAX_VALUE);
            attempts++;
        } while (existingIds.contains(randomId) && attempts < maxAttempts);

        if (attempts >= maxAttempts) {
            throw new ServiceException("Failed to generate a unique ID after \n");
        }

        existingIds.add(randomId); // Add the newly generated ID to the set
        return randomId;
    }


    private Prietenie findPrietenie(Utilizator u1, Utilizator u2){
        Prietenie[] result = new Prietenie[1];

        friendshipRepo.findAll().forEach(entity -> {
            if (entity instanceof Prietenie) {
                Prietenie prietenie = (Prietenie) entity;
                if (prietenie.equals(new Prietenie(u1, u2))) {
                    result[0] = prietenie;
                }
            }
        });

        return result[0];
    }

    private boolean existaPrietnie(Utilizator u1, Utilizator u2){
        boolean[]  result = {false};

        friendshipRepo.findAll().forEach(entity -> {
            if (entity instanceof Prietenie) {
                Prietenie prietenie = (Prietenie) entity;
                if (prietenie.equals(new Prietenie(u1, u2))) {
                    result[0] = true;
                }
            }
        });
        return result[0];
    }

    private boolean existaCerere(Utilizator u1, Utilizator u2){
        boolean[] result = {false};

        requestsRepo.findAll().forEach(entity -> {
            if(entity instanceof CererePrietenie){
                CererePrietenie cerere = (CererePrietenie) entity;
                if(cerere.equals(new CererePrietenie(u1, u2, LocalDateTime.now(), "no answer"))){
                    result[0] = true;
                }
            }
        });
        return result[0];
    }

    private CererePrietenie findCererePrietenie(Utilizator u1, Utilizator u2) {
        CererePrietenie[] result = {null};

        requestsRepo.findAll().forEach(entity -> {
            if (entity instanceof CererePrietenie) {
                CererePrietenie cerere = (CererePrietenie) entity;

                if ((cerere.getUtilizator1().equals(u1) && cerere.getUtilizator2().equals(u2)) ||
                        (cerere.getUtilizator1().equals(u2) && cerere.getUtilizator2().equals(u1))) {
                    result[0] = cerere;  // setăm cererea găsită
                }
            }
        });

        return result[0];
    }

}
