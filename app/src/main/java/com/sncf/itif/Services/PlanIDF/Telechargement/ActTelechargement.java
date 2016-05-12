package com.sncf.itif.Services.PlanIDF.Telechargement;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.sncf.itif.R;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rahghul on 15/04/2016.
 */
public class ActTelechargement extends AppCompatActivity {

    List<Telechargement> planList = new ArrayList<Telechargement>();

    int remainingDays;

    Context mContext;

    ListView listPlansTelecharges;
    TelechargementListViewAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Désactive le mode capture d'écran
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.act_telechargement);

        getSupportActionBar().setTitle(R.string.act_telechargement_tv_title);
        getSupportActionBar().setSubtitle(R.string.global_tv_short_title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        //list files names of data/data/package Name/Files from internal storage
        listFiles();

        listPlansTelecharges = (ListView) findViewById(R.id.planListView);
        customAdapter = new TelechargementListViewAdapter(mContext, planList);
        listPlansTelecharges.setAdapter(customAdapter);
    }

    @Override
    protected void onStart() {

        super.onStart();
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

                    planList.add(new Telechargement(bm, strFile, retval[2], retval[3], retval[4], remainingDays));
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
            long durationInMillis;
            // si le délai depasse, on supprime le fichier
            //Toast.makeText(this, "res : " + ((nowTime - lastTime) / (1000*60*60)) + " h", Toast.LENGTH_LONG).show();
            if ((durationInMillis = nowTime - lastTime) > getResources().getInteger(R.integer.act_telechargement_nb_jrs_autorise_sauvegarde) * 1000 * 60 * 60 * 24) {
                filePath.delete();

            } else {
                // en fonction du temps parcouru, on défini le temps restant.
                int durationInDays = (int) (durationInMillis / (1000 * 60 * 60 * 24));
                //Le jour autorisé avant disparition du plan automatiquement
                int finalDay = getResources().getInteger(R.integer.act_telechargement_nb_jrs_autorise_sauvegarde);
                remainingDays = finalDay - durationInDays;
                Log.d("***", durationInMillis + " " + durationInDays + " " + remainingDays);

                //sinon on retourne l'image
                FileInputStream fi = new FileInputStream(filePath);
                thumbnail = BitmapFactory.decodeStream(fi);
            }

        } catch (Exception ex) {
            Log.e("Internal Storage", ex.getMessage());
        }
        return thumbnail;
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
//            Toast.makeText(mContext, "Appuyez longuement sur le plan pour supprimer définitivement.", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Suppression");
            alertDialog.setMessage("Appuyez longuement sur le plan pour supprimer définitivement.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();

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
