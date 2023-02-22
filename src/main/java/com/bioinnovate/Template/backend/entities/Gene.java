package com.bioinnovate.PreMediT.backend.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Gene extends AbstractEntity {

    private String ncbiID;
    private String omimID;
    private String uniprotID;
    private String name;
    private String aliases;
    private String geneType;
    private String genomicLocation;
    private String function;
    private String expression;
    private String transcrits;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Disease> diseases;


    public Gene() {
    }

    public String getNcbiID() {
        return ncbiID;
    }

    public void setNcbiID(String ncbiID) {
        this.ncbiID = ncbiID;
    }

    public String getOmimID() {
        return omimID;
    }

    public void setOmimID(String omimID) {
        this.omimID = omimID;
    }

    public String getUniprotID() {
        return uniprotID;
    }

    public void setUniprotID(String uniprotID) {
        this.uniprotID = uniprotID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliases() {
        return aliases;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    public String getType() {
        return geneType;
    }

    public void setType(String geneType) {
        this.geneType = geneType;
    }

    public String getGenomicLocation() {
        return genomicLocation;
    }

    public void setGenomicLocation(String genomicLocation) {
        this.genomicLocation = genomicLocation;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getTranscrits() {
        return transcrits;
    }

    public void setTranscrits(String transcrits) {
        this.transcrits = transcrits;
    }

    public List<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }
}
