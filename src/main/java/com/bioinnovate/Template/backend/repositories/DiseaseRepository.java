package com.bioinnovate.PreMediT.backend.repositories;

import com.bioinnovate.PreMediT.backend.entities.Disease;
import com.bioinnovate.PreMediT.backend.entities.Gene;
import com.bioinnovate.PreMediT.backend.entities.Mutation;
import com.bioinnovate.PreMediT.backend.entities.Symptom;
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
    List<Disease> findByMutationsIn(List<Mutation> mutations);

    @Query("SELECT DISTINCT inheritance FROM Disease")
    List<String> findDistinctInheritance();

    @Query("SELECT DISTINCT genomicLocation FROM Disease")
    List<String> findDistinctGenomicLocation();

    @Query("SELECT DISTINCT classification FROM Disease")
    List<String> findDistinctClassification();

    @Query("SELECT d FROM Disease d "
            + "LEFT JOIN d.genes g "
            + "LEFT JOIN d.mutations m "
            + "LEFT JOIN d.symptoms s "
            + "WHERE " +
            "(d.diseaseName like %:diseaseName% "
            + "OR d.diseaseSymbol like %:diseaseSymbol% "
            + "OR d.alternativeNames like %:alternativeNames% )"
            + "AND d.inheritance like %:inheritance% "
            + "AND g IN :geneName "
            + "AND m IN :mutationName "
            + "AND s IN :symptomHpoid "
            + "AND d.genomicLocation like %:genomicLocation% "
            + "AND d.summary like %:summary% "
            + "AND d.classification like %:classification% "
            + "AND d.links like %:links% "
            + "AND d.centers like %:centers% "
            + "AND d.supportGroups like %:supportGroups% "
            + "AND d.diseasesReferences like %:diseasesReferences%")
    List<Disease> searchDiseases(
            @Param("diseaseName") String diseaseName,
            @Param("diseaseSymbol") String diseaseSymbol,
            @Param("alternativeNames") String alternativeNames,
            @Param("inheritance") String inheritance,
            @Param("geneName") List<Gene> genes,
            @Param("mutationName") List<Mutation> mutations,
            @Param("symptomHpoid") List<Symptom> symptoms,
            @Param("genomicLocation") String genomicLocation,
            @Param("summary") String summary,
            @Param("classification") String classification,
            @Param("links") String links,
            @Param("centers") String centers,
            @Param("supportGroups") String supportGroups,
            @Param("diseasesReferences") String diseasesReferences);

    List<Disease> findByInheritance(String inheritance);

    List<Disease> findByGenes(Gene genes);

    List<Disease> findByMutations(Mutation mutations);

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
