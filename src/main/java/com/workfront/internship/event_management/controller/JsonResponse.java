package com.workfront.internship.event_management.controller;

/**
 * Created by Hermine Turshujyan 8/23/16.
 */
public class JsonResponse {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    private String status = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message = null;
    private Object result = null;

}
