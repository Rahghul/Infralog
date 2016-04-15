package com.sncf.itif.Services.SavedImages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

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
public class SavedImageActivity extends AppCompatActivity {
    GridView gridView;
    GridViewBitmapAdapter gridAdapter;
    List<SavedImage> planList = new ArrayList<>();

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_display_plan);

        getSupportActionBar().setTitle(R.string.saved_image_title);
        getSupportActionBar().setSubtitle(R.string.home_title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        //list files names of data/data/package Name/Files from internal storage
        listFiles();

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewBitmapAdapter(this, R.layout.grid_item_plan_layout, planList);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                SavedImage itemImage = (SavedImage) parent.getItemAtPosition(position);


                //Create intent
                Intent intent = new Intent(mContext, DetailPlanActivity.class);
                intent.putExtra("SavedImageTitle", itemImage.getTitle());
                startActivity(intent);
            }
        });
    }

    void listFiles() {
        File dirFiles = this.getFilesDir();
        for (String strFile : dirFiles.list()) {
            Log.d("+++++Internal Storage :", strFile);
            if (strFile.contains("IMG")) {
                Bitmap bm = getThumbnail(strFile);
                //l'image doit etre non nulle puisque si le temps depasse l'image sera supprimé definitivement
                if(bm != null) {
                    String[] retval = strFile.split("%");
                    planList.add(new SavedImage(bm, strFile, retval[2], retval[3], retval[4]));
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
            if (nowTime - lastTime > 60*60*1000){ //=1 h
                filePath.delete();
            }
            else {
                //sinon on retourne l'image
                FileInputStream fi = new FileInputStream(filePath);
                thumbnail = BitmapFactory.decodeStream(fi);
            }

        } catch (Exception ex) {
            Log.e("Internal Storage", ex.getMessage());
        }
        return thumbnail;
    }
}
