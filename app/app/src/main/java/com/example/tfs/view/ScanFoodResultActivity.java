package com.example.tfs.view;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfs.R;
import com.example.tfs.api.VolleyCallBack;
import com.example.tfs.dto.Transaction;
import com.example.tfs.service.FoodService;

public class ScanFoodResultActivity extends AppCompatActivity {

    private int foodId;
    private int distributorId;
    private TextView tv;
    private TextView farm;
    private TextView provider;
    private Transaction transaction;
    private int transactionId;
    private EditText txtReason;
    private String reason;
    private TextView txtFoodId;
    private TextView txtCateId;
    private TextView txtBreed;
    private TextView txtCreateDate;
    private Dialog myDialog;
    private Button btnConfirm;
    private Button btnReject;
    private int status;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_food_result);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        distributorId = sharedPreferences.getInt("premisesId", 0);
//        distributorId = 16;
        Intent it = this.getIntent();
        foodId = Integer.parseInt(it.getStringExtra("foodId"));
        tv = findViewById(R.id.txtResult);
        tv.setText("FoodID  : " + foodId + "  PremisesId: " + distributorId);


    }

    public void onClickConfirmButton(View v) {
        FoodService.getInstance().updateDistributorFood(ScanFoodResultActivity.this, distributorId, foodId, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {
                success();
            }
            @Override
            public void onError(Object ex) {
                fail();
            }
        });
    }

    public void onClickRejectButton(View v) {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    public void success() {
        Toast.makeText(ScanFoodResultActivity.this, "Xác nhận thành công", Toast.LENGTH_LONG);
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }
    public void fail() {
        Toast.makeText(ScanFoodResultActivity.this, "Xác nhận thất bại", Toast.LENGTH_LONG);
    }
}
