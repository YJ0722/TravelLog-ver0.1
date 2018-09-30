package com.example.admin.travellog;

/**
 * Created by eee30 on 2018-09-30.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginTestActivity extends Activity {
    /** Called when the activity is first created. */
    TextView textView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnPrefs = (Button) findViewById(R.id.btnPrefs);
        Button btnGetPrefs = (Button) findViewById(R.id.btnGetPreferences);

        textView = (TextView) findViewById(R.id.txtPrefs);

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnPrefs:
                        Intent intent = new Intent(LoginTestActivity.this,
                                PasswordActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.btnGetPreferences:
                        displaySharedPreferences();
                        break;

                    default:
                        break;
                }
            }
        };

        btnPrefs.setOnClickListener(listener);
        btnGetPrefs.setOnClickListener(listener);
    }

    private void displaySharedPreferences() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(LoginTestActivity.this);

        String password = prefs.getString("password", "");

        StringBuilder builder = new StringBuilder();
        builder.append("비밀번호: " + password + "\n");

        textView.setText(builder.toString());
    }

}