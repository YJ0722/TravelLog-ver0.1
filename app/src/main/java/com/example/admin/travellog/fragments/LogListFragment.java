package com.example.admin.travellog.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.travellog.MapsActivity;
import com.example.admin.travellog.R;
import com.example.admin.travellog.ViewerActivity;
import com.example.admin.travellog.adapter.MemoAdapter;
import com.example.admin.travellog.adapter.TrackingHistoryAdapter;
import com.example.admin.travellog.models.TrackingHistory;
import com.example.admin.travellog.models.TrackingHistoryDAO;

public class LogListFragment extends Fragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private TrackingHistoryDAO mTrackingHistoryDAO;
    private TextView mNotifyTextView;
    private ListView mListView;
    private TrackingHistoryAdapter mAdapter;
    private MemoAdapter memoAdapter;

    private int travelNo;

    public int getTravelNo() {
        return travelNo;
    }

    public void setTravelNo(int travelNo) {
        this.travelNo = travelNo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.d("!!!", "LogListFragment OnCreateView()");

        return inflater.inflate(R.layout.fragment_log_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("$$$$$$$$$$", String.valueOf(getTravelNo()));
        mTrackingHistoryDAO = new TrackingHistoryDAO(getActivity());
        initViews();
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("!!!", "LogListFragment OnResume()");

        // TODO : Memo check
        /*
        MemoDAO memoDAO = new MemoDAO(getApplicationContext());
        Cursor cursor2 = memoDAO.findAll();
        if (memoAdapter == null) {
            memoAdapter = new MemoAdapter(getApplicationContext(), cursor2);
            Log.d("memo:", memoAdapter.get(0).toString());
        } else {
            memoAdapter.swapCursor(cursor2);
        }
        */
        // TrackingHistory 전체 목록 가져오기
        Cursor cursor = mTrackingHistoryDAO.findAll();
        System.out.println(cursor.getCount());
        if (mAdapter == null) {
            mAdapter = new TrackingHistoryAdapter(getActivity(), cursor);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.swapCursor(cursor);
        }

        if (cursor == null) {
            mNotifyTextView.setVisibility(View.VISIBLE);
        } else {
            mNotifyTextView.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        mNotifyTextView = (TextView) getActivity().findViewById(R.id.notify_tv);

        mListView = (ListView) getActivity().findViewById(R.id.lv);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnItemClickListener(this);
        getActivity().findViewById(R.id.fab).setOnClickListener(mOnTrackingStartButtonClickListener);
    }

    private View.OnClickListener mOnTrackingStartButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            Log.d("LogListF travelNo 인텐트", String.valueOf(getTravelNo()));
            intent.putExtra("travelNo", String.valueOf(getTravelNo()));
            startActivity(intent);
        }
    };

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
        new AlertDialog.Builder(getActivity())
                .setTitle("기록 삭제")
                .setMessage("기록을 삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTrackingHistoryDAO.delete((int) id);
                        mAdapter.swapCursor(mTrackingHistoryDAO.findAll());
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TrackingHistory data = mAdapter.get(position);
        Log.d("클릭 아이템 확인 : " , data.toString());
        Intent intent = new Intent(getActivity(), ViewerActivity.class);
        intent.putExtra("data", data);
        intent.putExtra("pathList", data.getPathJson());
        startActivity(intent);
    }
}
