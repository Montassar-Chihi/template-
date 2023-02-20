package com.bioinnovate.PreMediT.views.home;


public class Journal {
    private String name;
    private String link;
    private String type;
    private String author;
    private String subject;
    private String abstractOfJournal;
    private String date;

    public Journal(){
    }

    public Journal(String name, String link, String type, String author, String subject, String abstractOfJournal, String date) {
        this.name = name;
        this.link = link;
        this.type = type;
        this.author = author;
        this.subject = subject;
        this.abstractOfJournal = abstractOfJournal;
        this.date = date;
    }

    public String getAbstractOfJournal() {
        return abstractOfJournal;
    }

    public void setAbstractOfJournal(String abstractOfJournal) {
        this.abstractOfJournal = abstractOfJournal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
