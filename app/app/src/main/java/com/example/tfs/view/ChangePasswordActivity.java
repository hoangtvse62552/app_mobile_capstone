package com.example.tfs.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tfs.R;
import com.example.tfs.api.VolleyCallBack;
import com.example.tfs.service.UserService;

public class ChangePasswordActivity extends AppCompatActivity {


    private EditText txtOldPassword;
    private EditText txtPasswordChange;
    private EditText txtRePasswordChange;
    private TextView txtFailChangePassword;
    private ProgressDialog progressDialog;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        progressDialog = new ProgressDialog(ChangePasswordActivity.this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userID = sharedPreferences.getInt("userID",0);

    }
    public void closeChange(View v) {
        Intent intent =new Intent(getApplicationContext(),ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0,0);

    }

    public void changePassword(View view) {


        txtFailChangePassword = findViewById(R.id.txtFailChangePassword);
        txtPasswordChange = findViewById(R.id.txtPasswordChange);
        txtRePasswordChange = findViewById(R.id.txtRePasswordChange);
        txtOldPassword = findViewById(R.id.txtOldPassword);

//        Toast.makeText(ProfileActivity.this, "" + txtPasswordChange.getText(),Toast.LENGTH_LONG);
        if(!txtPasswordChange.getText().toString().equals(txtRePasswordChange.getText().toString())) {
            txtFailChangePassword.setText("Mật khẩu nhập lại phải trùng với mật khẩu");
            return;
        } else {
            txtFailChangePassword.setText("thay đổi thành công");
            UserService.getInstance().changePassword(ChangePasswordActivity.this, userID, txtOldPassword.getText().toString(), txtPasswordChange.getText().toString(),new VolleyCallBack() {
                @Override
                public void onSuccess(Object response) {
                    // change pass successful
                }

                @Override
                public void onError(Object ex) {
                    //change pass fail
                }
            });
        }
    }
}
