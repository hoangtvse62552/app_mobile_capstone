package com.example.tfs.view;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfs.R;
import com.example.tfs.api.VolleyCallBack;
import com.example.tfs.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeName;
    ProgressDialog progressDialog;
    private String fullname;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    progressDialog = new ProgressDialog(this);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sharedPreferences.getString("token","");
        fullname = sharedPreferences.getString("fullname","");
        userID = sharedPreferences.getInt("userID",0);

        initInformation(userID);
//        UserService.getInstance().getUserInformation(HomeActivity.this, userID, new VolleyCallBack() {
//            @Override
//            public void onSuccess(Object response) {
//                try {
//                    String json = response.toString();
//                    ObjectMapper mapper = new ObjectMapper();
//                    JsonNode jsonNode = mapper.readTree(json);
//                    fullname = jsonNode.get("Fullname").asText();
//                    welcomeName = findViewById(R.id.txtWelcomeName);
//                    welcomeName.setText("Chào mừng " + fullname);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onError(Object ex) {
//                //Fail
//                Toast.makeText(HomeActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
//            }
//        });



    }

    public void initInformation(int userID) {
        UserService.getInstance().getUserInformation(HomeActivity.this, userID, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {
                try {
                    String json = response.toString();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(json);
                    fullname = jsonNode.get("Fullname").asText();
                    welcomeName = findViewById(R.id.txtWelcomeName);
                    welcomeName.setText("Chào mừng " + fullname);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Object ex) {
                //Fail
                Toast.makeText(HomeActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
            }
        });
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
