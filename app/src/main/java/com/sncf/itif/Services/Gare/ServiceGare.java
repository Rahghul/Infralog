package com.sncf.itif.Services.Gare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;


import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.Services.WebServiceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.sncf.itif.R;

import dmax.dialog.SpotsDialog;

/**
 * Created by Rahghul on 21/03/2016.
 */
public class ServiceGare {
    private AlertDialog dialog;
    private ServiceCallBack callBack;
    private Exception error;
    private String service;
    private int result;

    public ServiceGare(ServiceCallBack callBack, Context context, String service) {
        this.callBack = callBack;
        this.service = service;
        this.dialog = new SpotsDialog(context, R.style.Custom);
    }

    public void enquiry(final String endpoint){
        new AsyncTask<String, Void, List<Gare>>() {

            @Override
            protected void onPreExecute() {
                dialog.show();
            }


            @Override
            protected List<Gare> doInBackground(String... params) {
                try{
                    switch (service){
                        case "getAll" :
                            return getAllGares(params[0]);

                        default:
                            throw (new Exception("Unknown Service : Gare"));
                    }
                }catch (Exception e){
                    error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Gare> gares) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    if (error != null) {
                        callBack.serviceFailure(error);
                        return;
                    }
                    callBack.serviceSuccess(gares, 1);
                }
                catch (Exception e) {
                    error = e;
                }
            }
        }.execute(endpoint);
    }


    public List<Gare> getAllGares(String endpoint) throws JSONException {
        String s = WebServiceUtil.requestWebServiceGET(endpoint);
        //Log.d("------------",s);

        if(s == null){
            return null;
        }
        List<Gare> foundGares = new ArrayList<>();

        JSONArray data = new JSONArray(s);
        for(int i =0; i<data.length(); i++){
            JSONObject p = data.getJSONObject(i);

            foundGares.add(
                    new Gare(p.getLong("id"), p.getString("name")));
        }
        return foundGares;
    }


}
