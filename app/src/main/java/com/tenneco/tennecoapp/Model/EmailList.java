package com.tenneco.tennecoapp.Model;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by ghoss on 08/12/2018.
 */
public class EmailList {
    public static final String DB = "email_list";
    private String name;
    private String id;
    private ArrayList<Email> emails;


    public static Comparator<EmailList> NameComparator = new Comparator<EmailList>  () {

        public int compare(EmailList us1, EmailList us2) {
            String user1 = us1.getName().toUpperCase();
            String user2 = us2.getName().toUpperCase();

            //ascending order
            return user1.compareTo(user2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

    public EmailList() {
    }

    public EmailList(String name, String id, ArrayList<Email> emails) {
        this.name = name;
        this.id = id;
        this.emails = emails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Email> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<Email> emails) {
        this.emails = emails;
    }

    @Override
    public String toString() {
        return getName();
    }
}
