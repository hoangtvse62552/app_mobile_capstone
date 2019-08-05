package com.example.tfs.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tfs.api.CONST;
import com.example.tfs.api.VolleyCallBack;
import com.example.tfs.dto.Users;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import static android.content.ContentValues.TAG;

public class UserService {

    private static UserService userService;

    public UserService() {
    }

    public static UserService getInstance() {
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }

    public void getUserInformation(Context context, int userId, final VolleyCallBack volleyCallBack) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = CONST.LOCAL_HOST + "api/Distributor/getProfile/" + userId;
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            String json = response.toString();
//                            ObjectMapper mapper = new ObjectMapper();
//                            JsonNode jsonNode = mapper.readTree(json);
//                            String phoneNum = jsonNode.get("PhoneNo").asText();
//                            String fullname = jsonNode.get("Fullname").asText();
//                            String email = jsonNode.get("Email").asText();
                            response.toString();
                            volleyCallBack.onSuccess(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyCallBack.onError(error);
                    }
                }
        ) {

        };
        requestQueue.add(objectRequest);
    }

    public void changePassword(Context context, int userId, String oldPassword, String newPassword, final VolleyCallBack volleyCallBack) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = CONST.LOCAL_HOST + "api/Distributor/changePassword/" + userId;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oldPassword", oldPassword);
            jsonObject.put("newPassword", newPassword);
            //

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode node = mapper.readTree(json);
                            int status = node.get("Status").asInt();
                            if (status == 200) {
                                volleyCallBack.onSuccess(response);
                            } else {
                                //fail
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyCallBack.onError(error);
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public void changeProfile(Context context, int userId, String fullname, String phone, String email, final VolleyCallBack volleyCallBack) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = CONST.LOCAL_HOST + "api/Distributor/account/" + userId;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", userId);
            jsonObject.put("Fullname", fullname);
            jsonObject.put("Email", email);
            jsonObject.put("PhoneNo", phone);
            //

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String json = response.toString();
                            ObjectMapper mapper = new ObjectMapper();
                            JsonNode node = mapper.readTree(json);
                            int status = node.get("Status").asInt();
                            if (status == 200) {
                                volleyCallBack.onSuccess(response);
                            } else {
                                //fail
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyCallBack.onError(error);
                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public void Login(Context context, String username, String password, final VolleyCallBack callback) {

        final String URL = CONST.LOCAL_HOST + "api/Guest/login";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        username = "verter1";
        password = "123";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Username", username);
            jsonObject.put("Password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                        Log.e(TAG, "onErrorResponse: " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(objectRequest);
    }

}
