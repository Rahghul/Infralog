package com.sncf.itif.Services.Plan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.sncf.itif.Services.DetailPlan.DetailPlanActivity;
import com.sncf.itif.Services.Network.NetworkOpt;
import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.R;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PlanActivity extends AppCompatActivity implements ServiceCallBack {

    ServicePlan servicePlan;
    List<Plan> planList = new ArrayList<>();


    Context mContext;

    GridView gridView;
    GridViewPlanAdapter gridAdapter;

    Long secteurId;
    String gareName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Désactive le mode capture d'écran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.content_display_plan);

        getSupportActionBar().setTitle(R.string.plan_title);
        getSupportActionBar().setSubtitle(R.string.home_title);


        mContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        secteurId = intent.getLongExtra("SelectedSecteurId", -1);
        gareName = intent.getStringExtra("SelectedGareName");

        //showMessage("Secteur Id", secteurId.toString());
        gridView = (GridView) findViewById(R.id.gridView);

    }

    @Override
    public void onResume() {
        if (NetworkOpt.isNetworkAvailable(this) == false) {
            NetworkOpt.showNetworkAlert(this);
        } else {
            callServicePlanFromSecteur(secteurId);
        }

        super.onResume();
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void callServicePlanFromSecteur(Long secteurID) {
        servicePlan = new ServicePlan(this, this, "getPlanFromSecteur");
        String urlPlanGetFromSecteur = getString(R.string.dns) + getString(R.string.url_secteur);

        servicePlan.enquiry(urlPlanGetFromSecteur + secteurID.toString() + "/carte");
    }

    @Override
    public void serviceSuccess(Object object, int id_srv) {
        if (id_srv == 3) {
            planList.clear();
            if (object != null) {
                planList.addAll((List<Plan>) object);
                //showMessage("Plan List", planList.toString());
                gridAdapter = new GridViewPlanAdapter(this, R.layout.grid_item_plan_layout, planList);
                gridView.setAdapter(gridAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Plan itemPlan = (Plan) parent.getItemAtPosition(position);
                        //Save image
                        if (saveImageToInternalStorage(itemPlan) == false) {
                            Toast.makeText(mContext, "Image Clicked not saved !!!", Toast.LENGTH_LONG).show();
                        }

                        //Create intent
                        Intent intent = new Intent(mContext, DetailPlanActivity.class);
                        intent.putExtra("id", itemPlan.getId());
                        startActivity(intent);

                                                /*intent.putExtra("version", item.getVersion());
                        intent.putExtra("image", item.getImage());*/
                        //intent.putExtra("com.sncf.myapplication2.Services.Plan", item);

                    }
                });

            } else
                Toast.makeText(this, "La liste des secteurs est vide.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, "Error exception service failure PlanActivity " + exception.getMessage(), Toast.LENGTH_LONG).show();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return true;
    }


    public boolean saveImageToInternalStorage(Plan plan) {

        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream

            String imageName = "IMG%" + plan.getId() + "%" + gareName + "%" + plan.getReference() + "%" + plan.getVersion();
            //FileOutputStream fos = new FileOutputStream(this.getFilesDir().getAbsolutePath()+"/"+imageName);
            //Toast.makeText(mContext, imageName, Toast.LENGTH_LONG).show();
            FileOutputStream fos = this.openFileOutput(imageName, Context.MODE_PRIVATE);

            // Writing the bitmap to the output stream
            Bitmap image = StringToBitMap(plan.getPlan());
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }

}
