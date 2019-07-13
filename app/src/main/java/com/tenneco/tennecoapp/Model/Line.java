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
    public static final String DB_DATE_P_LINE = "production_line_by_date";
    public static final String AVAILABLE_DATES = "available_dates";
    private String name;
    private String code;
    private String description;
    private ArrayList<Product> products;
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
    private ArrayList<Reject> rejects;
    private ArrayList<Downtime> downtimes;
    private EmailList lineList;
    private EmailList downtimeEmailList;
    private EmailList leakEmailList;
    private EmailList scrap1EmailList;
    private EmailList scrap2EmailList;
    private EmailList scrap3EmailList;
    private String groupLeaders;
    private String teamLeaders;
    private String password;
    private Product lastProduct;
    private int turno;
    private boolean isSchedule;
    private ArrayList<Employee> operators;
    private String dateLast1st;
    private String dateLast2nd;
    private String dateLast3rd;

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

    public ArrayList<Reject> getRejects() {
        return rejects;
    }

    public void setRejects(ArrayList<Reject> rejects) {
        this.rejects = rejects;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EmailList getLineList() {
        return lineList;
    }

    public void setLineList(EmailList lineList) {
        this.lineList = lineList;
    }

    public EmailList getDowntimeEmailList() {
        return downtimeEmailList;
    }

    public void setDowntimeEmailList(EmailList downtimeEmailList) {
        this.downtimeEmailList = downtimeEmailList;
    }

    public EmailList getLeakEmailList() {
        return leakEmailList;
    }

    public void setLeakEmailList(EmailList leakEmailList) {
        this.leakEmailList = leakEmailList;
    }

    public EmailList getScrap1EmailList() {
        return scrap1EmailList;
    }

    public void setScrap1EmailList(EmailList scrap1EmailList) {
        this.scrap1EmailList = scrap1EmailList;
    }

    public EmailList getScrap2EmailList() {
        return scrap2EmailList;
    }

    public void setScrap2EmailList(EmailList scrap2EmailList) {
        this.scrap2EmailList = scrap2EmailList;
    }

    public EmailList getScrap3EmailList() {
        return scrap3EmailList;
    }

    public void setScrap3EmailList(EmailList scrap3EmailList) {
        this.scrap3EmailList = scrap3EmailList;
    }

    public Product getLastProduct() {
        return lastProduct;
    }

    public void setLastProduct(Product lastProduct) {
        this.lastProduct = lastProduct;
    }

    public ArrayList<Downtime> getDowntimes() {
        return downtimes;
    }

    public void setDowntimes(ArrayList<Downtime> downtimes) {
        this.downtimes = downtimes;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public boolean isSchedule() {
        return isSchedule;
    }

    public void setSchedule(boolean schedule) {
        isSchedule = schedule;
    }

    public ArrayList<Employee> getOperators() {
        return operators;
    }

    public void setOperators(ArrayList<Employee> operators) {
        this.operators = operators;
    }

    public String getDateLast1st() {
        return dateLast1st;
    }

    public String getDateLast2nd() {
        return dateLast2nd;
    }

    public String getDateLast3rd() {
        return dateLast3rd;
    }

    public void setDateLast1st(String dateLast1st) {
        this.dateLast1st = dateLast1st;
    }

    public void setDateLast2nd(String dateLast2nd) {
        this.dateLast2nd = dateLast2nd;
    }

    public void setDateLast3rd(String dateLast3rd) {
        this.dateLast3rd = dateLast3rd;
    }
}
