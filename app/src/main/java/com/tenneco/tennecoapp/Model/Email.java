package com.tenneco.tennecoapp.Model;

import java.util.Comparator;

/**
 * Created by ghoss on 26/09/2018.
 */
public class Email {
    public static final String DB = "emails";
    private String id;
    private String email;
    private String name;
    private int type;

    public Email() {
    }

    public static Comparator<Email> EmailNameComparator = new Comparator<Email>() {

        public int compare(Email us1, Email us2) {
            String user1 = us1.getName().toUpperCase();
            String user2 = us2.getName().toUpperCase();

            //ascending order
            return user1.compareTo(user2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

    public Email(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
