package com.sncf.itif.Services.Plan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
public class PlanGridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List<Plan> data = new ArrayList();

    public PlanGridViewAdapter(Context context, int layoutResourceId, List<Plan> data) {
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
            row = inflater.inflate(R.layout.act_plan_grid_view_item, parent, false);
            holder = new ViewHolder();
            holder.imageTitleRef = (TextView) row.findViewById(R.id.txt_line1);
            holder.imageTitleVers = (TextView) row.findViewById(R.id.txt_line2);
            holder.image = (ImageView) row.findViewById(R.id.image_small);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Plan item = data.get(position);
        holder.imageTitleRef.setText(item.getReference());
        holder.imageTitleVers.setText("Version: " + item.getVersion());
        holder.image.setImageBitmap(StringToBitMap(item.getPlan()));
        return row;
    }

    static class ViewHolder {
        TextView imageTitleRef;
        TextView imageTitleVers;
        ImageView image;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}