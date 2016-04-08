package com.sncf.itif.Services.PlanIDF;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.R;

import uk.co.senab.photoview.PhotoViewAttacher;

public class TabPlanIDFActivity extends Fragment implements ServiceCallBack {

    ImageView image_carte;
    ServicePlanIDF servicePlanIDF;
    ProgressDialog dialog;

    DiversImage planIDFReceived;
    PhotoViewAttacher mAttacher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_plan_idf, container, false);

        image_carte = (ImageView) view.findViewById(R.id.image_idf);

        dialog = new ProgressDialog(getActivity());
        callServicePlanIDF();
        mAttacher = new PhotoViewAttacher(image_carte);


        return view;
    }

    @Override
    public void onResume() {
        //callServicePlanIDF();
        super.onResume();
    }


    public void callServicePlanIDF() {
        servicePlanIDF = new ServicePlanIDF(this, dialog, "getPlanIDF");
        servicePlanIDF.enquiry(getActivity().getResources().getString(R.string.dns)
                + getActivity().getResources().getString(R.string.url_divers_img_IDF));
    }

    @Override
    public void serviceSuccess(Object object, int id_srv) {
        if (id_srv == 4) {
            if (object != null) {
                planIDFReceived = (DiversImage)object;
                //Log.d(planIDFReceived.getName(), planIDFReceived.getImage());
                image_carte.setImageBitmap(StringToBitMap(planIDFReceived.getImage()));
            } else
                Toast.makeText(getContext(), "La carte IDF indisponible momentanément.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(getContext(),"Error exception service failure TabPlanIDF "+exception.getMessage(), Toast.LENGTH_LONG).show();
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

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}