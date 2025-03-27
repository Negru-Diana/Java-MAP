package com.example.retea_de_socializare.repository.database;

import com.example.retea_de_socializare.domain.Grup;
import com.example.retea_de_socializare.domain.Message;
import com.example.retea_de_socializare.domain.Utilizator;
import com.example.retea_de_socializare.domain.validators.Validator;
import com.example.retea_de_socializare.exceptions.RepoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDBRepository extends AbstractDBRepository<Long, Message>{

    private GrupDBRepository grupRepo;

    public MessageDBRepository(Validator<Message> validator, Connection connection, UtilizatorDBRepository utilizatorRepository, GrupDBRepository grupRepo) {
        super(validator, connection);
        this.utilizatorRepository = utilizatorRepository;
        this.grupRepo = grupRepo;
        //load();
    }

    @Override
    protected void load() {
        //Incarcarea mesajelor

        //Inacrc datele din tabela "mesaje"
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM mesaje")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Message mesaj = resultSetToEntity(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Eroare la încarcarea datelor din tabela mesaje.", e);
        }
    }


    @Override
    public Message resultSetToEntity(ResultSet resultSet) throws SQLException {

        Long id = resultSet.getLong("id");
        Long fromId = resultSet.getLong("msgfrom");
        Long toId = resultSet.getLong("msgto");
        String mesaj = resultSet.getString("mesaj");
        LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();
        Long replyId = resultSet.getLong("reply");

        // Găsește utilizatorul care a trimis mesajul
        Utilizator from = utilizatorRepository.findOne(fromId)
                .orElseThrow(() -> new RepoException("Utilizatorul cu ID " + fromId + " nu a fost găsit."));

        // Determină dacă `toId` este un utilizator sau un grup
        Optional<Utilizator> toUser = utilizatorRepository.findOne(toId);
        Optional<Grup> toGroup = grupRepo.findOne(toId);

        // Creează entitatea Message
        Message message;
        if (toUser.isPresent()) {
            message = new Message(from, toUser.get(), mesaj, replyId);
        } else if (toGroup.isPresent()) {
            message = new Message(from, toGroup.get(), mesaj, replyId);
        } else {
            throw new RepoException("Destinația cu ID " + toId + " nu a fost găsită.");
        }

        // Dacă există un reply, încarcă mesajul la care se răspunde și setează textul
        if (replyId != null && replyId != 0) {
            Optional<Message> replyMessage = findOne(replyId);  // Căutăm mesajul de reply
            replyMessage.ifPresent(r -> message.setReplyText(r.getMessage())); // Setăm textul mesajului la care se răspunde
        }

        message.setId(id);

        return message;
    }


    @Override
    public String entityToInsertSQL(Message entity) {
        return "INSERT INTO mesaje (id, msgfrom, msgto, mesaj, data, reply) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    public String entityToUpdateSQL(Message entity) {
        return "UPDATE mesaje SET msgfrom = ?, msgto = ?, mesaj = ?, data = ?, reply = ? WHERE id = ?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement statement, Message entity) throws SQLException {
        statement.setLong(1, entity.getId()); // ID-ul mesajului
        statement.setLong(2, entity.getFrom().getId()); // ID-ul expeditorului
        statement.setLong(3, (Long) entity.getTo().getId()); // ID-ul destinatarului
        statement.setString(4, entity.getMessage()); // Textul mesajului
        statement.setObject(5, entity.getData()); // Data trimiterii mesajului (ca LocalDateTime)
        if (entity.getReply() != null) {
            statement.setLong(6, entity.getReply()); // ID-ul mesajului la care se răspunde
        } else {
            statement.setObject(6, null); // Setează direct null
        }
    }

    @Override
    protected void setUpdateParameters(PreparedStatement statement, Message entity) throws SQLException {
        statement.setLong(1, entity.getFrom().getId());
        statement.setLong(2, (Long) entity.getTo().getId());
        statement.setString(3, entity.getMessage());
        statement.setObject(4, entity.getData()); // Folosim LocalDateTime direct
        if (entity.getReply() != null) {
            statement.setLong(5, entity.getReply());
        } else {
            statement.setObject(5, null); // Setăm null direct
        }
        statement.setLong(6, entity.getId());
    }

    @Override
    protected String getTableName() {
        return "mesaje";
    }

    @Override
    public Iterable<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM mesaje")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Message message = resultSetToEntity(resultSet);
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Eroare la incarcarea tuturor mesajelor din baza de date.", e);
        }
        return messages;
    }

    @Override
    public Optional<Message> findOne(Long id) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM mesaje WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSetToEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RepoException("Eroare la gasirea mesajului cu ID-ul " + id, e);
        }
        return Optional.empty();
    }


    public void updateMessagesFromUser(Long userId, Long unknownUserId) {
        // Pasul 1: Șterge conversațiile private (msgto nu este un grup)
        String deletePrivateMessagesSQL = "DELETE FROM mesaje WHERE (msgfrom = ? OR msgto = ?) AND msgto NOT IN (SELECT id FROM grupuri)";

        try (PreparedStatement deleteStatement = connection.prepareStatement(deletePrivateMessagesSQL)) {
            // Setăm utilizatorul de înlocuit pentru mesajele private
            deleteStatement.setLong(1, userId); // Căutăm mesaje trimise de utilizatorul care trebuie actualizat
            deleteStatement.setLong(2, userId); // Căutăm mesaje primite de utilizatorul care trebuie actualizat

            // Executăm ștergerea mesajelor private
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepoException("Eroare la ștergerea mesajelor private pentru utilizatorul cu ID " + userId, e);
        }

        // Pasul 2: Actualizează mesajele de grup (setăm msgfrom cu unknownUserId)
        String updateGroupMessagesSQL = "UPDATE mesaje SET msgfrom = ? WHERE msgfrom = ? AND msgto IN (SELECT id FROM grupuri)";

        try (PreparedStatement updateStatement = connection.prepareStatement(updateGroupMessagesSQL)) {
            // Setăm utilizatorul "necunoscut" ca expeditor pentru mesajele de grup
            updateStatement.setLong(1, unknownUserId); // Înlocuim cu unknownUserId
            updateStatement.setLong(2, userId); // Căutăm mesajele trimise de utilizatorul care trebuie actualizat

            // Executăm actualizarea mesajelor de grup
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepoException("Eroare la actualizarea mesajelor de grup pentru utilizatorul cu ID " + userId, e);
        }
    }


}
