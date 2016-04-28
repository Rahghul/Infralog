package com.sncf.itif.Services.PlanIDF;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sncf.itif.Services.PlanIDF.Telechargement.ActTelechargement;
import com.sncf.itif.R;

import java.util.ArrayList;
import java.util.List;

public class ActTabPlanIDF extends Fragment /*implements ServiceCallBack*/ {

   // ImageView image_carte;
   // ServicePlanIDF servicePlanIDF;

  //  DiversImage planIDFReceived;
  //  PhotoViewAttacher mAttacher;

   // Boolean isNetworkFail = false;

    ListView planIDFListView;
    List<String> planIDFList = new ArrayList<String>();
    ArrayAdapter planIDFAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_tab_plan_idf, container, false);
      //  image_carte = (ImageView) view.findViewById(R.id.image_idf);

        planIDFList.add("Accès au plan IDF de RATP");
        planIDFList.add("Accès au plan IDF de Carto*");
        planIDFList.add("Accès au plan enregistré");
        planIDFListView = (ListView) view.findViewById(R.id.planIDFListView);
        planIDFAdapter = new ArrayAdapter(getContext(), R.layout.act_tab_plan_idf_list_view_item, planIDFList);
        planIDFListView.setAdapter(planIDFAdapter);

        planIDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Click on ratp plan
                if (position == 0){
                    Intent intent = new Intent(getContext(), ActRatpPlan.class);
                    startActivity(intent);
                    //callServicePlanIDF();
                    //mAttacher = new PhotoViewAttacher(image_carte);
                }

                //Click on Carto
                if (position == 1) {

                    Intent intent = new Intent(getContext(), ActCartoMetro.class);
                    startActivity(intent);
                }

                //Click on plan enregistré
                if (position == 2) {

                    Intent intent = new Intent(getContext(), ActTelechargement.class);
                    startActivity(intent);
                }
            }
        });




       /* if (NetworkMonitor.isNetworkAvailable(getContext()) == true) {
            callServicePlanIDF();
        }
        mAttacher = new PhotoViewAttacher(image_carte);
*/

        return view;
    }

    @Override
    public void onResume() {
        //We play with variable isNetworkFail to allow display the IDF card once if network failed
        /*if (NetworkMonitor.isNetworkAvailable(getContext()) == false) {
            isNetworkFail = true;
        } else {
            if(isNetworkFail) {
                callServicePlanIDF();
                isNetworkFail = false;
                mAttacher.update();
            }
        }*/
        //Hide Soft Keyboard Android if it s available
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

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