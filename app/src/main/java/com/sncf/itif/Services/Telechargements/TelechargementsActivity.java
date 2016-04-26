package com.sncf.itif.Services.Telechargements;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.sncf.itif.R;
import com.sncf.itif.Services.DetailPlan.DetailPlanActivity;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rahghul on 15/04/2016.
 */
public class TelechargementsActivity extends AppCompatActivity {
    GridView gridView;
    GridViewBitmapAdapter gridAdapter;
    List<Telechargements> planList = new ArrayList<Telechargements>();

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Désactive le mode capture d'écran
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_plan);

        getSupportActionBar().setTitle(R.string.act_telechargements_title);
        getSupportActionBar().setSubtitle(R.string.app_short_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        //list files names of data/data/package Name/Files from internal storage
        listFiles();

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewBitmapAdapter(this, R.layout.grid_item_plan_layout, planList);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Telechargements itemImage = (Telechargements) parent.getItemAtPosition(position);


                //Create intent
                Intent intent = new Intent(mContext, DetailPlanActivity.class);
                intent.putExtra("SavedImageTitle", itemImage.getTitle());
                startActivity(intent);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Telechargements itemImage = (Telechargements) parent.getItemAtPosition(position);

                //Delete Image
                confirmDeleteAlert(mContext, itemImage);

                return true;
            }
        });

    }

    void listFiles() {
        File dirFiles = this.getFilesDir();
        planList.clear();
        for (String strFile : dirFiles.list()) {
            Log.d("+++++Internal Storage :", strFile);
            if (strFile.contains("IMG")) {
                Bitmap bm = getThumbnail(strFile);
                //l'image doit etre non nulle puisque si le temps depasse l'image sera supprimé definitivement
                if (bm != null) {
                    String[] retval = strFile.split("%");
                    planList.add(new Telechargements(bm, strFile, retval[2], retval[3], retval[4]));
                }
            }
        }
    }

    public Bitmap getThumbnail(String filename) {

        Bitmap thumbnail = null;

        try {
            File filePath = this.getFileStreamPath(filename);

            long lastTime = filePath.lastModified();
            Date nowDate = new Date();
            long nowTime = nowDate.getTime();
            // si le délai depasse, on supprime le fichier
            //Toast.makeText(this, "res : "+((nowTime - lastTime)/1000) + " sec", Toast.LENGTH_LONG).show();
            if (nowTime - lastTime > R.integer.temps_sauvegarde_telechargements) { //=1 h
                filePath.delete();

            } else {
                //sinon on retourne l'image
                FileInputStream fi = new FileInputStream(filePath);
                thumbnail = BitmapFactory.decodeStream(fi);
            }

        } catch (Exception ex) {
            Log.e("Internal Storage", ex.getMessage());
        }
        return thumbnail;
    }

    void deleteImageFromInternalStorage(String filename) {
        File filePath = this.getFileStreamPath(filename);
        filePath.delete();
//        Toast.makeText(mContext, "Le plan est supprimé.", Toast.LENGTH_LONG).show();
        listFiles();
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
                deleteImageFromInternalStorage(itemImage.getTitle());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_telechargements, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            Toast.makeText(mContext, "Appuyez longuement sur le plan pour supprimer définitivement.", Toast.LENGTH_SHORT).show();
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;

        }
        return true;
    }
}
