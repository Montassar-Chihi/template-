package com.bioinnovate.PreMediT.backend.services;

import com.bioinnovate.PreMediT.backend.entities.Symptom;
import com.bioinnovate.PreMediT.backend.repositories.SymptomRepository;
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
