package com.tenneco.tennecoapp.Model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Line extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;
    private String code;
    private String date;
    private String parentId;
    private String plantId;
    private String p1;
    private String p2;
    private String p3;
    private String a1;
    private String a2;
    private String a3;
    private boolean isPline;
    private boolean isSchedule;

    public Line() {
    }

    public Line(String id, String name, String code, String date, String p1, String p2, String p3, String a1, String a2, String a3
            , boolean isPline,boolean isSchedule, String plantId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.date = date;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.isPline = isPline;
        this.plantId = plantId;
        this.isSchedule = isSchedule;
    }

    public String getPlantId() {
        return plantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public String getP3() {
        return p3;
    }

    public void setP3(String p3) {
        this.p3 = p3;
    }

    public String getA1() {
        return a1;
    }

    public void setA1(String a1) {
        this.a1 = a1;
    }

    public String getA2() {
        return a2;
    }

    public void setA2(String a2) {
        this.a2 = a2;
    }

    public String getA3() {
        return a3;
    }

    public void setA3(String a3) {
        this.a3 = a3;
    }

    public boolean isPline() {
        return isPline;
    }

    public void setPline(boolean pline) {
        isPline = pline;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public boolean isSchedule() {
        return isSchedule;
    }

    public void setSchedule(boolean schedule) {
        isSchedule = schedule;
    }
}
