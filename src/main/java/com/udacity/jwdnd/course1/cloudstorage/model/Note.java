package com.udacity.jwdnd.course1.cloudstorage.model;

import javax.persistence.*;

@Entity
@Table(name = "NOTES")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer note_id;
    private String note_title;
    private String note_description;
    private Integer user_id;

    public Note(Integer note_id, String note_title, String note_description, Integer user_id) {
        this.note_id = note_id;
        this.note_title = note_title;
        this.note_description = note_description;
        this.user_id = user_id;
    }

    public Integer getNote_id() {
        return note_id;
    }

    public void setNote_id(Integer note_id) {
        this.note_id = note_id;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote_description() {
        return note_description;
    }

    public void setNote_description(String note_description) {
        this.note_description = note_description;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
