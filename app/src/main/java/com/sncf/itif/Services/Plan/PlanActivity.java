package com.sncf.itif.Services.Plan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.R;
import com.sncf.itif.Services.DetailPlan.DetailPlanActivity;

import java.util.ArrayList;
import java.util.List;

public class PlanActivity extends AppCompatActivity implements ServiceCallBack {

    ServicePlan servicePlan;
    List<Plan> planList = new ArrayList<>();


    //String urlPlanGetFromSecteur = getString(R.string.dns) + getString(R.string.url_secteur);

    ProgressDialog dialog;

    ImageView imgView;

    Context mContext;

    GridView gridView;
    GridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
      //          WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_display_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        imgView = (ImageView) findViewById(R.id.imageView2);
        dialog = new ProgressDialog(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Long secteurId = intent.getLongExtra("SelectedSecteurId",-1);
        //showMessage("Secteur Id", secteurId.toString());

        gridView = (GridView) findViewById(R.id.gridView);


        callServicePlanFromSecteur(secteurId);
    }


    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void callServicePlanFromSecteur(Long secteurID){
        servicePlan = new ServicePlan(this, dialog, "getPlanFromSecteur");
        String urlPlanGetFromSecteur = getString(R.string.dns) + getString(R.string.url_secteur);

        servicePlan.enquiry(urlPlanGetFromSecteur + secteurID.toString() + "/carte");
    }

    @Override
    public void serviceSuccess(Object object, int id_srv) {
        if(id_srv == 3){
            planList.clear();
            if(object != null){
                planList.addAll((List<Plan>) object);
                //showMessage("Plan List", planList.toString());
                gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, planList);
                gridView.setAdapter(gridAdapter);
                //imgView.setImageBitmap(StringToBitMap(planList.get(1).getPlan()));

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Plan item = (Plan) parent.getItemAtPosition(position);
                        //Create intent


                        Intent intent = new Intent(mContext, DetailPlanActivity.class);
                        intent.putExtra("id", item.getId());
                        startActivity(intent);

                                                /*intent.putExtra("version", item.getVersion());
                        intent.putExtra("image", item.getPlan());*/
                        //intent.putExtra("com.sncf.myapplication2.Services.Plan", item);

                        //Start details activity
                    }
                });

            }
            else
                Toast.makeText(this,"La liste des secteurs est vide.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this,"Error exception service failure PlanActivity "+exception.getMessage(), Toast.LENGTH_LONG).show();
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
}
