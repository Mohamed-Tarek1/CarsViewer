package com.example.carsviewer;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CarRVAdabter extends RecyclerView.Adapter<CarRVAdabter.CarViewHolder> {

    private ArrayList<Car> cars;

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    private OnRecyclerViewItemClickListener listener;
    public CarRVAdabter (ArrayList<Car> cars , OnRecyclerViewItemClickListener listener){
        this.cars= cars;
        this.listener= listener;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_car_layout,null,false);
        CarViewHolder viewHolder = new CarViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car c = cars.get(position);

        if(c.getImage() != null && !c.getImage().isEmpty())
            holder.image.setImageURI(Uri.parse(c.getImage()));

        holder.tv_dpl.setText(String.valueOf(c.getDpl()));
        holder.tv_model.setText(c.getModel());
        holder.tv_color.setText(c.getColor());
        try {
            holder.tv_color.setTextColor(Color.parseColor(c.getColor()));
        }catch (Exception e){

        }
        holder.image.setTag(c.getId());

    }

    @Override
    public int getItemCount() {
        return cars.size();

    }

    class CarViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView tv_color , tv_model , tv_dpl;
        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.custom_car_iv);
            tv_color = itemView.findViewById(R.id.custom_car_tv_color);
            tv_model = itemView.findViewById(R.id.custom_car_tv_model);
            tv_dpl = itemView.findViewById(R.id.custom_car_tv_dpl);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = (int) image.getTag();
                     listener.onItemClick(id);
                }
            });
        }
    }

}
