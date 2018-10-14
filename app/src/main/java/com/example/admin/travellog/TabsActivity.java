package com.example.admin.travellog;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.admin.travellog.fragments.ExpenseFragment;
import com.example.admin.travellog.fragments.LogListFragment;
import com.example.admin.travellog.fragments.MemoFragment;
import com.example.admin.travellog.models.Travel;

public class TabsActivity extends AppCompatActivity {

    TextView travelTitle;
    ExpenseFragment expenseFragment;
    LogListFragment logListFragment;
    MemoFragment memoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        Intent intent = getIntent();

        Travel travel = intent.getParcelableExtra("data");
        String name = travel.getTravelTitle();

        travelTitle = (TextView) findViewById(R.id.travelTitle);
        travelTitle.setText(name);

        logListFragment = new LogListFragment();
        /////////////////////////////////
        logListFragment.setTravelNo(travel.getTravel_no());
        /////////////////////////////////
        expenseFragment = new ExpenseFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, logListFragment).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("로그"));
        tabs.addTab(tabs.newTab().setText("경비"));
        //tabs.addTab(tabs.newTab().setText("메모"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if(position == 0) {
                    selected = new LogListFragment();
                } else if(position == 1) {
                    selected = new ExpenseFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
