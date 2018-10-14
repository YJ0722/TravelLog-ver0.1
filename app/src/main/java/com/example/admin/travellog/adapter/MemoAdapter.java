package com.example.admin.travellog.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.admin.travellog.R;
import com.example.admin.travellog.models.Memo;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by a on 2018-09-16.
 */

public class MemoAdapter extends CursorAdapter {

    private SimpleDateFormat mSimpleDateFormat;

    String memoText;

    public String getMemoText() {
        return memoText;
    }

    public void setMemoText(String memoText) {
        this.memoText = memoText;
    }

    public MemoAdapter(Context context, Cursor c) {
        super(context, c, false);
        mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREAN);
    }

    public Memo get(int position) {
        Cursor cursor = getCursor();
        Memo memoDB = null;

        if (cursor.moveToPosition(position)) {
            String memoTitle = cursor.getString(cursor.getColumnIndex("memoTitle"));
            double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
            double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
            //String memoContent = cursor.getString(cursor.getColumnIndex("memoContent"));
            long date = cursor.getLong(cursor.getColumnIndex("date"));
            memoDB = new Memo(memoTitle, latitude, longitude);
        }

        return memoDB;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_check, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();
        double lengtitude = cursor.getDouble(cursor.getColumnIndex("lengtitude"));
        double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
        String memo = cursor.getString(cursor.getColumnIndex("memo"));
        long date = cursor.getLong(cursor.getColumnIndex("date"));

        holder.checkTextView.setText(memo);
        //holder.mDistanceTextView.setText(FormatUtil.getDouble(distance) + " m");
        //holder.mAverageSpeedTextView.setText(FormatUtil.getDouble(averageSpeed) + " km/h");
        //holder.mDateTextView.setText(mSimpleDateFormat.format(new Date(date)));
    }

    private static class ViewHolder {
        TextView checkTextView;

        ViewHolder(View itemView) {
            //mDistanceTextView = (TextView) itemView.findViewById(R.id.item_distance_tv);
            //mAverageSpeedTextView = (TextView) itemView.findViewById(R.id.item_speed_tv);
            //mTimeTextView = (TextView) itemView.findViewById(R.id.item_time_tv);
            checkTextView = (TextView) itemView.findViewById(R.id.checkTextView);
        }
    }
}
