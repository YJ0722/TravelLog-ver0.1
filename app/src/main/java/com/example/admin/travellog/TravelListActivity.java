package com.example.admin.travellog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.travellog.adapter.TravelAdapter;
import com.example.admin.travellog.models.Travel;
import com.example.admin.travellog.models.TravelDAO;

public class TravelListActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private TravelDAO travelDAO;
    private TextView travelNotifyTextView;
    private ListView travelListView;
    private TravelAdapter travelAdapter;
    private ProgressDialog mSaveDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_list);
        travelDAO = new TravelDAO(getApplicationContext());
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Travel 전체 목록 가져오기
        Cursor cursor = travelDAO.findAll();
        System.out.println(cursor.getCount());
        Log.d("travelDAO 커서 확인", String.valueOf(cursor.getCount()));
        if (travelAdapter == null) {
            travelAdapter = new TravelAdapter(getApplicationContext(), cursor);
            Log.d("!!!!!!!!!", "travelAdapter 생성 성공!");
            travelListView.setAdapter(travelAdapter);
        } else {
            travelAdapter.swapCursor(cursor);
        }

        if (cursor == null) {
            travelNotifyTextView.setVisibility(View.VISIBLE);
        } else {
            travelNotifyTextView.setVisibility(View.GONE);
        }
    }

    private void initDialog() {

        mSaveDialog = new ProgressDialog(TravelListActivity.this);
        mSaveDialog.setCancelable(false);
        mSaveDialog.setTitle("여행 기록 저장중");
        mSaveDialog.setMessage("여행 기록을 저장하고 있습니다.");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode == 1) {

                final String travelTitle = data.getStringExtra("travelTitle");
                String travelStartDate = data.getStringExtra("travelStartDate");
                String travelEndDate = data.getStringExtra("travelEndDate");

                Toast.makeText(TravelListActivity.this, "title : " + travelTitle + "\ntravelStartDate : " +
                        travelStartDate + "\ntravelEndDate : " + travelEndDate, Toast.LENGTH_SHORT).show();

                final Travel travel = new Travel(travelTitle, Long.parseLong(travelStartDate), Long.parseLong(travelEndDate));
                //startActivityForResult(intent, 1);
                // TODO : memo 객체 DB에 save
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        travelDAO.save(travel);
                        restartActivity();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //mSaveDialog.dismiss();
                                Toast.makeText(TravelListActivity.this, "여행이 저장되었습니다!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();


        } else {

            Toast.makeText(TravelListActivity.this, "result == 1아님", Toast.LENGTH_SHORT).show();
        }

    }

    private void initViews() {
        travelNotifyTextView = (TextView) findViewById(R.id.travel_notify_tv);

        travelListView = (ListView) findViewById(R.id.travel_lv);
        travelListView.setOnItemLongClickListener(this);
        travelListView.setOnItemClickListener(this);
        findViewById(R.id.travel_fab).setOnClickListener(mOnTrackingStartButtonClickListener);
    }

    private View.OnClickListener mOnTrackingStartButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivityForResult(new Intent(TravelListActivity.this, TravelAddActivity.class), 1);
        }
    };

    public void restartActivity()
    {
        Intent intent= new Intent(this, TravelListActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
        new AlertDialog.Builder(TravelListActivity.this)
                .setTitle("기록 삭제")
                .setMessage("기록을 삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //travelDAO.delete((int) id);
                        //travelAdapter.swapCursor(travelDAO.findAll());
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
        Travel data = travelAdapter.get(position);
        Log.d("클릭 데이터 확인 : " , data.toString());
        startActivity(new Intent(TravelListActivity.this, TabsActivity.class).putExtra("data", (Parcelable) data));
    }
}
