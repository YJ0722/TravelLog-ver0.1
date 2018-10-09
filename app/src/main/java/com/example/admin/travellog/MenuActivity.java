package com.example.admin.travellog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {


    // 로그 시작 버튼
    Button logBtn;
    // 여행 목록 버튼
    Button listBtn;
    // 설정 버튼
    Button planBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        logBtn = (Button) findViewById(R.id.logBtn);
        listBtn = (Button) findViewById(R.id.listBtn);
        planBtn = (Button) findViewById(R.id.planBtn);

        // 여행 계획 이동


        // 여행 목록 이동

    }


    // 로그 시작 이동
    public void clickedLogBtn(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // 여행 목록 이동
    // 잠깐 확인용으로 연결! 나중에 수정 필요!!
    public void clickedListBtn(View v) {
        Intent intent = new Intent(this, CheckActivtiy.class);
        startActivity(intent);
    }

    public void clickedPlanBtn(View v) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
}
