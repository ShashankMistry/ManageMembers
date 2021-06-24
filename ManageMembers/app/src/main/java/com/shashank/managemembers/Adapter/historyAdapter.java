package com.shashank.managemembers.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shashank.managemembers.R;
import com.shashank.managemembers.models.history;

import java.util.List;

public class historyAdapter extends ArrayAdapter<history> {

    public historyAdapter(@NonNull Context context, @NonNull List<history> objects) {
        super(context, 0, objects);
    }

    private static class ViewHolder {
        TextView nameTv;
        TextView mobileTv;
        TextView orderTv;
        TextView dateTv;
        TextView timeTv;
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        String name = getItem(position).getNameHistory();
        String mobile = getItem(position).getMobileHistory();
        String drinks = getItem(position).getOrderHistory();
        String time = getItem(position).getTimeHistory();
        String date = getItem(position).getDateHistory();
//        String memberCode = getItem(position).getMemberCode();
        ViewHolder holder;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.history_lv, parent, false);
            holder = new ViewHolder();
            holder.nameTv = convertView.findViewById(R.id.nameHis);
            holder.mobileTv = convertView.findViewById(R.id.mobileHis);
            holder.orderTv = convertView.findViewById(R.id.boughtDrinks);
            holder.dateTv = convertView.findViewById(R.id.date);
            holder.timeTv = convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        }else{
            holder = (historyAdapter.ViewHolder) convertView.getTag();
        }
        holder.nameTv.setText("Name: "+ name);
        holder.mobileTv.setText("Mobile: "+mobile);
        holder.orderTv.setText("Drinks:\n"+drinks);
        holder.dateTv.setText("Date:\n"+date);
        holder.timeTv.setText("Time:\n"+time);
        return convertView;
    }
}
