package com.sncf.itif.Services.Secteur;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sncf.itif.Services.Plan.PlanActivity;
import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahghul on 23/03/2016.
 */
public class SecteurActivity extends AppCompatActivity implements ServiceCallBack {

    ServiceSecteur serviceSecteur;
    List<Secteur> secteursList = new ArrayList<>();
    ListView secteursListView;
    CustomAdapterSecteur secteurAdapter;

    String DNS = "http://itif.cloudapp.net/";
    //String DNS = "http://10.0.3.2:8080/";

    // Service Gare

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_display_secteur);

        mContext = this;
        secteursListView = (ListView)findViewById(R.id.secteurListView);
        secteurAdapter = new CustomAdapterSecteur(this, secteursList);
        secteursListView.setAdapter(secteurAdapter);

        Intent intent = getIntent();
        final Long gareId = intent.getLongExtra("SelectedGareId",-1);

        secteursListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = secteursListView.getItemAtPosition(position);
                //showMessage("Item Clicked:", ((Secteur)o).toString());
                //start display Secteur Activity
                Intent intent = new Intent(mContext, PlanActivity.class);
                intent.putExtra("SelectedSecteurId", ((Secteur) o).getId());
                startActivityForResult(intent, 1);
            }
        });


       // ArrayList<Secteur> secteurList =  (ArrayList<Secteur>)intent.getSerializableExtra("SecteurList");
        //showMessage("Gare ID:", gareId.toString());

        callServiceSecteurFromGare(gareId);

    }


    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void callServiceSecteurFromGare(Long gareID){
        serviceSecteur = new ServiceSecteur(this, this, "getSecteur");
        String urlGareGet = getString(R.string.dns) + getString(R.string.url_gare_one_query_id);
        serviceSecteur.enquiry(urlGareGet + gareID.toString());
    }

    @Override
    public void serviceSuccess(Object object, int id_srv) {
        if(id_srv == 2){
            secteursList.clear();
            if(object != null){
                secteursList.addAll((List<Secteur>) object);
                //showMessage("Show Secteur List New", secteursList.toString());
                //callServicePlanFromSecteur(secteursList.get(0).getId());
                //imgView.setImageBitmap(StringToBitMap(secteursList.get(0).getImage()));
                secteurAdapter.notifyDataSetChanged();


            }
            else
                Toast.makeText(this,"La liste des secteurs est vide.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
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
