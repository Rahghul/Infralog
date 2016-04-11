package com.sncf.itif.Services.PlanIDF;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sncf.itif.R;
import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.Services.WebServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

/**
 * Created by Rahghul on 23/03/2016.
 */
public class ServicePlanIDF {

   // private AlertDialog dialog;
    private ServiceCallBack callBack;
    private Exception error;
    private String service;
    private int result;

    public ServicePlanIDF(ServiceCallBack callBack, Context context, String service) {
        this.callBack = callBack;
        this.service = service;
        //this.dialog = new SpotsDialog(context, R.style.Custom);
    }

    public void enquiry(final String endpoint){
        new AsyncTask<String, Void, DiversImage>() {

            @Override
            protected void onPreExecute() {
                //dialog.show();
            }


            @Override
            protected DiversImage doInBackground(String... params) {
                try{
                    switch (service){
                        case "getPlanIDF" :
                            return getPlanFromSecteur(params[0]);

                        default:
                            throw (new Exception("Unknown Service : DiversImage"));
                    }
                }catch (Exception e){
                    error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(DiversImage plan) {
//                if (dialog.isShowing()) {
//                    dialog.dismiss();
//                }
                try {
                    if (error != null) {
                        callBack.serviceFailure(error);
                        return;
                    }
                    callBack.serviceSuccess(plan, 4);
                }
                catch (Exception e) {
                    error = e;
                }
            }

            public DiversImage getPlanFromSecteur(String endpoint) throws JSONException {
                String s = WebServiceUtil.requestWebServiceGET(endpoint);
                if(s == null){
                    return null;
                }
                DiversImage planIDF;

                JSONObject p = new JSONObject(s);
                planIDF = new DiversImage(p.getString("name"), p.getString("plan"));
                return planIDF;

            }
        }.execute(endpoint);
    }



}
