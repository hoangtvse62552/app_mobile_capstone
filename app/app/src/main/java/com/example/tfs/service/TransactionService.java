package com.example.tfs.service;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tfs.api.CONST;
import com.example.tfs.api.VolleyCallBack;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TransactionService {

    private static TransactionService transactionService;

    public TransactionService() {
    }

    public static TransactionService getInstance() {
        if (transactionService == null) {
            transactionService = new TransactionService();
        }
        return transactionService;
    }

    public void getTransaction(Context context, int transactionId, String role, final VolleyCallBack volleyCallBack) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = CONST.LOCAL_HOST + "api/" + role + "/transaction/" + transactionId;

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyCallBack.onSuccess(response);
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

    public void updateTransaction(Context context, int transactionId, int status, String reason, String role,int userID, final VolleyCallBack volleyCallBack) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = CONST.LOCAL_HOST + "api/" + role + "/transaction/" + transactionId + "?tranId=" + transactionId;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("StatusId", status);
            jsonObject.put("RejectedReason", reason);
            jsonObject.put("RejectById", userID );

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
                        volleyCallBack.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyCallBack.onError(error);
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
