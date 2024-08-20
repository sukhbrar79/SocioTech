package com.example.sociotech.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sociotech.R;
import com.example.sociotech.activity.HomeActivity;
import com.example.sociotech.model.guidelines.Datum;

import java.util.List;

public class GuidelinesAdapter extends RecyclerView.Adapter<GuidelinesAdapter.ViewHolder> {

    private List<com.example.sociotech.model.guidelines.Datum> dataList;
    private Context context;

    public GuidelinesAdapter(Context context, List<Datum> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_guidelines, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.guideline.setText("#" + dataList.get(position).getName());
        holder.guideline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_dialog(dataList.get(position).getDescription());
            }
        });
    }

    private void open_dialog(String guideline) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.guideline_dialog, null);

        // Create the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(customView);

        // Set up the dialog elements
         TextView title = customView.findViewById(R.id.guideline);
        ImageView cross=customView.findViewById(R.id.cross_button);
        title.setText(guideline+"");

        // Display the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView guideline, status, amount, due_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            guideline = itemView.findViewById(R.id.guideline);

        }
    }
}
