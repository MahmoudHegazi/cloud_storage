package com.udacity.jwdnd.course1.cloudstorage.model;

import javax.persistence.*;
import java.sql.Blob;


@Entity
@Table(name = "files")
public class Filea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer file_id;
    private String file_name;
    private String content_type;
    private String file_size;
    private Integer user_id;
    @Lob
    private Blob file_data;

    // @ManyToOne private Menu menu;

    //constructor
    public Filea(Integer file_id, String content_type, String file_name, String file_size,
                 Integer user_id, Blob file_data) {
        this.file_id = file_id;
        this.file_name = file_name;
        this.content_type = content_type;
        this.file_size = file_size;
        this.user_id = user_id;
        this.file_data = file_data;

    }



    public Integer getFile_id() {
        return file_id;
    }

    public void setFile_id(Integer file_id) {
        this.file_id = file_id;
    }


    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }


    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }


    public Blob getFile_data() {
        return file_data;
    }

    public void setFile_data(Blob file_data) {
        this.file_data = file_data;
    }
}
