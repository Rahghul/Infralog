package com.sncf.itif.Services.DetailPlan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.sncf.itif.Services.Plan.Plan;
import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class DetailPlanActivity extends ActionBarActivity implements ServiceCallBack {


    Plan plan = null;
    ServiceDetailPlan serviceDetailPlan = null;
    ProgressDialog dialog;
    ImageView imgPlan;
    PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
       //         WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.details_activity);

        dialog = new ProgressDialog(this);
        imgPlan = (ImageView) findViewById(R.id.image_detail);
        mAttacher = new PhotoViewAttacher(imgPlan);
        //mAttacher.update();

        //PlanIDF planIDF = getIntent().getExtras().getParcelable("com.sncf.myapplication2.Services.PlanIDF");
        //Plan plan = getIntent().getExtras().getParcelable("com.sncf.myapplication2.Services.Plan");
        //byte[] bytes = getIntent().getByteArrayExtra("BMP");
        //Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        //Toast.makeText(this, planIDF.toString(), Toast.LENGTH_LONG).show();
        /*String image = null;
        if(planIDF != null)
            image = planIDF.getPlan();
        else
            image= plan.getPlan();
        */

                /*String version = getIntent().getStringExtra("version");
        String image = getIntent().getStringExtra("image");
*/
        //TextView imageTitleRef = (TextView) findViewById(R.id.txt_reference);
        //TextView imageTitleVers = (TextView) findViewById(R.id.txt_version);

        // imageTitleRef.setText(reference);
        // imageTitleVers.setText(version);

        Long id = getIntent().getLongExtra("id", -1);
        if(id!=-1){
            callServicePlanFromSecteur(id);
        }
        else{
            Toast.makeText(this,"Erreur d'affichage, à réessayer.", Toast.LENGTH_LONG).show();
        }
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
        serviceDetailPlan = new ServiceDetailPlan(this, dialog, "getPlanById");
        String url_carte_by_id = getString(R.string.dns) + getString(R.string.url_plan_detail_by_id);
        serviceDetailPlan.enquiry(url_carte_by_id +planID);
    }
}