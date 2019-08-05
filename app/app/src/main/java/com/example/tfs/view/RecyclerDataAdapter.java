package com.example.tfs.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tfs.R;
import com.example.tfs.dto.Foods;
//import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerDataAdapter.DataViewHolder> {

    private List<Foods> listFood;
    private Context context;

    public RecyclerDataAdapter(Context context, List<Foods> listFood) {
        this.listFood = listFood;
        this.context = context;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_food_view, viewGroup, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerDataAdapter.DataViewHolder holder, int position) {
        Foods dto = listFood.get(position);
        holder.foodName.setText(dto.getName());
//        Picasso.with(context).load(dto.getBanner()).error(R.drawable.image_not_available).into(holder.post_image);
//        Picasso.with(context).load(dto.getLogoStore()).error(R.drawable.image_not_available).into(holder.logoPostCartScreen);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(view.getContext(), FoodDetailActivity.class);
                Foods dto = listFood.get(position);
                intent.putExtra("FOODID", dto.getId());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listFood == null ? 0 : listFood.size();
    }


    public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView foodName;
        private ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public DataViewHolder(final View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.txtFoodName);
//            GET VIEW BY ID
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

}

