package com.sncf.itif.Services.Secteur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sncf.itif.R;

import java.util.List;

/**
 * Created by Rahghul on 23/03/2016.
 */
public class SecteurListViewAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<Secteur> secteurList;

    public SecteurListViewAdapter(Context context, List<Secteur> secteurList) {
        super();
        this.secteurList = secteurList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return secteurList.size();
    }

    @Override
    public Object getItem(int position) {
        return secteurList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return secteurList.indexOf(getItem(position));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.global_list_view_item, parent, false);
        }
        Secteur secteur = secteurList.get(position);
        if (secteur != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(secteur.getName());
        }
        return view;
    }



}
