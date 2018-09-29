package com.example.admin.travellog.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.travellog.FormatUtil;
import com.example.admin.travellog.R;
import com.example.admin.travellog.models.TrackingHistory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by massivcode@gmail.com on 2017. 1. 5. 14:07
 */

public class TrackingHistoryAdapter extends CursorAdapter {
    private SimpleDateFormat mSimpleDateFormat;

    public TrackingHistoryAdapter(Context context, Cursor c) {
        super(context, c, false);
        mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREAN);
    }

    public TrackingHistory get(int position) {
        Cursor cursor = getCursor();
        TrackingHistory trackingHistory = null;

        if (cursor.moveToPosition(position)) {
            String logTitle = cursor.getString(cursor.getColumnIndex("logTitle"));
            long elapsedTime = cursor.getLong(cursor.getColumnIndex("elapsedTime"));
            double distance = cursor.getDouble(cursor.getColumnIndex("distance"));
            double averageSpeed = cursor.getDouble(cursor.getColumnIndex("averageSpeed"));
            String pathJson = cursor.getString(cursor.getColumnIndex("pathJson"));
            long date = cursor.getLong(cursor.getColumnIndex("date"));
            trackingHistory = new TrackingHistory(logTitle, elapsedTime, averageSpeed, distance, pathJson, date);
        }

        return trackingHistory;
    }

    // 트래킹 기록 목록 뷰 설정
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tracking_history, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    // 트래킹 기록 목록에 데이터 셋팅
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        String logTitle = cursor.getString(cursor.getColumnIndex("logTitle"));
        long elapsedTime = cursor.getLong(cursor.getColumnIndex("elapsedTime"));
        double distance = cursor.getDouble(cursor.getColumnIndex("distance"));
        double averageSpeed = cursor.getDouble(cursor.getColumnIndex("averageSpeed"));
        long date = cursor.getLong(cursor.getColumnIndex("date"));

        holder.mLogTitle.setText(logTitle);
        holder.mTimeTextView.setText(FormatUtil.getTime(elapsedTime));
        holder.mDistanceTextView.setText(FormatUtil.getDouble(distance) + " m");
        //holder.mAverageSpeedTextView.setText(FormatUtil.getDouble(averageSpeed) + " km/h");
        holder.mDateTextView.setText(mSimpleDateFormat.format(new Date(date)));

    }

    private static class ViewHolder {
        TextView mLogTitle, mDistanceTextView, mAverageSpeedTextView, mTimeTextView, mDateTextView;

        ViewHolder(View itemView) {
            mLogTitle = (TextView) itemView.findViewById(R.id.logTitle);
            mDistanceTextView = (TextView) itemView.findViewById(R.id.item_distance_tv);
            //mAverageSpeedTextView = (TextView) itemView.findViewById(R.id.item_speed_tv);
            mTimeTextView = (TextView) itemView.findViewById(R.id.item_time_tv);
            mDateTextView = (TextView) itemView.findViewById(R.id.item_date_tv);
        }
    }
}
