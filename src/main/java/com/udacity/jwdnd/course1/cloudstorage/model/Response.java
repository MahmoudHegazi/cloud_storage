package com.udacity.jwdnd.course1.cloudstorage.model;

public class Response {
    public boolean success;
    public String message;

    public Response(boolean success, String message){
        this.success = success;
        this.message = message;
    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
