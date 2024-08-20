package com.example.sociotech.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sociotech.R;
import com.example.sociotech.databinding.ItemServiceRequestBinding;
import com.example.sociotech.model.serviceRequest.serviceRequestListingResponse.Datum;

import java.util.List;

public class ServiceRequestAdapter extends RecyclerView.Adapter<ServiceRequestAdapter.ViewHolder> {

    private List<Datum> dataList;
    private Context context;

    public ServiceRequestAdapter(Context context, List<Datum> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id.setText("#"+dataList.get(position).getId().toString());
        holder.status.setText(dataList.get(position).getStatus());
        holder.subject.setText(dataList.get(position).getSubject());
        holder.description.setText(dataList.get(position).getDescription());
        if(dataList.get(position).getStatus().equals("pending"))
        {
            holder.status.setText("PENDING");
            holder.status.setTextColor(context.getResources().getColor(R.color.numbers_background));

        }
        else  if(dataList.get(position).getStatus().equals("in_progress"))
        {
            holder.status.setText("IN PROGRESS");
            holder.status.setTextColor(context.getResources().getColor(R.color.colors_background));
        }
       else if(dataList.get(position).getStatus().equals("resolved"))
        {
            holder.status.setText("RESOLVED");
            holder.status.setTextColor(context.getResources().getColor(R.color.green));

        }
        else  if(dataList.get(position).getStatus().equals("closed"))
        {
            holder.status.setText("CLOSED");
            holder.status.setTextColor(context.getResources().getColor(R.color.brown_500));

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, status, subject, description;
        private ItemServiceRequestBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            status = itemView.findViewById(R.id.status);
            subject = itemView.findViewById(R.id.subject);
            description = itemView.findViewById(R.id.description);

        }
    }
}
