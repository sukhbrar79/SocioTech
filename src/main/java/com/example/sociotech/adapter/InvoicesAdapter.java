package com.example.sociotech.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sociotech.R;
import com.example.sociotech.activity.PaymentActivity;
import com.example.sociotech.model.invoices.Datum;

import java.util.List;

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.ViewHolder> {

    private List<Datum> dataList;
    private Context context;

    public InvoicesAdapter(Context context, List<Datum> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.invoice_number.setText("#"+dataList.get(position).getInvoiceNumber());
        holder.status.setText(dataList.get(position).getStatus());
        holder.amount.setText("₹ "+dataList.get(position).getAmount().toString());
        holder.due_date.setText(dataList.get(position).getDueDate());

        holder.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, PaymentActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView invoice_number, status, amount,due_date;
        Button payButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            invoice_number = itemView.findViewById(R.id.invoice_number);
            status = itemView.findViewById(R.id.status);
            amount = itemView.findViewById(R.id.amount);
            due_date = itemView.findViewById(R.id.due_date);
            payButton=itemView.findViewById(R.id.payButton);

        }
    }
}
