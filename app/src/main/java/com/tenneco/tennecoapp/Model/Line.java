package com.tenneco.tennecoapp.Model;

import com.tenneco.tennecoapp.Model.Downtime.Downtime;
import com.tenneco.tennecoapp.Model.Downtime.Reason;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by ghoss on 11/09/2018.
 */
public class Line {
    public static final String DB_LINE = "line";
    public static final String DB_PRODUCTION_LINE = "production_line";

    private String name;
    private String id;
    private Shift first;
    private Shift second;
    private Shift third;
    private String leakFailureCounter;
    private ArrayList<Reason> scrapReasons;
    private Downtime downtime;
    private String date;
    private String parentId;
    private ArrayList<EmployeePosition> positions;
    private ArrayList<Employee> employees;
    private String timeStart;
    private String timeEnd;
    private ArrayList<Scrap> scraps;
    private ArrayList<Email> downtimeList;
    private ArrayList<Email> scrap1List;
    private ArrayList<Email> scrap2List;
    private ArrayList<Email> scrap3List;
    private String groupLeaders;
    private String teamLeaders;
    private String password;

    public Line() {
    }

    public static Comparator<Line> NameComparator = new Comparator<Line>() {

        public int compare(Line us1, Line us2) {
            String name1 = us1.getName().toUpperCase();
            String name2 = us2.getName().toUpperCase();

            //ascending order
            return name1.compareTo(name2);

            //descending order
            //return name2.compareTo(name1);
        }};

    public Line(String name, String id, Shift first, Shift second, Shift third, ArrayList<EmployeePosition> positions) {
        this.name = name;
        this.id = id;
        this.first = first;
        this.second = second;
        this.third = third;
        this.positions = positions;
    }

    public Line(Line line){
        this.name = line.getName();
        this.first = line.getFirst();
        this.second = line.getSecond();
        this.third = line.getThird();
        this.leakFailureCounter = line.getLeakFailureCounter();
        this.scrapReasons = line.getScrapReasons();
        this.downtime = line.getDowntime();
        this.parentId = line.getId();
        this.positions = line.getPositions();
        this.downtime = line.getDowntime();
        this.scrapReasons = line.getScrapReasons();
        this.timeEnd = line.timeEnd;
        this.timeStart = line.timeStart;
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

    public String getLeakFailureCounter() {
        return leakFailureCounter;
    }

    public void setLeakFailureCounter(String leakFailureCounter) {
        this.leakFailureCounter = leakFailureCounter;
    }

    public ArrayList<Reason> getScrapReasons() {
        return scrapReasons;
    }

    public void setScrapReasons(ArrayList<Reason> scrapReasons) {
        this.scrapReasons = scrapReasons;
    }

    public Downtime getDowntime() {
        return downtime;
    }

    public void setDowntime(Downtime downtime) {
        this.downtime = downtime;
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

    public ArrayList<EmployeePosition> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<EmployeePosition> positions) {
        this.positions = positions;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public ArrayList<Scrap> getScraps() {
        return scraps;
    }

    public void setScraps(ArrayList<Scrap> scraps) {
        this.scraps = scraps;
    }

    public ArrayList<Email> getDowntimeList() {
        return downtimeList;
    }

    public void setDowntimeList(ArrayList<Email> downtimeList) {
        this.downtimeList = downtimeList;
    }

    public ArrayList<Email> getScrap1List() {
        return scrap1List;
    }

    public void setScrap1List(ArrayList<Email> scrap1List) {
        this.scrap1List = scrap1List;
    }

    public ArrayList<Email> getScrap2List() {
        return scrap2List;
    }

    public void setScrap2List(ArrayList<Email> scrap2List) {
        this.scrap2List = scrap2List;
    }

    public ArrayList<Email> getScrap3List() {
        return scrap3List;
    }

    public void setScrap3List(ArrayList<Email> scrap3List) {
        this.scrap3List = scrap3List;
    }

    public String getGroupLeaders() {
        return groupLeaders;
    }

    public void setGroupLeaders(String groupLeaders) {
        this.groupLeaders = groupLeaders;
    }

    public String getTeamLeaders() {
        return teamLeaders;
    }

    public void setTeamLeaders(String teamLeaders) {
        this.teamLeaders = teamLeaders;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
