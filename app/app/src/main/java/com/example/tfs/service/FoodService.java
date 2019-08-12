package com.example.tfs.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tfs.api.CONST;
import com.example.tfs.api.VolleyCallBack;
import com.example.tfs.dto.Foods;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;

public class FoodService {

    private static FoodService myInstance;

    public FoodService() {
    }

    public static FoodService getInstance() {
        if (myInstance == null) {
            myInstance = new FoodService();
        }
        return myInstance;
    }

    public void getFoods(final Context context, int foodId, final VolleyCallBack volley) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = CONST.LOCAL_HOST + "api/Staff/getFoodData?id=" + foodId;
//        String URL = "https://api.myjson.com/bins/ogv7p";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        volley.onSuccess(response);
//                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volley.onError(error);
            }
        });
        requestQueue.add(stringRequest);
    }

    public void updateDistributorFood(Context context, int distributorId, int foodId, final VolleyCallBack volleyCallBack) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = CONST.LOCAL_HOST + "api/Distributor/distributorFood/" + distributorId;
        JSONObject jsonObject = new JSONObject();
        try {
                jsonObject.put("FoodId", foodId);
        }catch (Exception e) {
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
        );
        requestQueue.add(objectRequest);
    }
}
