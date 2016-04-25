package com.sncf.itif.Services.Info;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.sncf.itif.R;


public class CustomAdapterInfo extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<Info> infoList = new ArrayList<Info>();

    public CustomAdapterInfo(Context context, List<Info> infoList) {
        super();
        this.context = context;
        this.infoList = infoList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Object getItem(int position) {
        return infoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return infoList.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.one_item_list_info, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvContext = (TextView) convertView.findViewById(R.id.tvContext);
            holder.imgWarning1 = (ImageView) convertView.findViewById(R.id.imgDegree1);
            holder.imgWarning2 = (ImageView) convertView.findViewById(R.id.imgDegree2);
            holder.imgWarning3 = (ImageView) convertView.findViewById(R.id.imgDegree3);
            holder.tvDateTime = (TextView) convertView.findViewById(R.id.tvDateTime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(infoList.get(position).getTitle());
        holder.tvContext.setText(infoList.get(position).getContext());
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        holder.tvDateTime.setText(sfd.format(infoList.get(position).getDateTime()));

        holder.imgWarning1.setImageResource(android.R.color.transparent);
        holder.imgWarning2.setImageResource(android.R.color.transparent);
        holder.imgWarning3.setImageResource(android.R.color.transparent);

        if (infoList.get(position).getDegree().equals("Faible")) {
            holder.imgWarning1.setImageResource(R.drawable.ic_warning_red_18dp);
        }


        if (infoList.get(position).getDegree().equals("Moyen")) {
            holder.imgWarning1.setImageResource(R.drawable.ic_warning_red_18dp);
            holder.imgWarning2.setImageResource(R.drawable.ic_warning_red_18dp);
        }

        if (infoList.get(position).getDegree().equals("Fort")) {
            holder.imgWarning1.setImageResource(R.drawable.ic_warning_red_18dp);
            holder.imgWarning2.setImageResource(R.drawable.ic_warning_red_18dp);
            holder.imgWarning3.setImageResource(R.drawable.ic_warning_red_18dp);
        }



        holder.tvTitle.setTag(String.valueOf(position));
        holder.tvContext.setTag(String.valueOf(position));
        holder.imgWarning1.setTag(String.valueOf(position));
        holder.imgWarning2.setTag(String.valueOf(position));
        holder.imgWarning3.setTag(String.valueOf(position));
        holder.tvDateTime.setTag(String.valueOf(position));


        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvContext;
        TextView tvDateTime;
        ImageView imgWarning1;
        ImageView imgWarning2;
        ImageView imgWarning3;

    }

}

