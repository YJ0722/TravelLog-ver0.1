package com.example.admin.travellog;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MemoViewActivity extends Activity {

    private TextView memoTitleView, memoContentView;
    private Button memoViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_memo_view);

        initView();

    }

    private void initView() {
        memoTitleView = (TextView) findViewById(R.id.memoTitleView);
        memoContentView = (TextView) findViewById(R.id.memoContentView);
        memoViewBtn = (Button) findViewById(R.id.memoViewBtn);

        memoViewBtn.setOnClickListener(memoViewBtnOnClickListener);
    }
    // 확인 버튼 클릭 리스너
    private View.OnClickListener memoViewBtnOnClickListener = new View.OnClickListener() {
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
