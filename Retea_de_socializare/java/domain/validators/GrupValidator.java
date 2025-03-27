package com.example.retea_de_socializare.domain.validators;

import com.example.retea_de_socializare.domain.CererePrietenie;
import com.example.retea_de_socializare.domain.Grup;
import com.example.retea_de_socializare.exceptions.ValidationException;

public class GrupValidator implements Validator<Grup>{

    private final Validator<Grup> grupValidator = (Grup entity) -> {
        if(entity == null){
            throw new ValidationException("Detaliile grupului nu pot fi null. \n");
        }

        if(entity.getNumeGrup() == null){
            throw new ValidationException("Grupul trebuie sa aiba un nume. \n");
        }

        if(entity.getMembri().size() < 3){
            throw new ValidationException("Un grup trebuie sa aiba minim 3 membri.\n");
        }
    };


    @Override
    public void validate(Grup entity) throws ValidationException {
        grupValidator.validate(entity);
    }
}
