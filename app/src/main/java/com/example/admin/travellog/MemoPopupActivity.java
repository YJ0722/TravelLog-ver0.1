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

public class MemoPopupActivity extends Activity {

    Button saveMemoBtn, cancelMemoBtn;
    EditText inputMemoTitle, inputMemoContent;
    String memoTitle, memoContent;
    double memoLatitude, memoLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_memo_popup);

        initView();

        Intent intent = getIntent();
        memoLatitude = intent.getDoubleExtra("memoLatitude", 0);
        memoLongitude = intent.getDoubleExtra("memoLongitude", 0);

        Toast.makeText(MemoPopupActivity.this, "lat: " + memoLatitude + "\nlong:" + memoLongitude, Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        inputMemoTitle = (EditText) findViewById(R.id.inputMemoTitle);
        inputMemoContent = (EditText) findViewById(R.id.inputMemoContent);
        saveMemoBtn = (Button) findViewById(R.id.saveMemoBtn);
        cancelMemoBtn = (Button) findViewById(R.id.cancelMemoBtn);

        saveMemoBtn.setOnClickListener(saveMemoBtnOnClickListener);
        cancelMemoBtn.setOnClickListener(cancelMemoBtnOnClickListener);
    }

    // 확인 버튼 클릭 리스너
    private View.OnClickListener saveMemoBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            memoTitle = inputMemoTitle.getText().toString();
            memoContent = inputMemoContent.getText().toString();

            //데이터 전달하기
            Intent intent = new Intent();
            intent.putExtra("memoTitle", memoTitle);
            intent.putExtra("memoContent", memoContent);
            intent.putExtra("memoLatitude", memoLatitude);
            intent.putExtra("memoLongitude", memoLongitude);
            setResult(RESULT_OK, intent);
//            setResult(2, intent);

            //액티비티(팝업) 닫기
            finish();
        }
    };

    // 취소 버튼 클릭 리스너
    private View.OnClickListener cancelMemoBtnOnClickListener = new View.OnClickListener() {
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
