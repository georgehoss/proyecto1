package com.tenneco.tennecoapp.Model;

/**
 * Created by ghoss on 19/12/2018.
 */
public class Template {
    public static final String DB_TEMPLATE = "templates";
    /*public static final String DOWNTIME = "templates";
    public static final String FTQ = "leak";
    public static final String REJECTS = "rejects";
    public static final String REPORTS = "reports";
    public static final String SHIFT = "shift";*/
    private String subject;
    private String body1;
    private String body2;

    public Template() {
    }

    public Template(String subject, String body1, String body2) {
        this.subject = subject;
        this.body1 = body1;
        this.body2 = body2;
    }



    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody1() {
        return body1;
    }

    public void setBody1(String body1) {
        this.body1 = body1;
    }

    public String getBody2() {
        return body2;
    }

    public void setBody2(String body2) {
        this.body2 = body2;
    }
}
