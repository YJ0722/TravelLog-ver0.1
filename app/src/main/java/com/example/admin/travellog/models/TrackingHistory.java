package com.example.admin.travellog.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by massivcode@gmail.com on 2017. 1. 5. 11:17
 */

public class TrackingHistory implements Parcelable {
    private String title;
    private long elapsedTime;
    private double averageSpeed;
    private double distance;
    private String pathJson;
    private long date;

    public TrackingHistory(long elapsedTime, double averageSpeed, double distance, String pathJson, long date) {
        this.elapsedTime = elapsedTime;
        this.averageSpeed = averageSpeed;
        this.distance = distance;
        this.pathJson = pathJson;
        this.date = date;
    }

    public TrackingHistory(String title, long elapsedTime, double averageSpeed, double distance, String pathJson, long date) {
        this.title = title;
        this.elapsedTime = elapsedTime;
        this.averageSpeed = averageSpeed;
        this.distance = distance;
        this.pathJson = pathJson;
        this.date = date;
    }

    public TrackingHistory(Parcel source) {
        this.elapsedTime = source.readLong();
        this.averageSpeed = source.readDouble();
        this.distance = source.readDouble();
        this.pathJson = source.readString();
        this.date = source.readLong();
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
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
        final StringBuffer sb = new StringBuffer("TrackingHistory{");
        sb.append("elapsedTime=").append(elapsedTime);
        sb.append(", averageSpeed=").append(averageSpeed);
        sb.append(", distance=").append(distance);
        sb.append(", pathJson='").append(pathJson).append('\'');
        sb.append(", date=").append(date);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(elapsedTime);
        dest.writeDouble(averageSpeed);
        dest.writeDouble(distance);
        dest.writeString(pathJson);
        dest.writeLong(date);
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
