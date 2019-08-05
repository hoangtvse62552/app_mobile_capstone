package com.example.tfs.api;

public interface VolleyCallBack<T> {
    void onSuccess(T response);
    void onError(T ex);
}
