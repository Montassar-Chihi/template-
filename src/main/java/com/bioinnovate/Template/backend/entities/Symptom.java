package com.bioinnovate.Template.backend.entities;

import javax.persistence.Entity;

@Entity
public class Symptom extends AbstractEntity {
    private String description;
    private String hpoTerm;
    private String hpoID;
    private String frequency;
    private String clinicalSynopsisOMIM;

    public Symptom() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHpoTerm() {
        return hpoTerm;
    }

    public void setHpoTerm(String hpoTerm) {
        this.hpoTerm = hpoTerm;
    }

    public String getHpoID() {
        return hpoID;
    }

    public void setHpoID(String hpoID) {
        this.hpoID = hpoID;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getClinicalSynopsisOMIM() {
        return clinicalSynopsisOMIM;
    }

    public void setClinicalSynopsisOMIM(String clinicalSynopsisOMIM) {
        this.clinicalSynopsisOMIM = clinicalSynopsisOMIM;
    }
}
