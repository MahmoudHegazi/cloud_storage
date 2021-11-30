package com.udacity.jwdnd.course1.cloudstorage.model;

public class CredentialsEditForm {
    private Integer edit_credential_id;
    private String edit_credential_url;
    private String edit_credential_username;
    private String edit_credential_password;

    public Integer getEdit_credential_id() {
        return edit_credential_id;
    }

    public void setEdit_credential_id(Integer edit_credential_id) {
        this.edit_credential_id = edit_credential_id;
    }

    public String getEdit_credential_url() {
        return edit_credential_url;
    }

    public void setEdit_credential_url(String edit_credential_url) {
        this.edit_credential_url = edit_credential_url;
    }

    public String getEdit_credential_username() {
        return edit_credential_username;
    }

    public void setEdit_credential_username(String edit_credential_username) {
        this.edit_credential_username = edit_credential_username;
    }

    public String getEdit_credential_password() {
        return edit_credential_password;
    }

    public void setEdit_credential_password(String edit_credential_password) {
        this.edit_credential_password = edit_credential_password;
    }
}
