package com.bioinnovate.Template.backend.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Disease extends AbstractEntity {

    private int omimID;
    private String diseaseName;
    private String  diseaseSymbol;
    private String alternativeNames;
    private String inheritance;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Gene> genes;
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Symptom> symptoms;
    private String genomicLocation;
    private String summary;
    private String classification;
    private String links;
    private String centers;
    private String supportGroups;
    private String diseasesReferences;

    public Disease() {
    }

    public List<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    public int getOmimID() {
        return omimID;
    }

    public void setOmimID(int omimID) {
        this.omimID = omimID;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getDiseaseSymbol() {
        return diseaseSymbol;
    }

    public void setDiseaseSymbol(String diseaseSymbol) {
        this.diseaseSymbol = diseaseSymbol;
    }

    public String getAlternativeNames() {
        return alternativeNames;
    }

    public void setAlternativeNames(String alternativeNames) {
        this.alternativeNames = alternativeNames;
    }

    public String getInheritance() {
        return inheritance;
    }

    public void setInheritance(String inheritance) {
        this.inheritance = inheritance;
    }

    public List<Gene> getGenes() {
        return genes;
    }

    public void setGenes(List<Gene> genes) {
        this.genes = genes;
    }

    public String getGenomicLocation() {
        return genomicLocation;
    }

    public void setGenomicLocation(String genomicLocation) {
        this.genomicLocation = genomicLocation;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getCenters() {
        return centers;
    }

    public void setCenters(String centers) {
        this.centers = centers;
    }

    public String getSupportGroups() {
        return supportGroups;
    }

    public void setSupportGroups(String supportGroups) {
        this.supportGroups = supportGroups;
    }

    public String getDiseasesReferences() {
        return diseasesReferences;
    }

    public void setDiseasesReferences(String diseasesReferences) {
        this.diseasesReferences = diseasesReferences;
    }
}
