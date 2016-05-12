package com.sncf.itif.Services.Plan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.sncf.itif.Services.PlanViewer.ActPlanViewer;
import com.sncf.itif.Global.Network.NetworkMonitor;
import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ActPlan extends AppCompatActivity implements ServiceCallBack {

    ServicePlan servicePlan;
    List<Plan> planList = new ArrayList<Plan>();


    Context mContext;

    GridView gridView;
    PlanGridViewAdapter gridAdapter;

    Long secteurId;
    String gareName;

    TextView display_gareName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        // Désactive le mode capture d'écran
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.act_plan);

        getSupportActionBar().setTitle(getResources().getString(R.string.act_plan_tv_title));
        getSupportActionBar().setSubtitle(R.string.global_tv_short_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        secteurId = intent.getLongExtra("SelectedSecteurId", -1);
        gareName = intent.getStringExtra("SelectedGareName");

        display_gareName = (TextView) findViewById(R.id.tvGareName);
        display_gareName.setText(gareName);

        gridView = (GridView) findViewById(R.id.gridView);

    }

    @Override
    public void onResume() {
        if (NetworkMonitor.isNetworkAvailable(this) == false) {
            NetworkMonitor.showNetworkAlert(this);
        } else {
            callServicePlanFromSecteur(secteurId);
        }

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_plan, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_input) {

//            Toast.makeText(mContext, "Appuyez longuement sur le plan pour enregistrer. Il sera accessible pour une période de 2 semaines uniquement dans l'interface 'PLAN IDF'->'Accès au plan enregistré'. Au-delà, il faudra renouveler l'action.", Toast.LENGTH_LONG).show();

            android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext);
            alertDialog.setTitle("Téléchargement");
            alertDialog.setMessage("Appuyez longuement sur le plan pour enregistrer dans l'interface 'PLAN IDF'->'Accès au plan enregistré'. Au-delà de 2 semaines, il faudra renouveler l'action.");
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

        //return super.onOptionsItemSelected(item);
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
        String urlPlanGetFromSecteur = getString(R.string.global_server_endpoint) + getString(R.string.global_server_url_secteur);

        servicePlan.enquiry(urlPlanGetFromSecteur + secteurID.toString() + "/carte");
    }

    @Override
    public void serviceSuccess(Object object, int id_srv) {
        if (id_srv == 3) {
            planList.clear();
            if (object != null) {
                planList.addAll((List<Plan>) object);
                //showMessage("Plan List", planList.toString());
                gridAdapter = new PlanGridViewAdapter(this, R.layout.act_plan_grid_view_item, planList);
                gridView.setAdapter(gridAdapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                        Plan itemPlan = (Plan) parent.getItemAtPosition(position);

                        //Create intent
                        Intent intent = new Intent(mContext, ActPlanViewer.class);
                        intent.putExtra("id", itemPlan.getId());
                        startActivity(intent);
                        /*intent.putExtra("version", item.getVersion());
                        intent.putExtra("image", item.getImage());*/
                        //intent.putExtra("com.sncf.myapplication2.Services.Plan", item);
                    }


                });

                gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Plan itemPlan = (Plan) parent.getItemAtPosition(position);
                        //Save image
                        if (saveImageToInternalStorage(itemPlan) == false) {
                            Toast.makeText(mContext, "Le plan n'a pas été sauvegardé proprement. A réessayer.", Toast.LENGTH_LONG).show();
                            return false;
                        }
                        return true;
                    }
                });


            } else
                Toast.makeText(this, "La liste des secteurs est vide.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, "Error exception service failure ActPlan " + exception.getMessage(), Toast.LENGTH_LONG).show();
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


    public boolean saveImageToInternalStorage(Plan plan) {

        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream

            String imageName = "IMG%" + plan.getId() + "%" + gareName + "%" + plan.getReference() + "%" + plan.getVersion();
            //FileOutputStream fos = new FileOutputStream(this.getFilesDir().getAbsolutePath()+"/"+imageName);
            //Toast.makeText(mContext, imageName, Toast.LENGTH_LONG).show();
            if (fileExistance(imageName)) {
                Toast.makeText(mContext, "Le plan existe déjà dans l'interface 'PLAN IDF'->'Accès au plan enregistré'", Toast.LENGTH_LONG).show();
            } else {
                FileOutputStream fos = this.openFileOutput(imageName, Context.MODE_PRIVATE);

                // Writing the bitmap to the output stream
                Bitmap image = StringToBitMap(plan.getPlan());
                image.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                Toast.makeText(mContext, "Le plan enregistré sera accessible à l'interface 'PLAN IDF'->'Accès au plan enregistré' pendant 2 semaines.", Toast.LENGTH_LONG).show();
            }
            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }

    public boolean fileExistance(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

}
