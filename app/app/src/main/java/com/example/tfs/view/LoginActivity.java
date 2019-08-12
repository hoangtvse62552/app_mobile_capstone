package com.example.tfs.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tfs.R;
import com.example.tfs.api.VolleyCallBack;
import com.example.tfs.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.util.Base64;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog myProgress;
    private static String token;
    private static Base64 base64;
    private int userId;
    private String role;
    private int premisesId;
    private String fullname;
    private String phoneNum;
    private String email;
    private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void checkLogin(View view) {

        EditText username = findViewById(R.id.txtUsername);
        EditText password = findViewById(R.id.txtPassword);
        String txtUsername = username.getText().toString().trim();
        String txtPassword = password.getText().toString().trim();

//        if(txtUsername.isEmpty()) {
//            Toast.makeText(this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        goToHome();

        UserService.getInstance().Login(LoginActivity.this, txtUsername, txtPassword, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {
                try {
                    String json = response.toString();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node = mapper.readTree(json);
                    token = node.get("Data").get("Token").toString();
                    decodeToken();
                    goToHome();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(Object ex) {
                loginFail();
            }
        });
    }

    public static void decodeToken() {
        String[] split_string = token.split("\\.");
        String base64EncodedBody = split_string[1];
        byte[] decode = Base64.decode(base64EncodedBody, Base64.DEFAULT);
        token = new String(decode);
    }

    public void goToHome() {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(token);
            userId = Integer.parseInt(node.get("userID").asText());
            if (node.get("premisesID").asText().length() != 0) {
                premisesId = Integer.parseInt(node.get("premisesID").asText());
            }

            if (token.contains("Distributor") || token.contains("Veterinary") ) {
            } else {
                loginFail();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserService.getInstance().getUserInformation(LoginActivity.this, userId, new VolleyCallBack() {
            @Override
            public void onSuccess(Object response) {
                try {
                    String json = response.toString();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(json);
                    phoneNum = jsonNode.get("PhoneNo").asText();
                    fullname = jsonNode.get("Fullname").asText();
                    email = jsonNode.get("Email").asText();
//                    img = node.get("Image").asText();
                    img = json;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Object ex) {
                //Fail
                Toast.makeText(LoginActivity.this, "" + ex, Toast.LENGTH_SHORT).show();
                return;
            }
        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userID", userId);
        editor.putInt("premisesId", premisesId);
        editor.putString("token", token);
//        editor.putString("phoneNum", phoneNum);
//        editor.putString("fullname", fullname);
//        editor.putString("email", email);
//        editor.putString("img", img);

        editor.apply();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    public void loginFail() {
        Toast.makeText(this, "tài khoản không hợp lệ", Toast.LENGTH_SHORT).show();
    }

}
