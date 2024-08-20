package com.example.sociotech.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sociotech.R;
import com.example.sociotech.model.parking.getparking.Datum;

import java.util.List;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ViewHolder> {

    private List<Datum> dataList;
    private Context context;

    public ParkingAdapter(Context context, List<Datum>  dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_parking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.spotNumber.setText(dataList.get(position).getParking().getSpotNumber());
        holder.location.setText(dataList.get(position).getParking().getLocation());
        holder.startDate.setText(dataList.get(position).getAllocationDate());
        holder.endDate.setText(dataList.get(position).getExpirationDate());
        holder.status.setText(dataList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView spotNumber, location, startDate,endDate,status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            spotNumber = itemView.findViewById(R.id.spotNumber);
            location = itemView.findViewById(R.id.location);
            startDate = itemView.findViewById(R.id.startDate);
            endDate = itemView.findViewById(R.id.endDate);
            status=itemView.findViewById(R.id.status);
        }
    }
}
