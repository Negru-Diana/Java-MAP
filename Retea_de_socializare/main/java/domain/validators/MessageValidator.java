package com.example.retea_de_socializare.domain.validators;

import com.example.retea_de_socializare.domain.Grup;
import com.example.retea_de_socializare.domain.Message;
import com.example.retea_de_socializare.exceptions.ValidationException;

public class MessageValidator implements Validator<Message> {

    private final Validator<Message> messageValidator = (Message entity) -> {
        if(entity == null){
            throw new ValidationException("Mesajul nu poate fi null. \n");
        }

        if(entity.getFrom() == null || entity.getTo() == null || entity.getMessage().isEmpty() || entity.getData() == null){
            throw new ValidationException("Detaliile mesajului sunt invalide! \n");
        }
    };


    @Override
    public void validate(Message entity) throws ValidationException {
        messageValidator.validate(entity);
    }
}
