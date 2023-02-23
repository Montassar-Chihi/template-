package com.bioinnovate.Template.backend.repositories;

import com.bioinnovate.Template.backend.entities.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SymptomRepository extends JpaRepository<Symptom, Long> {
    List<Symptom> findByDescriptionContainingOrHpoTermContainingOrHpoIDContainingOrFrequencyContainingOrClinicalSynopsisOMIMContaining(String description, String hpoTerm, String hpoID, String frequency, String clinicalSynopsisOMIM);
}