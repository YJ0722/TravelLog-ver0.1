package com.example.admin.travellog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.admin.travellog.fragments.MyDatePickerFragment;

public class TravelAddActivity extends AppCompatActivity implements View.OnClickListener {

    Button saveTravelBtn, cancelTravelBtn;
    EditText inputTravelTitle, inputTravelStartDate, inputTravelEndDate;
    String travelTitle, travelStartDate, travelEndDate;
    //chaeeun test
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_add);


        inputTravelTitle = (EditText) findViewById(R.id.inputTravelTitle);
        inputTravelStartDate = (EditText) findViewById(R.id.inputTravelStartDate);
        inputTravelEndDate = (EditText) findViewById(R.id.inputTravelEndDate);
        inputTravelStartDate.setOnClickListener(this);
        inputTravelEndDate.setOnClickListener(this);

        saveTravelBtn = (Button) findViewById(R.id.saveTravelBtn);
        cancelTravelBtn = (Button) findViewById(R.id.cancelTravelBtn);
        saveTravelBtn.setOnClickListener(saveLogBtnOnClickListener);
        cancelTravelBtn.setOnClickListener(cancelLogBtnOnClickListener);

        //initViews();

        //데이터 전달하기
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
    }

    public void onClick(View v) {
        //데이터피커 보여주기
        showDatePicker(v);
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
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

}
