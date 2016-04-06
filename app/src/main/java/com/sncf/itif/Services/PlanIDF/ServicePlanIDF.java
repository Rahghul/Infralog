package com.sncf.itif.Services.PlanIDF;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.Services.WebServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rahghul on 23/03/2016.
 */
public class ServicePlanIDF {

    private ProgressDialog dialog;
    private ServiceCallBack callBack;
    private Exception error;
    private String service;
    private int result;

    public ServicePlanIDF(ServiceCallBack callBack, ProgressDialog dialog, String service) {
        this.dialog = dialog;
        this.callBack = callBack;
        this.service = service;
    }

    public void enquiry(final String endpoint){
        new AsyncTask<String, Void, DiversImage>() {

            @Override
            protected void onPreExecute() {
                // TODO i18n
                //   dialog = new ProgressDialog(c);
                dialog.setMessage("Chargement...");
                dialog.show();
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
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
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
