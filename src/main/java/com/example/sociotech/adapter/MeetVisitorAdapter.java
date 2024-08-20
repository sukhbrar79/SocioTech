package com.example.sociotech.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sociotech.R;
import com.example.sociotech.databinding.ItemMeetVisitorBinding;
import com.example.sociotech.databinding.ItemServiceRequestBinding;
import com.example.sociotech.model.meetVisitor.getVisitor.Datum;

import java.util.List;

public class MeetVisitorAdapter extends RecyclerView.Adapter<MeetVisitorAdapter.ViewHolder> {

    private List<Datum> dataList;
    private Context context;

    public MeetVisitorAdapter(Context context, List<Datum> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meet_visitor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText("Name: "+dataList.get(position).getName());
        holder.contact.setText("Contact: "+dataList.get(position).getContactNumber());
        holder.purpose.setText("Purpose: "+dataList.get(position).getPurpose());
        holder.checkInDate.setText("Check-In Date: "+dataList.get(position).getCheck_in_date());
        holder.checkOutDate.setText("Check-Out Date: "+dataList.get(position).getCheck_out_date());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, contact, purpose,checkInDate,checkOutDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            contact = itemView.findViewById(R.id.contact);
            purpose = itemView.findViewById(R.id.purpose);
            checkInDate = itemView.findViewById(R.id.checkInDate);
            checkOutDate = itemView.findViewById(R.id.checkOutDate);
        }
    }
}
