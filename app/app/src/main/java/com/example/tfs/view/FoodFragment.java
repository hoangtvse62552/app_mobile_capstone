package com.example.tfs.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfs.R;
import com.example.tfs.dto.Foods;

import java.util.ArrayList;
import java.util.List;

public class FoodFragment extends Fragment {
    View view;
    private List<Foods> foods = new ArrayList<>();
    private RecyclerView rvItems1;
    private ProgressDialog myProgress;
    private RecyclerDataAdapter adapter;

    public FoodFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_food_recycleview, container, false);
        myProgress = new ProgressDialog(getActivity());
        myProgress.setMessage("Please wait...");
        myProgress.setCancelable(true);
        myProgress.show();
//        RestfulAPIManager.getInstancẹ̣̣̣().getAllFoodPost(getActivity(), new VolleyCallback() {
//            @Override
//            public void onSuccess(Object response) {
//                JsonNode data = (JsonNode) response;
//                for (JsonNode item :
//                        data) {
//                    posts.add(dto);
//                }
//                updateList();
//                myProgress.dismiss();
//            }

//            @Override
//            public void onError(Object ex) {
//                myProgress.dismiss();
//            }
//        });
        foods.add(new Foods(1,"food 1", "abc"));
        foods.add(new Foods(2,"food 2", "abc"));
        foods.add(new Foods(3,"food 3", "abc"));
        foods.add(new Foods(4,"food 4", "abc"));
        foods.add(new Foods(5,"food 5", "abc"));

        updateList();
        myProgress.dismiss();
        return view;
    }
    private void updateList() {
        adapter = new RecyclerDataAdapter(view.getContext(), foods);
        rvItems1 = view.findViewById(R.id.rv_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rvItems1.setLayoutManager(layoutManager);
        rvItems1.setHasFixedSize(true);
        rvItems1.setAdapter(adapter);
    }
}
