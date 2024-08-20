package com.example.sociotech.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sociotech.R;
import com.example.sociotech.activity.EmergencyContactsActivity;
import com.example.sociotech.activity.InvoicesActivity;
import com.example.sociotech.activity.ParkingActivity;
import com.example.sociotech.activity.ServiceRequestActivity;
import com.example.sociotech.model.emergencyContacts.Datum;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    ArrayList name;
    ArrayList images;
    Context context;

    public HomeAdapter(Context context, ArrayList personNames, ArrayList personImages) {
        this.context = context;
        this.name = personNames;
        this.images = personImages;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(name.get(position).toString());
       holder.image.setImageResource((Integer) images.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position==0)
                {
                    // open another activity on item click
                Intent intent = new Intent(context, InvoicesActivity.class);
                context.startActivity(intent); // start Intent
                }
                 if (position==1)
                {
                    // open another activity on item click
                    Intent intent = new Intent(context, ServiceRequestActivity.class);
                    context.startActivity(intent); // start Intent
                }

                if (position==2)
                {
                    // open another activity on item click
                    Intent intent = new Intent(context, ParkingActivity.class);
                    context.startActivity(intent); // start Intent
                }

                if (position==3)
                {
                    // open another activity on item click
                    Intent intent = new Intent(context, EmergencyContactsActivity.class);
                    context.startActivity(intent); // start Intent
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}