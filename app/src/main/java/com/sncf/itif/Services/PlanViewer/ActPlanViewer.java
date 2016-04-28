package com.sncf.itif.Services.PlanViewer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.sncf.itif.Global.Network.NetworkMonitor;
import com.sncf.itif.Services.Plan.Plan;
import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.R;

import java.io.File;
import java.io.FileInputStream;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ActPlanViewer extends Activity implements ServiceCallBack {


    Plan plan = null;
    ServiceDetailPlan serviceDetailPlan = null;
    ImageView imgPlan;
    PhotoViewAttacher mAttacher;

    /*variable qui assure l'affichage AlertDialog Box internet settings une fois.
    Problème rencontré : au démarrage la méthode onCreate and onResume exécuté une après l'autre
    donc l'alert dialog box internet affiche deux fois.*/
    //Boolean isDisplay = false;

    Long img_id;
    String savedImageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Désactive le mode capture d'écran
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.act_plan_viewer);

        imgPlan = (ImageView) findViewById(R.id.image_small);

        img_id = getIntent().getLongExtra("id", -1);
        savedImageTitle = getIntent().getStringExtra("SavedImageTitle");
        mAttacher = new PhotoViewAttacher(imgPlan);

        if (savedImageTitle != null)
            imgPlan.setImageBitmap(getPlanFromInternalStorage(savedImageTitle));

        mAttacher.update();

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

//        if (img_id == -1) {
//            Toast.makeText(this, "Erreur d'affichage, à réessayer.", Toast.LENGTH_LONG).show();
//
////            if (isNetworkAvailable() == false) {
////                showNetworkAlert(this);
////                isDisplay = true;
////            } else {
////                isDisplay = false;
////                callServicePlanFromSecteur(img_id);
////            }
//        }

    }

    @Override
    public void onResume() {
        if (NetworkMonitor.isNetworkAvailable(this) == false) {
            // if (!isDisplay) {
            NetworkMonitor.showNetworkAlert(this);
            //     isDisplay = true;
            // }
        } else {
            //  isDisplay = false;
            if (img_id != -1)
                callServicePlanFromSecteur(img_id);
            else if (savedImageTitle == null)
                Toast.makeText(this, "Erreur d'affichage, à réessayer.", Toast.LENGTH_LONG).show();
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

    @Override
    public void serviceSuccess(Object object, int id_srv) {

        if (id_srv == 1) {
            plan = (Plan) object;
            imgPlan.setImageBitmap(StringToBitMap(plan.getPlan()));
            mAttacher.update();
        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, "Error exception service failure ActPlanViewer " + exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void callServicePlanFromSecteur(Long planID) {
        serviceDetailPlan = new ServiceDetailPlan(this, this, "getPlanById");
        String url_carte_by_id = getString(R.string.global_server_endpoint) + getString(R.string.global_server_url_plan_detail_by_id);
        serviceDetailPlan.enquiry(url_carte_by_id + planID);
    }

    //Get Image from Internal Storage
    public Bitmap getPlanFromInternalStorage(String filename) {

        Bitmap thumbnail = null;

        try {
            File filePath = this.getFileStreamPath(filename);

            FileInputStream fi = new FileInputStream(filePath);
            thumbnail = BitmapFactory.decodeStream(fi);


        } catch (Exception ex) {
            Log.e("Internal Storage", ex.getMessage());
        }
        return thumbnail;
    }
}