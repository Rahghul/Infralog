package com.sncf.itif.Services.Gare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.sncf.itif.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahghul on 21/03/2016.
 */
public class CustomAdapterGare extends ArrayAdapter<Gare> {

    Context context;
    int resource, textViewResourceId;
    List<Gare> items, tempItems, suggestions;

    public CustomAdapterGare(Context context, int resource, int textViewResourceId, List<Gare> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Gare>(items); // this makes the difference.
        suggestions = new ArrayList<Gare>();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_view_row_item, parent, false);
        }
        Gare gare = items.get(position);
        if (gare != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(gare.getName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Gare) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                String regex = "\\s*-\\s*";


                for (Gare gare : tempItems) {
                    String typed_word = constraint.toString().trim().toLowerCase().replaceAll(regex," ");
                    String g_name = gare.getName().trim().toLowerCase().replaceAll(regex," ");
                    if ((" "+g_name).contains(" "+typed_word)) {
                        suggestions.add(gare);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Gare> filterList = (ArrayList<Gare>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Gare gare : filterList) {
                    add(gare);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
