package com.example.admin.travellog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.travellog.models.ExpenseHistory;
import com.example.admin.travellog.models.ExpenseHistoryDAO;

import java.util.ArrayList;


public class ExpensePopupActivity extends Activity {

    Spinner typeSpinner;
    EditText inputExpenseTitle, inputExpenseData;
    Button saveExpenseBtn, cancelExpenseBtn;

    private ProgressDialog mProgressDialog, mSaveDialog;
    ExpenseHistory expenseHistory;
    ExpenseHistoryDAO expenseHistoryDAO;
    ArrayList spinnerTypeArr;
    String expenseTitle;
    int type, cost;
    double expenseLatitude, expenseLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_expense_popup);

        initView();

        Intent intent = getIntent();
        expenseLatitude = intent.getDoubleExtra("expenseLatitude", 0);
        expenseLongitude = intent.getDoubleExtra("expenseLongitude", 0);

        Toast.makeText(ExpensePopupActivity.this, "lat: " + expenseLatitude + "\nlong:" + expenseLongitude, Toast.LENGTH_SHORT).show();


    }

    private void initView() {
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        inputExpenseTitle = (EditText) findViewById(R.id.inputExpenseTitle);
        inputExpenseData = (EditText) findViewById(R.id.inputExpenseData);
        saveExpenseBtn = (Button) findViewById(R.id.saveExpenseBtn);
        cancelExpenseBtn = (Button) findViewById(R.id.cancelExpenseBtn);

        saveExpenseBtn.setOnClickListener(saveExpenseBtnOnClickListener);
        cancelExpenseBtn.setOnClickListener(cancelExpenseBtnOnClickListener);

        settingSpinner(typeSpinner);
    }

    // spinner에 수입/지출 선택 셋팅
    private void settingSpinner(final Spinner spinner) {
        spinnerTypeArr = new ArrayList();
        spinnerTypeArr.add("수입");
        spinnerTypeArr.add("지출");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerTypeArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setPrompt("수입/지출 선택하세요");
        typeSpinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // 확인 버튼 클릭 리스너
    private View.OnClickListener saveExpenseBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            expenseTitle = inputExpenseTitle.getText().toString();

            expenseTitle = inputExpenseTitle.getText().toString();
            cost = Integer.parseInt(inputExpenseData.getText().toString());

            //데이터 전달하기
            Intent intent = new Intent();
            intent.putExtra("expenseTitle", expenseTitle);
            intent.putExtra("type", type);
            intent.putExtra("cost", cost);
            intent.putExtra("expenseLatitude", expenseLatitude);
            intent.putExtra("expenseLongitude", expenseLongitude);
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
