package com.sncf.itif.Services.PlanIDF;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.sncf.itif.Services.Network.NetworkOpt;
import com.sncf.itif.Services.SavedImages.SavedImageActivity;
import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.R;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class TabPlanIDFActivity extends Fragment /*implements ServiceCallBack*/ {

   // ImageView image_carte;
   // ServicePlanIDF servicePlanIDF;

  //  DiversImage planIDFReceived;
  //  PhotoViewAttacher mAttacher;

   // Boolean isNetworkFail = false;

    ListView planIDFListView;
    List<String> planIDFList = new ArrayList<>();
    ArrayAdapter planIDFAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_plan_idf, container, false);
      //  image_carte = (ImageView) view.findViewById(R.id.image_idf);

        planIDFList.add("Accès au plan IDF de RATP");
        planIDFList.add("Accès au plan IDF de Carto*");
        planIDFList.add("Accès au plan enregistré");
        planIDFListView = (ListView) view.findViewById(R.id.planIDFListView);
        planIDFAdapter = new ArrayAdapter(getContext(), R.layout.one_item_list_plan_idf, planIDFList);
        planIDFListView.setAdapter(planIDFAdapter);

        planIDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Click on ratp plan
                if (position == 0){
                    Intent intent = new Intent(getContext(), RATPplanIDFActivity.class);
                    startActivity(intent);
                    //callServicePlanIDF();
                    //mAttacher = new PhotoViewAttacher(image_carte);
                }

                //Click on Carto
                if (position == 1) {

                    Intent intent = new Intent(getContext(), WebViewCartoActivity.class);
                    startActivity(intent);
                }

                //Click on plan enregistré
                if (position == 2) {

                    Intent intent = new Intent(getContext(), SavedImageActivity.class);
                    startActivity(intent);
                }
            }
        });




       /* if (NetworkOpt.isNetworkAvailable(getContext()) == true) {
            callServicePlanIDF();
        }
        mAttacher = new PhotoViewAttacher(image_carte);
*/

        return view;
    }

    @Override
    public void onResume() {
        //We play with variable isNetworkFail to allow display the IDF card once if network failed
        /*if (NetworkOpt.isNetworkAvailable(getContext()) == false) {
            isNetworkFail = true;
        } else {
            if(isNetworkFail) {
                callServicePlanIDF();
                isNetworkFail = false;
                mAttacher.update();
            }
        }*/
        super.onResume();
    }




 /*   public void callServicePlanIDF() {
        servicePlanIDF = new ServicePlanIDF(this, getContext(), "getPlanIDF");
        servicePlanIDF.enquiry(getActivity().getResources().getString(R.string.dns)
                + getActivity().getResources().getString(R.string.url_divers_img_IDF));
    }

    @Override
    public void serviceSuccess(Object object, int id_srv) {
        if (id_srv == 4) {
            if (object != null) {
                planIDFReceived = (DiversImage) object;

                Bitmap bm = StringToBitMap(planIDFReceived.getImage());
                image_carte.setImageBitmap(bm);

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
    }*/




}