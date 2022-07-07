package com.pujisudaryanto.tubesmbkelompok5;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GroceryRecyclerViewAdapter extends RecyclerView.Adapter<GroceryRecyclerViewAdapter.GroceryViewHolder>{

    //private List<Grocery> groceryList;
    private ArrayList<HashMap> groceryList;
    private RecyclerImageAdapter listener;

    public GroceryRecyclerViewAdapter(ArrayList<HashMap> groceryList, RecyclerImageAdapter listener) {
        this.groceryList = groceryList;
        this.listener = listener;
    }


    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_list, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder groceryViewHolder, int i) {
        HashMap xData = groceryList.get(i);
        String xtgl = formateDateFromstring("yyyy-mm-dd", "dd/mm/yy", xData.get("date").toString());
        groceryViewHolder.title.setText(xData.get("title").toString());
        groceryViewHolder.fee.setText(xData.get("fee").toString());
        groceryViewHolder.tanggal.setText(xtgl);
    }

    @Override
    public int getItemCount() {
        return (groceryList != null) ? groceryList.size() : 0;
    }

    class GroceryViewHolder extends RecyclerView.ViewHolder {
        private TextView title, fee, tanggal;
        ImageView btnHapus;
        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tPengeluaran);
            fee = itemView.findViewById(R.id.tHarga);
            tanggal = itemView.findViewById(R.id.tTanggal);
            btnHapus = itemView.findViewById(R.id.btnHapus);

            btnHapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRecyclerImageSelected(groceryList.get(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onRecyclerLongClick(groceryList.get(getAdapterPosition()));
                    return true;
                }
            });
        }
    }

    public interface RecyclerImageAdapter {
        void onRecyclerImageSelected(HashMap groceryList);
        void onRecyclerLongClick(HashMap groceryList);
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (ParseException e) {
            Log.d("Error : ", "ParseException - dateFormat");
        }

        return outputDate;

    }
}