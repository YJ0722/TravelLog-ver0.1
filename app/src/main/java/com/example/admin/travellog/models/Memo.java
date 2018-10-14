package com.example.admin.travellog.models;

/**
 * Created by a on 2018-09-16.
 */

public class Memo {
    private int travel_no;
    //private int log_no;
    private int memo_no;
    private String memoTitle;
    private double latitude;
    private double longitude;
    private String memoContent;
    private long date;

    public Memo(String memoTitle, double latitude, double longitude) {
        this.memoTitle = memoTitle;
        this.latitude = latitude;
        this.longitude = longitude;
        //this.date = date;
    }
    public Memo(String memoTitle, double latitude, double longitude, long date) {
        this.memoTitle = memoTitle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public Memo(int travel_no, String memoTitle, double latitude, double longitude, long date) {
        this.travel_no = travel_no;
        //this.log_no = log_no;
        this.memo_no = memo_no;
        this.memoTitle = memoTitle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public int getTravel_no() {
        return travel_no;
    }

    public void setTravel_no(int travel_no) {
        this.travel_no = travel_no;
    }

    public int getMemo_no() {
        return memo_no;
    }

    public void setMemo_no(int memo_no) {
        this.memo_no = memo_no;
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

    public String getMemoTitle() {
        return memoTitle;
    }

    public void setMemoTitle(String memoTitle) {
        this.memoTitle = memoTitle;
    }

    public String getMemoContent() {
        return memoContent;
    }

    public void setMemoContent(String memoContent) {
        this.memoContent = memoContent;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "travel_no=" + travel_no +
                ", memo_no=" + memo_no +
                ", memoTitle='" + memoTitle + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", memoContent='" + memoContent + '\'' +
                ", date=" + date +
                '}';
    }
}
