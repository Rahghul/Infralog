package com.sncf.itif.Services.Secteur;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sncf.itif.Global.Network.NetworkMonitor;
import com.sncf.itif.Main.MainActivity;
import com.sncf.itif.Services.Gare.ActTabGare;
import com.sncf.itif.Services.Plan.ActPlan;
import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahghul on 23/03/2016.
 */
public class ActSecteur extends AppCompatActivity implements ServiceCallBack {

    ServiceSecteur serviceSecteur;
    List<Secteur> secteursList = new ArrayList<Secteur>();
    ListView secteursListView;
    SecteurListViewAdapter secteurAdapter;

    /*variable qui assure l'affichage AlertDialog Box internet settings une fois.
     Problème rencontré : au démarrage la méthode onCreate and onResume exécuté une après l'autre
     donc l'alert dialog box internet affiche deux fois.*/
    // Boolean isDisplay = false;

    Context mContext;
    Long gareId;
    String gareName;

    TextView display_gareName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_secteur);

        getSupportActionBar().setTitle(R.string.act_secteur_tv_title);
        getSupportActionBar().setSubtitle(R.string.main_act_tv_full_title);


        mContext = this;
        secteursListView = (ListView) findViewById(R.id.secteurListView);
        secteurAdapter = new SecteurListViewAdapter(this, secteursList);
        secteursListView.setAdapter(secteurAdapter);

        Intent intent = getIntent();
        gareId = intent.getLongExtra("SelectedGareId", -1);
        gareName = intent.getStringExtra("SelectedGareName");

        display_gareName = (TextView) findViewById(R.id.tvGareName);
        display_gareName.setText(gareName);

        // ArrayList<Secteur> secteurList =  (ArrayList<Secteur>)intent.getSerializableExtra("SecteurList");
        //showMessage("Gare ID:", gareId.toString());

//        if (isNetworkAvailable() == false) {
//            showNetworkAlert(this);
//            isDisplay = true;
//        } else {
//            isDisplay = false;
//            callServiceSecteurFromGare(gareId);
//        }
    }

    @Override
    protected void onStart() {
        secteursListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = secteursListView.getItemAtPosition(position);
                //showMessage("Item Clicked:", ((Secteur)o).toString());
                //start display Secteur Activity
                Intent intent = new Intent(mContext, ActPlan.class);
                intent.putExtra("SelectedSecteurId", ((Secteur) o).getId());
                intent.putExtra("SelectedGareName", gareName);

                startActivityForResult(intent, 1);
            }
        });
        super.onStart();
    }

    @Override
    public void onResume() {
        if (NetworkMonitor.isNetworkAvailable(this) == false) {
            //  if (!isDisplay) {
            NetworkMonitor.showNetworkAlert(this);
            //      isDisplay = true;
            //  }
        } else {
            //  isDisplay = false;
            callServiceSecteurFromGare(gareId);
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

    public void callServiceSecteurFromGare(Long gareID) {
        serviceSecteur = new ServiceSecteur(this, this, "getSecteur");
        String urlGareGet = getString(R.string.global_server_endpoint) + getString(R.string.global_server_url_gare_one_query_id);
        serviceSecteur.enquiry(urlGareGet + gareID.toString());
    }

    @Override
    public void serviceSuccess(Object object, int id_srv) {
        if (id_srv == 2) {
            secteursList.clear();
            if (object != null) {
                secteursList.addAll((List<Secteur>) object);
                //showMessage("Show Secteur List New", secteursList.toString());
                //callServicePlanFromSecteur(secteursList.get(0).getId());
                //imgView.setImageBitmap(StringToBitMap(secteursList.get(0).getImage()));
                secteurAdapter.notifyDataSetChanged();


            } else
                Toast.makeText(this, "La liste des secteurs est vide.", Toast.LENGTH_LONG).show();

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
                //super.onBackPressed();
                startActivity(new Intent(ActSecteur.this, MainActivity.class));
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(ActSecteur.this, MainActivity.class));
        finish();

    }
}
