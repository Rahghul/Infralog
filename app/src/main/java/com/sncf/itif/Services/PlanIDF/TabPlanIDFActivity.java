package com.sncf.itif.Services.PlanIDF;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    Boolean isNetworkFail = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_plan_idf, container, false);

        image_carte = (ImageView) view.findViewById(R.id.image_idf);

        dialog = new ProgressDialog(getActivity());

        if (isNetworkAvailable() == true) {
            callServicePlanIDF();
        }
        mAttacher = new PhotoViewAttacher(image_carte);


        return view;
    }

    @Override
    public void onResume() {
        //We play with variable isNetworkFail to allow display the IDF card once if network failed
        if (isNetworkAvailable() == false) {
            isNetworkFail = true;
        } else {
            if(isNetworkFail) {
                callServicePlanIDF();
                isNetworkFail = false;
            }
        }
        super.onResume();
    }




    public void callServicePlanIDF() {
        servicePlanIDF = new ServicePlanIDF(this, getContext(), "getPlanIDF");
        servicePlanIDF.enquiry(getActivity().getResources().getString(R.string.dns)
                + getActivity().getResources().getString(R.string.url_divers_img_IDF));
    }

    @Override
    public void serviceSuccess(Object object, int id_srv) {
        if (id_srv == 4) {
            if (object != null) {
                planIDFReceived = (DiversImage) object;
                //Log.d(planIDFReceived.getName(), planIDFReceived.getImage());
                image_carte.setImageBitmap(StringToBitMap(planIDFReceived.getImage()));
            } else
                Toast.makeText(getContext(), "La carte IDF indisponible momentanément.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(getContext(), "Error exception service failure TabPlanIDF " + exception.getMessage(), Toast.LENGTH_LONG).show();
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

    //vérifie la disponibilité de l'accès à l'internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}