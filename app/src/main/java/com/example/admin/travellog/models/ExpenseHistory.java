package com.example.admin.travellog.models;

/**
 * Created by a on 2018-10-01.
 */

public class ExpenseHistory {

    private int id;     // ExpenseHistory 레코드 ID
    private int trackingHistory_id;     // 비용 객체가 해당하는 트래킹 기록의 ID
    private String expenseTitle;    // 비용 제목
    private int expenseType;        // 수입: 0, 지출: 1
    private int cost;   // 비용
    private int balance;    // 잔고
    private double latitude;    // 위도
    private double longitude;   // 경도
    private long date;      // 날짜

    public ExpenseHistory(String expenseTitle, int expenseType, int cost, double latitude, double longitude, long date) {
        this.expenseTitle = expenseTitle;
        this.expenseType = expenseType;
        this.cost = cost;
        this.date = date;
    }

    public ExpenseHistory(int trackingHistory_id, String expenseTitle, int expenseType, int cost, double latitude, double longitude, long date) {
        this.trackingHistory_id = trackingHistory_id;
        this.expenseTitle = expenseTitle;
        this.expenseType = expenseType;
        this.cost = cost;
        this.date = date;
    }

    public ExpenseHistory(int trackingHistory_id, String expenseTitle, int expenseType, int cost, int balance, double latitude, double longitude, long date) {
        this.trackingHistory_id = trackingHistory_id;
        this.expenseTitle = expenseTitle;
        this.expenseType = expenseType;
        this.cost = cost;
        this.balance = balance;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrackingHistory_id() {
        return trackingHistory_id;
    }

    public void setTrackingHistory_id(int trackingHistory_id) {
        this.trackingHistory_id = trackingHistory_id;
    }

    public String getExpenseTitle() {
        return expenseTitle;
    }

    public void setExpenseTitle(String expenseTitle) {
        this.expenseTitle = expenseTitle;
    }

    public int getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(int expenseType) {
        this.expenseType = expenseType;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ExpenseHistory{" +
                "id=" + id +
                ", trackingHistory_id=" + trackingHistory_id +
                ", expenseTitle='" + expenseTitle + '\'' +
                ", expenseType=" + expenseType +
                ", cost=" + cost +
                ", balance=" + balance +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", date=" + date +
                '}';
    }
}
