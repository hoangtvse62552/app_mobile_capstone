package com.example.tfs.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tfs.api.CONST;
import com.example.tfs.api.VolleyCallBack;

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

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        volley.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volley.onError(error);
            }
        });
        requestQueue.add(stringRequest);
    }


}
