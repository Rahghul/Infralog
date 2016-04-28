package com.sncf.itif.Services.Gare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.sncf.itif.R;
import com.sncf.itif.Services.Info.Info;


public class GareListViewInfoAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<Info> infoList = new ArrayList<Info>();

    public GareListViewInfoAdapter(Context context, List<Info> infoList) {
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
            convertView = layoutInflater.inflate(R.layout.act_tab_gare_list_view_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            //holder.tvContext = (TextView) convertView.findViewById(R.id.tvContext);
            holder.tvDateTime = (TextView) convertView.findViewById(R.id.tvDateTime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(infoList.get(position).getTitle());
        //holder.tvContext.setText(infoList.get(position).getContext());
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        holder.tvDateTime.setText(sfd.format(infoList.get(position).getDateTime()));



        holder.tvTitle.setTag(String.valueOf(position));
        //holder.tvContext.setTag(String.valueOf(position));
        holder.tvDateTime.setTag(String.valueOf(position));


        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        //TextView tvContext;
        TextView tvDateTime;
    }

}

