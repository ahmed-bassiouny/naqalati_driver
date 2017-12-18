package com.bassiouny.naqalati_driver.model;

/**
 * Created by bassiouny on 18/12/17.
 */

public class Message {
    private String title;
    private String body;

    public String getTitle() {
        if (title == null)
            title = "";
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        if (body == null)
            body = "";
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
