package com.sncf.itif.Services.DetailPlan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.sncf.itif.Services.Plan.Plan;
import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class DetailPlanActivity extends ActionBarActivity implements ServiceCallBack {


    Plan plan = null;
    ServiceDetailPlan serviceDetailPlan = null;
    ImageView imgPlan;
    PhotoViewAttacher mAttacher;

    /*variable qui assure l'affichage AlertDialog Box internet settings une fois.
    Problème rencontré : au démarrage la méthode onCreate and onResume exécuté une après l'autre
    donc l'alert dialog box internet affiche deux fois.*/
    Boolean isDisplay = false;

    Long img_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.details_activity);

        imgPlan = (ImageView) findViewById(R.id.image_detail);
        mAttacher = new PhotoViewAttacher(imgPlan);
        //mAttacher.update();

        //DiversImage planIDF = getIntent().getExtras().getParcelable("com.sncf.myapplication2.Services.DiversImage");
        //Plan plan = getIntent().getExtras().getParcelable("com.sncf.myapplication2.Services.Plan");
        //byte[] bytes = getIntent().getByteArrayExtra("BMP");
        //Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        //Toast.makeText(this, planIDF.toString(), Toast.LENGTH_LONG).show();
        /*String image = null;
        if(planIDF != null)
            image = planIDF.getImage();
        else
            image= plan.getImage();
        */

                /*String version = getIntent().getStringExtra("version");
        String image = getIntent().getStringExtra("image");
*/
        //TextView imageTitleRef = (TextView) findViewById(R.id.txt_reference);
        //TextView imageTitleVers = (TextView) findViewById(R.id.txt_version);

        // imageTitleRef.setText(reference);
        // imageTitleVers.setText(version);

        img_id = getIntent().getLongExtra("id", -1);
        if(img_id!=-1){
            if (isNetworkAvailable() == false) {
                showNetworkAlert(this);
                isDisplay = true;
            } else {
                isDisplay = false;
                callServicePlanFromSecteur(img_id);
            }
        }
        else{
            Toast.makeText(this,"Erreur d'affichage, à réessayer.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        if (isNetworkAvailable() == false) {
            if (!isDisplay) {
                showNetworkAlert(this);
                isDisplay = true;
            }
        } else {
            isDisplay = false;
            callServicePlanFromSecteur(img_id);
        }

        super.onResume();
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

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public void serviceSuccess(Object object, int id_srv) {

        if(id_srv == 1){
            plan = (Plan)object;
            imgPlan.setImageBitmap(StringToBitMap(plan.getPlan()));
            mAttacher.update();
        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this,"Error exception service failure DetailPlanActivity "+exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void callServicePlanFromSecteur(Long planID){
        serviceDetailPlan = new ServiceDetailPlan(this, this, "getPlanById");
        String url_carte_by_id = getString(R.string.dns) + getString(R.string.url_plan_detail_by_id);
        serviceDetailPlan.enquiry(url_carte_by_id +planID);
    }

    //vérifie la disponibilité de l'accès à l'internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    //Affichage de l'AlertBox Internet Settings
    public void showNetworkAlert(final Context mContext) {
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        customBuilder.setTitle("Paramètre Internet :");
        customBuilder.setIcon(R.drawable.ic_warning_violet_18dp);

        // Setting Dialog Message
        customBuilder.setMessage("Vous n'avez pas accès à l'Internet. Merci de vérifier votre connexion.");

        // On pressing Settings button
        customBuilder.setPositiveButton("Paramètres", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        customBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = customBuilder.create();
        dialog.show();

        Button btn_negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        btn_negative.setTextColor(getResources().getColor(R.color.color3));

        Button btn_positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btn_positive.setTextColor(getResources().getColor(R.color.color3));
    }
}