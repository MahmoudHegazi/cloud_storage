package com.udacity.jwdnd.course1.cloudstorage.model;

import javax.persistence.*;
import java.util.List;


@Table(name = "USERS", uniqueConstraints = {
        @UniqueConstraint(name = "uc_user_user_id", columnNames = {"user_id"})
})
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    private String username;
    private String salt;
    private String password;
    private String first_name;
    private String last_name;


    public User(Integer userId, String username, String salt, String password, String first_name, String last_name) {
        this.userId = userId;
        this.username = username;
        this.salt = salt;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String lastName) {
        this.last_name = last_name;
    }

}
