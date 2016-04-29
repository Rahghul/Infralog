package com.sncf.itif.Services.PlanIDF;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.sncf.itif.Services.ServiceCallBack;

import uk.co.senab.photoview.PhotoViewAttacher;
import com.sncf.itif.R;

/**
 * Created by Rahghul on 14/04/2016.
 */
public class ActRatpPlan extends Activity implements ServiceCallBack {

    ServicePlanIDF servicePlanIDF;

    ImageView image_carte;
    PhotoViewAttacher mAttacher;

    DiversImage planIDFReceived;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ratp_plan);

        image_carte = (ImageView) findViewById(R.id.image_ratp_idf);
        mAttacher = new PhotoViewAttacher(image_carte);

        callServicePlanIDF();
    }

    public void callServicePlanIDF() {
        servicePlanIDF = new ServicePlanIDF(this, this, "getPlanIDF");
        servicePlanIDF.enquiry(getResources().getString(R.string.global_server_endpoint)
                + getResources().getString(R.string.global_server_url_divers_img_IDF));
    }

    @Override
    public void serviceSuccess(Object object, int id_srv) {
        if (id_srv == 4) {
            if (object != null) {
                planIDFReceived = (DiversImage) object;

                Bitmap bm = StringToBitMap(planIDFReceived.getImage());
                image_carte.setImageBitmap(bm);
                mAttacher.update();

            } else
                Toast.makeText(this, "La carte IDF indisponible momentanément.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, "Error exception service failure TabPlanIDF " + exception.getMessage(), Toast.LENGTH_LONG).show();
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
}