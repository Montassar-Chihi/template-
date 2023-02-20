package com.bioinnovate.PreMediT.backend.services;

import com.bioinnovate.PreMediT.backend.entities.Disease;
import com.bioinnovate.PreMediT.backend.entities.Gene;
import com.bioinnovate.PreMediT.backend.entities.Mutation;
import com.bioinnovate.PreMediT.backend.entities.Symptom;
import com.bioinnovate.PreMediT.backend.repositories.DiseaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.artur.helpers.CrudService;

import java.util.List;

@Service
@Transactional
public class DiseaseService extends CrudService<Disease, Integer> {
    private DiseaseRepository repository;

    public DiseaseService(@Autowired DiseaseRepository repository) { this.repository=repository; }

    @Override
    protected DiseaseRepository getRepository() {
        return repository;
    }

    public List<Disease> findByDiseaseNameContainingIgnoreCase(String diseaseName) {
        return repository.findByDiseaseNameContainingIgnoreCase(diseaseName);
    }

    public List<Disease> findByInheritanceContainingIgnoreCase(String inheritance) {
        return repository.findByInheritanceContainingIgnoreCase(inheritance);
    }

    public List<Disease> findByGenomicLocationContainingIgnoreCase(String genomicLocation) {
        return repository.findByGenomicLocationContainingIgnoreCase(genomicLocation);
    }

    public List<Disease> findByInheritance(String inheritance) {
        return repository.findByInheritance(inheritance);
    }

    public List<Disease> findByOmimID(int omimID) {
        return repository.findByOmimID(omimID);
    }

    public List<String> findDistinctInheritance() {
        return repository.findDistinctInheritance();
    }

    public List<String> findDistinctGenomicLocation() {
        return repository.findDistinctGenomicLocation();
    }

    public List<Disease> findByGenes(List<Gene> genes) {
        return repository.findByGenesIn(genes);
    }

    public List<Disease> findByMutations(List<Mutation> mutations) {
        return repository.findByMutationsIn(mutations);
    }

    public List<Disease> findBySymptomsIn(List<Symptom> symptoms) {
        return repository.findBySymptomsIn(symptoms);
    }

    public List<Disease> findByClassification(String classification) {
        return repository.findByClassification(classification);
    }
    
    public List<Disease> searchByExpression(String name){return repository.findByDiseaseNameContainingIgnoreCase(name);}

    public List<Disease> searchByClassification(String classification){return repository.findByClassification(classification);}

    public List<Disease> searchByGene(Gene gene){return repository.findByGenes(gene);}

    public List<Disease> findDiseasesByMutation(Mutation mutation){return repository.findByMutations(mutation);}

    public List<Disease> searchByOmimID(int omimID){return repository.findByOmimID(omimID);}

    public List<Disease> searchDiseases(
            String diseaseName,
            String diseaseSymbol,
            String alternativeNames,
            String inheritance,
            List<Gene> genes,
            List<Mutation> mutations,
            List<Symptom> symptoms,
            String genomicLocation,
            String summary,
            String classification,
            String links,
            String centers,
            String supportGroups,
            String diseasesReferences) {
        return repository.searchDiseases(diseaseName, diseaseSymbol, alternativeNames, inheritance, genes, mutations, symptoms, genomicLocation, summary, classification, links, centers, supportGroups, diseasesReferences);
    }

    public List<Disease> searchByInheritance(String inheritance){return repository.findByInheritance(inheritance);}

    public List<Disease> searchByGenomicLocation(String genomicLocation){return repository.findByGenomicLocation(genomicLocation);}

    public List<String> findInheritances(){return repository.findInheritances();}

    public List<String> findGenomicLocations(){return repository.findGenomicLocations();}

    public List<String> findCategories(){return repository.findCategories();}

    public List findAll(){
        return  repository.findAll();
    }
}
