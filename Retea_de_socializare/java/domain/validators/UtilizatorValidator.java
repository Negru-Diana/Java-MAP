package com.example.retea_de_socializare.domain.validators;

import com.example.retea_de_socializare.domain.Utilizator;
import com.example.retea_de_socializare.exceptions.ValidationException;

public class UtilizatorValidator implements Validator<Utilizator> {

    //Functie lambda
    private final Validator<Utilizator> utilizatorValidator = (Utilizator user) -> {
        if(user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getUserName().isEmpty() || user.getParola().isEmpty()){
            throw  new ValidationException("Utilizatorul nu este valid. \n");
        }
    };

    @Override
    public void validate(Utilizator entity) throws ValidationException {
        utilizatorValidator.validate(entity);
    }
}

