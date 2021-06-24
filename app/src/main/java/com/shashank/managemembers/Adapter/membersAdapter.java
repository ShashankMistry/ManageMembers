package com.shashank.managemembers.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shashank.managemembers.Activities.editMember;
import com.shashank.managemembers.R;
import com.shashank.managemembers.models.members;

import java.util.List;

public class membersAdapter extends ArrayAdapter<members> {
    Context mContext;

    public membersAdapter(@NonNull Context context, @NonNull List<members> objects) {
        super(context, 0, objects);
        mContext = context;
    }

    private static class ViewHolder {
        TextView nameTv;
        TextView mobileTv;
        TextView drinksTv;
        TextView joinTv;
        TextView dobTv;
        ImageView Edit;
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String mobile = getItem(position).getMobile();
        String drinks = getItem(position).getDrinksList();
        String join = getItem(position).getJoinDate();
        String dob = getItem(position).getDob();
        String memberCode = getItem(position).getMemberCode();
        ViewHolder holder;
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.raw_list_members, parent, false);
            holder = new ViewHolder();
            holder.nameTv = convertView.findViewById(R.id.Name);
            holder.mobileTv = convertView.findViewById(R.id.Mobile);
            holder.drinksTv = convertView.findViewById(R.id.currentDrinks);
            holder.joinTv = convertView.findViewById(R.id.joinDate);
            holder.dobTv = convertView.findViewById(R.id.DOB);
            holder.Edit = convertView.findViewById(R.id.Edit);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTv.setText("Name: "+ name);
        holder.mobileTv.setText("Mobile: "+mobile);
        holder.drinksTv.setText("Drinks:\n"+drinks);
        holder.joinTv.setText("Join Date:\n"+join);
        holder.dobTv.setText("DOB:\n"+dob);

        holder.Edit.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, editMember.class);
            intent.putExtra("CODE",memberCode);
            mContext.startActivity(intent);
        });

        return convertView;
    }
}
