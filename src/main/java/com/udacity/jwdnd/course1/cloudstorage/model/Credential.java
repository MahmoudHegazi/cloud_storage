package com.udacity.jwdnd.course1.cloudstorage.model;

import javax.persistence.*;

@Entity
@Table(name = "CREDENTIALS")
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer credentials_id;
    private String url;
    private String uname;
    private String secret_key;
    private String password;

    private Integer user_id;

    public Credential(Integer credentials_id, String url,String uname,  String secret_key, String password, Integer user_id){
        this.credentials_id = credentials_id;
        this.url = url;
        this.uname = uname;
        this.secret_key = secret_key;
        this.password = password;
        this.user_id = user_id;

    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getCredentials_id() {
        return credentials_id;
    }

    public void setCredentials_id(Integer credentials_id) {
        this.credentials_id = credentials_id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
