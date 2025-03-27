package com.example.retea_de_socializare.domain.validators;

import com.example.retea_de_socializare.domain.CererePrietenie;
import com.example.retea_de_socializare.exceptions.ValidationException;

public class CererePrietenieValidator implements Validator<CererePrietenie>{

    private final Validator<CererePrietenie> prietenieValidator = (CererePrietenie entity) -> {
        if(entity == null){
            throw new ValidationException("Cererea de prietenie nu poate fi null. \n");
        }

        if(entity.getUtilizator1() == null || entity.getUtilizator2() == null || entity.getData() == null || entity.getStatus() == null){
            throw new ValidationException("Cerearea de prietenie nu a fost gasita. \n");
        }

        if(entity.getUtilizator1().getId().equals(entity.getUtilizator2().getId())){
            throw new ValidationException("Un utilizator nu isi poate trimite lui o cerere de prietenie. \n");
        }
    };


    @Override
    public void validate(CererePrietenie entity) throws ValidationException {
        prietenieValidator.validate(entity);
    }
}
