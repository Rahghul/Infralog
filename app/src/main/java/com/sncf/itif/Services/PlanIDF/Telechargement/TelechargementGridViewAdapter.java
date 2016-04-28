package com.sncf.itif.Services.PlanIDF.Telechargement;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sncf.itif.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahghul on 23/03/2016.
 */
public class TelechargementGridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List<Telechargement> data = new ArrayList();

    public TelechargementGridViewAdapter(Context context, int layoutResourceId, List<Telechargement> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.act_telechargement_grid_view_item, parent, false);
            holder = new ViewHolder();
            holder.reference = (TextView) row.findViewById(R.id.txt_line1);
            holder.version = (TextView) row.findViewById(R.id.txt_line2);
            holder.remainingDays = (TextView) row.findViewById(R.id.txt_line3);

            holder.image = (ImageView) row.findViewById(R.id.image_small);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Telechargement item = data.get(position);
        holder.image.setImageBitmap(item.getImage());
        holder.reference.setText(item.getReference());
        holder.version.setText("Version: " + item.getVersion());
        holder.remainingDays.setText("Jours restants: " + (item.getRemainingDays()-1));
        return row;
    }

    static class ViewHolder {
        TextView reference;
        TextView version;
        ImageView image;
        TextView remainingDays;
    }
}