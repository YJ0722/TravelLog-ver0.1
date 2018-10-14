package com.example.admin.travellog.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.admin.travellog.R;
import com.example.admin.travellog.models.Travel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by a on 2018-10-14.
 */

public class TravelAdapter extends CursorAdapter {

    private SimpleDateFormat mSimpleDateFormat;

    public TravelAdapter(Context context, Cursor c) {
        super(context, c, false);
        mSimpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREAN);
    }

    public Travel get(int position) {
        Cursor cursor = getCursor();
        Travel travel = null;

        if (cursor.moveToPosition(position)) {
            int travel_no = cursor.getInt(cursor.getColumnIndex("_id"));
            String travelTitle = cursor.getString(cursor.getColumnIndex("travelTitle"));
            long startDate = cursor.getLong(cursor.getColumnIndex("startDate"));
            long endDate = cursor.getLong(cursor.getColumnIndex("endDate"));
            travel = new Travel(travel_no, travelTitle, startDate, endDate);
        }

        return travel;
    }

    // 여행 기록 목록 뷰 설정
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    // 여행 기록 목록에 데이터 셋팅
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        int travel_no = cursor.getInt(cursor.getColumnIndex("_id"));
        String travelTitle = cursor.getString(cursor.getColumnIndex("travelTitle"));
        long startDate = cursor.getLong(cursor.getColumnIndex("startDate"));
        long endDate = cursor.getLong(cursor.getColumnIndex("endDate"));

        String no = String.valueOf(travel_no);
        Log.d("travel_no 확인! " , no);

        holder.itemTravelNo.setText(no);
        holder.itemTravelTitle.setText(travelTitle);
        holder.itemTravelStartDate.setText(mSimpleDateFormat.format(new Date(startDate)));
        holder.itemTravelEndDate.setText(mSimpleDateFormat.format(new Date(endDate)));

    }

    private static class ViewHolder {
        TextView itemTravelNo, itemTravelTitle, itemTravelStartDate, itemTravelEndDate;

        ViewHolder(View itemView) {
            itemTravelNo = (TextView) itemView.findViewById(R.id.item_travel_no);
            itemTravelTitle = (TextView) itemView.findViewById(R.id.item_travelTitle);
            itemTravelStartDate = (TextView) itemView.findViewById(R.id.item_travel_start_date);
            itemTravelEndDate = (TextView) itemView.findViewById(R.id.item_travel_end_date);
        }
    }

}
