package com.example.tfs.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfs.R;
import com.example.tfs.api.VolleyCallBack;
import com.example.tfs.dto.Users;
import com.example.tfs.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProfileActivity extends AppCompatActivity {

    private Users users;
    private EditText txtFullname;
    private EditText txtEmail;
    private EditText txtPhone;
    private Button btnChangepassword;
    private ProgressDialog progressDialog;
    private String phoneNum;
    private String fullname;
    private String email;
    private String img;
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressDialog = new ProgressDialog(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getInt("userID", 0);
        initInformation(userId);

    }
    public void initInformation(int userID) {
        UserService.getInstance().getUserInformation(ProfileActivity.this, userID, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {
                try {
                    String json = response.toString();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(json);
                    fullname = jsonNode.get("Fullname").asText();
                    phoneNum = jsonNode.get("PhoneNo").asText();
                    email = jsonNode.get("Email").asText();
                    img = jsonNode.get("Image").asText();

                    txtFullname = findViewById(R.id.txtNameEdit);
                    txtPhone = findViewById(R.id.txtPhone);
                    txtEmail = findViewById(R.id.txtEmail);

                    txtFullname.setText(fullname);
                    txtPhone.setText(phoneNum);
                    txtEmail.setText(email);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Object ex) {
                //Fail
                System.out.println(ex);
                Toast.makeText(ProfileActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void changeProfile(View v) {
        txtFullname = findViewById(R.id.txtNameEdit);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        String fullnameStr = txtFullname.getText().toString().trim();
        String phoneStr = txtPhone.getText().toString().trim();
        String emailStr = txtEmail.getText().toString().trim();

        UserService.getInstance().changeProfile(ProfileActivity.this, userId, fullnameStr, phoneStr, emailStr, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {
                //Change successful
                success();
            }
            @Override
            public void onError(Object ex) {
                // change fail
                Toast.makeText(getApplicationContext(), "Change fail", Toast.LENGTH_LONG);
            }
        });

    }
    public void success() {
        Toast.makeText(ProfileActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
    }
    public void logout(View v) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void goToChangePassword(View view) {
        Intent intent =new Intent(getApplicationContext(),ChangePasswordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    public void scanCode(View view) {

        Intent intent =new Intent(getApplicationContext(),ScanCodeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
    public void goToProfile(View view) {
        Intent intent =new Intent(getApplicationContext(),ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
    public void goToScanCode(View view) {

        Intent intent =new Intent(getApplicationContext(),HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
    public void goToListFood(View view) {
        Intent intent =new Intent(getApplicationContext(),ListFoodActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
}
