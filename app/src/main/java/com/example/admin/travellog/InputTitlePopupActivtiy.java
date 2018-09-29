package com.example.admin.travellog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.travellog.models.TrackingHistory;

public class InputTitlePopupActivtiy extends Activity {

    Button saveLogBtn, cancelLogBtn;
    EditText inputLogTitle;
    String logTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_input_title_popup_activtiy);

        initView();

        //데이터 가져오기
        Intent intent = getIntent();
        TrackingHistory trackingHistory = intent.getParcelableExtra("trackingHistory");

        Toast.makeText(InputTitlePopupActivtiy.this, trackingHistory.toString(), Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        inputLogTitle = (EditText) findViewById(R.id.inputLogTitle);
        saveLogBtn = (Button) findViewById(R.id.saveLogBtn);
        cancelLogBtn = (Button) findViewById(R.id.cancelLogBtn);

        saveLogBtn.setOnClickListener(saveLogBtnOnClickListener);
        cancelLogBtn.setOnClickListener(cancelLogBtnOnClickListener);
    }

    // 확인 버튼 클릭 리스너
    private View.OnClickListener saveLogBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logTitle = inputLogTitle.getText().toString();

            //데이터 전달하기
            Intent intent = new Intent();
            intent.putExtra("title", logTitle);
            setResult(RESULT_OK, intent);

            //액티비티(팝업) 닫기
            finish();
        }
    };

    // 취소 버튼 클릭 리스너
    private View.OnClickListener cancelLogBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //액티비티(팝업) 닫기
            finish();
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
