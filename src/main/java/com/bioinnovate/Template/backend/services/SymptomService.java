package com.bioinnovate.Template.backend.services;

import com.bioinnovate.Template.backend.entities.Symptom;
import com.bioinnovate.Template.backend.repositories.SymptomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SymptomService {
    @Autowired
    private SymptomRepository symptomRepository;

    public List<Symptom> findAll() {
        return symptomRepository.findAll();
    }

}
