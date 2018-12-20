package com.tenneco.tennecoapp.Model;

import java.util.Comparator;

/**
 * Created by ghoss on 06/12/2018.
 */
public class Product {
    public static final String DB = "products";
    private String name;
    private String id;
    private String description;
    private String code;
    private Shift first;
    private Shift second;
    private Shift third;

    public Product() {
    }

    public Product(String name, String id, String description, String code) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.code = code;
    }

    public static Comparator<Product> NameComparator = new Comparator<Product>() {

        public int compare(Product us1, Product us2) {
            String name1 = us1.getName().toUpperCase();
            String name2 = us2.getName().toUpperCase();

            //ascending order
            return name1.compareTo(name2);

            //descending order
            //return name2.compareTo(name1);
        }
    };

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public Shift getFirst() {
        return first;
    }

    public void setFirst(Shift first) {
        this.first = first;
    }

    public Shift getSecond() {
        return second;
    }

    public void setSecond(Shift second) {
        this.second = second;
    }

    public Shift getThird() {
        return third;
    }

    public void setThird(Shift third) {
        this.third = third;
    }

    @Override
    public String toString() {
        return getName();
    }
}
