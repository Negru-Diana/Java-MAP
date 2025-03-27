package com.example.retea_de_socializare.service;

import com.example.retea_de_socializare.domain.*;
import com.example.retea_de_socializare.domain.validators.*;
import com.example.retea_de_socializare.exceptions.ServiceException;
import com.example.retea_de_socializare.repository.Repository;
import com.example.retea_de_socializare.repository.database.*;
import com.example.retea_de_socializare.utils.events.EntityChangeEvent;
import javafx.collections.ObservableList;


import java.sql.Connection;
import java.sql.SQLException;


public class ServiceFactory {

    // Instanța singleton pentru `ObservableService`
    private static ObservableService observableService;

    private ServiceFactory() {}

    // Creează ObservableService doar dacă nu a fost deja creat
    public static ObservableService createService() {
        if (observableService == null) {
            System.out.println("Am creat observable-ul");
            try {
                // Conexiune la baza de date
                Connection connection = DBConnection.getConnection();

                // Crearea repository-urilor
                Repository<Long, Utilizator> userRepo = new UtilizatorDBRepository(new UtilizatorValidator(), connection);
                Repository<Long, Prietenie> friendshipRepo = new PrietenieDBRepository(new PrietenieValidator(), connection, (UtilizatorDBRepository) userRepo);
                Repository<Long, CererePrietenie> requestsRepo = new CererePrietenieDBRepository(new CererePrietenieValidator(), connection, (UtilizatorDBRepository) userRepo);
                Repository<Long, Grup> groupsRepo = new GrupDBRepository(new GrupValidator(), connection, (UtilizatorDBRepository) userRepo);
                Repository<Long, Message> messageRepo = new MessageDBRepository(new MessageValidator(), connection, (UtilizatorDBRepository) userRepo, (GrupDBRepository) groupsRepo);

                // Crearea instanței de Service
                Service service = new Service(userRepo, friendshipRepo, requestsRepo, groupsRepo, messageRepo);

                // Împachetarea serviciului în ObservableService
                observableService = new ObservableService(service);

            } catch (SQLException e) {
                System.err.println("Eroare la conectarea la baza de date: " + e.getMessage());
                throw new ServiceException("A apărut o eroare la conectarea la baza de date!");
            }
        }

        return observableService;
    }

    // Returnează instanța singleton deja creată
    public static ObservableService getObservableService() {
        if (observableService == null) {
            createService();  // Dacă observabilul nu este creat, creează-l
        }
        else{
            System.out.println("Folosesc un observable deja creat!");
        }
        return observableService;
    }
}
