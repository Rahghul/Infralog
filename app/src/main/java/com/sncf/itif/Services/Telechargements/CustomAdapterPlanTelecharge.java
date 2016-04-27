package com.sncf.itif.Services.Telechargements;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sncf.itif.R;
import com.sncf.itif.Services.DetailPlan.DetailPlanActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rahghul on 26/04/2016.
 */
public class CustomAdapterPlanTelecharge extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    HashMap<Integer, String> hashSupprimeDoublonsGareName = new HashMap();
    List<Telechargements> listImageOfSameGare;
    GridViewTelechargementsAdapter gridAdapter;
    HashMap<Integer, List<Telechargements>> hashPlansTelecharge = new HashMap();
    int i = 0;

    public CustomAdapterPlanTelecharge(Context context, List<Telechargements> downloadPlan) {
        super();
        this.context = context;
        //Ici on supprime les doublons des noms de gares. Au final, hashSupprimeDoublonsGareName
        //contient uniquement les noms de gares sans doublons
        for (Telechargements t : downloadPlan) {
            if (!hashSupprimeDoublonsGareName.containsValue(t.getGareName())) {
                hashSupprimeDoublonsGareName.put(i++, t.getGareName());
            }
        }

        //On parcourt foreach de hashSupprimeDoublonsGareName pour affecter une clé unique
        //à chaque gare. Exemple : Garges, Plan1 ; Garges,Plan2 ; Noisy,Plan4
        //               Donc, List<Garges, Plan1 ; Garges,Plan2> -> key 1
        //                      List<Noisy,Plan4> -> key 2
        // Le resultat se trouve dans "hashPlansTelecharge"
        //Au final, en fonction de la clé, on attribuera la vue dans la listView(Voir ci dessous)
        for (Map.Entry<Integer, String> entry : hashSupprimeDoublonsGareName.entrySet()) {
            Integer key = entry.getKey();
            String gareName = entry.getValue();
            listImageOfSameGare = new ArrayList<Telechargements>();
            for (Telechargements t : downloadPlan) {
                if (gareName.equals(t.getGareName())) {
                    listImageOfSameGare.add(t);
                }
            }
            if (!listImageOfSameGare.isEmpty()) {
                hashPlansTelecharge.put(key, listImageOfSameGare);
            }

        }

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return hashSupprimeDoublonsGareName.size();
    }

    @Override
    public Object getItem(int position) {
        return hashSupprimeDoublonsGareName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.one_item_list_plan, null);
            holder.tvGareName = (TextView) convertView.findViewById(R.id.tvGareName);
            holder.gridView = (MyGridView) convertView.findViewById(R.id.gridViewTelecharge);

            //Simple gridView click event
            holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Telechargements itemImage = (Telechargements) parent.getItemAtPosition(position);


                    //goto page DetailPlanActivity
                    Intent intent = new Intent(context, DetailPlanActivity.class);
                    intent.putExtra("SavedImageTitle", itemImage.getTitle());
                    context.startActivity(intent);
                }
            });

            //Long Click GridView event
            holder.gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Telechargements itemImage = (Telechargements) parent.getItemAtPosition(position);

                    //Delete Image
                    confirmDeleteAlert(context, itemImage);

                    return true;
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvGareName.setText(hashSupprimeDoublonsGareName.get(position));
        //En focntion de la clé, on retrouve l'ensemble d'images correspondant pour regrouper.
        gridAdapter = new GridViewTelechargementsAdapter(context, R.layout.grid_item_plan_layout, hashPlansTelecharge.get((position)));
        holder.gridView.setAdapter(gridAdapter);


        holder.tvGareName.setTag(String.valueOf(position));
        holder.gridView.setTag(String.valueOf(position));

        return convertView;
    }

    class ViewHolder {
        TextView tvGareName;
        MyGridView gridView;
    }


    void deleteImageFromInternalStorage(String filename, Context mContext) {
        File filePath = mContext.getFileStreamPath(filename);

        //Delete file from internal storage
        filePath.delete();

        //Kill current activity to update a new list of plans.
        Intent intent = new Intent(context, TelechargementsActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();

        //Toast.makeText(mContext, "Le plan est supprimé.", Toast.LENGTH_LONG).show();
        gridAdapter.notifyDataSetChanged();
    }

    public void confirmDeleteAlert(final Context mContext, final Telechargements itemImage) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Supprimer");

        // Setting Dialog Message
        alertDialog.setMessage("Le plan sélectionné va être supprimé.");

        // On pressing Settings button
        alertDialog.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteImageFromInternalStorage(itemImage.getTitle(), mContext);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }



}
