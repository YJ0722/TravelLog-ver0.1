package com.example.admin.travellog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity {


    // 로그인 설정 버튼
    Button loginsetBtn;
    // 설정 버튼
    Button contactusBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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

        loginsetBtn = (Button) findViewById(R.id.loginsetting);
        contactusBtn = (Button) findViewById(R.id.contactus);

    }


    // 로그인 설정 이동
    public void clickedLoginSetBtn(View v) {
        Intent intent = new Intent(this, LoginTestActivity.class);
        startActivity(intent);
    }

    // ContactUs page 설정
    //todo : contact 페이지 설정
    public void clickedContactUs(View v) {
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

}
