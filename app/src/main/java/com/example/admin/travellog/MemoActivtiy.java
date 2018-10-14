package com.example.admin.travellog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.travellog.db.TrackingHistoryHelper;
import com.example.admin.travellog.models.Memo;
import com.example.admin.travellog.models.MemoDAO;
import com.google.android.gms.maps.model.LatLng;

public class MemoActivtiy extends AppCompatActivity {

    private TrackingHistoryHelper mHelper;
    private Memo memoBeans;
    private MemoDAO memoDAO;
    private TextView memoTitle, expense;
    private MultiAutoCompleteTextView memoContent;
    private Button addMemoBtn, cancelMemoBtn;
    LatLng location;
    String memoTitleStr, memoContentStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_activtiy);

        // memoDAO 연결
        memoDAO = new MemoDAO(getApplicationContext());

        // 뷰 셋팅
        initViews();

        // 인텐트로 넘겨받은 현재 위도, 경도 값 확인 --- 나중에 삭제!
        Intent intent = getIntent();
        location = intent.getParcelableExtra("location");

        Toast.makeText(MemoActivtiy.this, location.toString(), Toast.LENGTH_LONG).show();
    }

    private void initViews() {
        memoTitle = (TextView) findViewById(R.id.memoTitle);
        memoContent = (MultiAutoCompleteTextView) findViewById(R.id.memoContent);
        expense = (TextView) findViewById(R.id.expense);
        addMemoBtn = (Button) findViewById(R.id.addMemoBtn);
        addMemoBtn.setOnClickListener(addMemoBtnClickListener);
        cancelMemoBtn = (Button) findViewById(R.id.cancelMemoBtn);
        cancelMemoBtn.setOnClickListener(cancelMemoBtnClickListener);
    }

    // 확인 버튼 클릭리스너

    private View.OnClickListener addMemoBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            memoTitleStr = memoTitle.getText().toString();
            memoContentStr = memoContent.getText().toString();
            // 입력된 메모가 없을 경우
            if(memoTitleStr == null || memoContentStr == null) {
                Toast.makeText(MemoActivtiy.this, "memo : null", Toast.LENGTH_LONG).show();
            }
            // 입력된 메모가 있을 경우
            else {
                Toast.makeText(MemoActivtiy.this, memoContentStr, Toast.LENGTH_LONG).show();
                /** DAO - insertMemo 구현 필요
                 *  위도
                 *  경도
                 *  메모
                 *  경비
                 **/
                memoBeans = new Memo(memoTitleStr, location.latitude, location.longitude, System.currentTimeMillis());
                Log.d("MemoActivity - beans : ", memoBeans.toString());

                mHelper = TrackingHistoryHelper.getInstance(getApplicationContext());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        memoDAO.save(memoBeans);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //mSaveDialog.dismiss();
                                Toast.makeText(MemoActivtiy.this, "메모 기록이 저장되었습니다!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }).start();
            }
        }
    };

    // 취소 버튼 클릭리스너
    private View.OnClickListener cancelMemoBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MemoActivtiy.super.onBackPressed();
        }
    };
}
