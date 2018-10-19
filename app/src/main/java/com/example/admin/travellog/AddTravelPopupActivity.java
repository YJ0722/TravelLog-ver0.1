package com.example.admin.travellog;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.admin.travellog.fragments.MyDatePickerFragment;

public class AddTravelPopupActivity extends AppCompatActivity {

    Button saveTravelBtn, cancelTravelBtn;
    EditText inputTravelTitle, inputTravelStartDate, inputTravelEndDate;
    String travelTitle, travelStartDate, travelEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_travel_popup);
        initView();

        //데이터 전달하기
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }


    public void showDatePicker(View v) {
        DialogFragment newFragment = new MyDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "date picker");

        //String year = y;
            /*int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year =  datePicker.getYear();*/

        //btn.setText("X");
        //btn.setText();
    }
    private void initView() {
        inputTravelTitle = (EditText) findViewById(R.id.inputTravelTitle);

        inputTravelStartDate = (EditText) findViewById(R.id.inputTravelStartDate);
        inputTravelEndDate = (EditText) findViewById(R.id.inputTravelEndDate);
        inputTravelStartDate.setOnClickListener(StartDatePickerListener);
        inputTravelEndDate.setOnClickListener(EndDatePickerListener);

        saveTravelBtn = (Button) findViewById(R.id.saveTravelBtn);
        cancelTravelBtn = (Button) findViewById(R.id.cancelTravelBtn);
        saveTravelBtn.setOnClickListener(saveLogBtnOnClickListener);
        cancelTravelBtn.setOnClickListener(cancelLogBtnOnClickListener);
    }

    private View.OnClickListener StartDatePickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //데이터피커 보여주기
             showDatePicker(v);
        }

    };

    private View.OnClickListener EndDatePickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //데이터피커 보여주기
            showDatePicker(v);
        }

    };
    public void doDatePicker(View view) {
        DialogFragment myDatePickerFragment = new MyDatePickerFragment();
        myDatePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }


    // 확인 버튼 클릭 리스너
    private View.OnClickListener saveLogBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            travelTitle = inputTravelTitle.getText().toString();
            travelStartDate = inputTravelStartDate.getText().toString();
            travelEndDate = inputTravelEndDate.getText().toString();

            //데이터 전달하기
            Intent intent = new Intent();
            intent.putExtra("travelTitle", travelTitle);
            intent.putExtra("travelStartDate", travelStartDate);
            intent.putExtra("travelEndDate", travelEndDate);
            //startActivityForResult(intent, 1);
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
