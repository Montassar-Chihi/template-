package com.bioinnovate.Template.backend.repositories;

import com.bioinnovate.Template.backend.entities.Disease;
import com.bioinnovate.Template.backend.entities.Gene;
import com.bioinnovate.Template.backend.entities.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiseaseRepository extends JpaRepository<Disease, Integer> {

    List<Disease> findByDiseaseNameContainingIgnoreCase(String diseaseName);
    List<Disease> findByInheritanceContainingIgnoreCase(String inheritance);
    List<Disease> findByGenomicLocationContainingIgnoreCase(String genomicLocation);
    List<Disease> findByOmimID(int omimID);
    List<Disease> findBySymptomsIn(List<Symptom> symptoms);
    List<Disease> findByGenesIn(List<Gene> genes);

    @Query("SELECT DISTINCT inheritance FROM Disease")
    List<String> findDistinctInheritance();

    @Query("SELECT DISTINCT genomicLocation FROM Disease")
    List<String> findDistinctGenomicLocation();

    @Query("SELECT DISTINCT classification FROM Disease")
    List<String> findDistinctClassification();


    List<Disease> findByInheritance(String inheritance);

    List<Disease> findByGenes(Gene genes);

    List<Disease> findByGenomicLocation(String genomicLocation);

    @Query(value = "SELECT * FROM `disease` " +
            "WHERE classification LIKE %:classification% "
            ,nativeQuery = true)
    List<Disease> findByClassification(@Param("classification") String classification);

    @Query(value = "SELECT DISTINCT(genomic_location) FROM `disease` "
            ,nativeQuery = true)
    List<String> findGenomicLocations();

    @Query(value = "SELECT DISTINCT(inheritance) FROM `disease` "
            ,nativeQuery = true)
    List<String> findInheritances();

    @Query(value = "SELECT DISTINCT(classification) FROM `disease` "
            ,nativeQuery = true)
    List<String> findCategories();
}
