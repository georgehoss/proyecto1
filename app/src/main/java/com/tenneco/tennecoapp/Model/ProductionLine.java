package com.tenneco.tennecoapp.Model;

/**
 * Created by ghoss on 11/09/2018.
 */
public class ProductionLine {

    public static final String DB = "production_line";
    private Line line;
    private String date;
    private String id;

    public ProductionLine() {
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
