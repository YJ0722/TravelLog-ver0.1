package com.example.admin.travellog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class ExpensePopupActivity extends Activity {

    EditText inputExpenseTitle;
    Button saveExpenseBtn, cancelExpenseBtn;

    String expenseTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_expense_popup);

        initView();
    }

    private void initView() {
        inputExpenseTitle = (EditText) findViewById(R.id.inputExpenseTitle);
        saveExpenseBtn = (Button) findViewById(R.id.saveExpenseBtn);
        cancelExpenseBtn = (Button) findViewById(R.id.cancelExpenseBtn);

        saveExpenseBtn.setOnClickListener(saveExpenseBtnOnClickListener);
        cancelExpenseBtn.setOnClickListener(cancelExpenseBtnOnClickListener);
    }

    // 확인 버튼 클릭 리스너
    private View.OnClickListener saveExpenseBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            expenseTitle = inputExpenseTitle.getText().toString();

            //데이터 전달하기
            Intent intent = new Intent();
            intent.putExtra("expenseTitle", expenseTitle);
            setResult(RESULT_OK, intent);
//            setResult(1, intent);

            //액티비티(팝업) 닫기
            finish();
        }
    };

    // 취소 버튼 클릭 리스너
    private View.OnClickListener cancelExpenseBtnOnClickListener = new View.OnClickListener() {
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
