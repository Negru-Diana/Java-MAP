package com.example.retea_de_socializare.domain.validators;

import com.example.retea_de_socializare.domain.Prietenie;
import com.example.retea_de_socializare.exceptions.ValidationException;

public class PrietenieValidator implements Validator<Prietenie>{

    //Functie lambda
    private final Validator<Prietenie> prietenieValidator = (Prietenie entity) -> {
        if(entity == null){
            throw new ValidationException("Prietenia nu poate fi null. \n");
        }

        if(entity.getUtilizator1() == null || entity.getUtilizator2() == null){
            throw new ValidationException("Utilizatorii nu au fost gasiti. \n");
        }

        if(entity.getUtilizator1().getId().equals(entity.getUtilizator2().getId())){
            throw new ValidationException("Un utilizator nu poate fi prieten cu el insusi. \n");
        }
    };


    @Override
    public void validate(Prietenie entity) throws ValidationException {
        prietenieValidator.validate(entity);
    }
}