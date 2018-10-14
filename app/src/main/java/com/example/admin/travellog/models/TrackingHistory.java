package com.example.admin.travellog.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by massivcode@gmail.com on 2017. 1. 5. 11:17
 */

public class TrackingHistory implements Parcelable {
    private int travelNo;
    private String title;
    private long elapsedTime;
    private double distance;
    private String pathJson;
    private long date;

    public TrackingHistory(int travelNo, long date, double distance, long elapsedTime, String pathJson) {
        this.travelNo = travelNo;
        this.elapsedTime = elapsedTime;
        this.distance = distance;
        this.pathJson = pathJson;
        this.date = date;
    }

    public TrackingHistory(int travelNo, String title, long date, double distance, long elapsedTime, String pathJson) {
        this.travelNo = travelNo;
        this.title = title;
        this.elapsedTime = elapsedTime;
        this.distance = distance;
        this.pathJson = pathJson;
        this.date = date;
    }

    public TrackingHistory(Parcel source) {
        this.travelNo = source.readInt();
        this.elapsedTime = source.readLong();
        this.title = source.readString();
        this.distance = source.readDouble();
        this.pathJson = source.readString();
        this.date = source.readLong();
    }

    public int getTravelNo() {
        return travelNo;
    }

    public void setTravelNo(int travelNo) {
        this.travelNo = travelNo;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getPathJson() {
        return pathJson;
    }

    public void setPathJson(String pathJson) {
        this.pathJson = pathJson;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "TrackingHistory{" +
                "travelNo=" + travelNo +
                ", title='" + title + '\'' +
                ", elapsedTime=" + elapsedTime +
                ", distance=" + distance +
                ", pathJson='" + pathJson + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(travelNo);
        dest.writeString(title);
        dest.writeLong(date);
        dest.writeDouble(distance);
        dest.writeLong(elapsedTime);
        dest.writeString(pathJson);
    }

    public static final Parcelable.Creator<TrackingHistory> CREATOR =
            new Parcelable.Creator<TrackingHistory>() {
                @Override
                public TrackingHistory createFromParcel(Parcel source) {
                    return new TrackingHistory(source);
                }

                @Override
                public TrackingHistory[] newArray(int size) {
                    return new TrackingHistory[size];
                }
            };
}
