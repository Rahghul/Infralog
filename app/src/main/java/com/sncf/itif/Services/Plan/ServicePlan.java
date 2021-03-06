package com.sncf.itif.Services.Plan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.sncf.itif.R;
import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.Services.WebServiceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by Rahghul on 23/03/2016.
 */
public class ServicePlan {

    private AlertDialog dialog;
    private ServiceCallBack callBack;
    private Exception error;
    private String service;
    private int result;

    public ServicePlan(ServiceCallBack callBack, Context context, String service) {
        this.dialog = new SpotsDialog(context, R.style.Custom);
        this.callBack = callBack;
        this.service = service;
    }

    public void enquiry(final String endpoint){
        new AsyncTask<String, Void, List<Plan>>() {

            @Override
            protected void onPreExecute() {
                // TODO i18n
                //   dialog = new ProgressDialog(c);
             //   dialog.setMessage("Chargement...");
                dialog.show();
            }


            @Override
            protected List<Plan> doInBackground(String... params) {
                try{
                    switch (service){
                        case "getPlanFromSecteur" :
                            return getPlanFromSecteur(params[0]);

                        default:
                            throw (new Exception("Unknown Service : Plan"));
                    }
                }catch (Exception e){
                    error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Plan> plans) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    if (error != null) {
                        callBack.serviceFailure(error);
                        return;
                    }
                    callBack.serviceSuccess(plans, 3);
                }
                catch (Exception e) {
                    error = e;
                }
            }

            public List<Plan> getPlanFromSecteur(String endpoint) throws JSONException {
                String s = WebServiceUtil.requestWebServiceGET(endpoint);
                if(s == null){
                    return null;
                }
                List<Plan> foundPlans = new ArrayList<Plan>();

                JSONArray data = new JSONArray(s);
                for(int i =0; i<data.length(); i++){
                    JSONObject p = data.getJSONObject(i);

                    foundPlans.add(
                            // new Plan(p.getLong("id"), p.getString("version"), p.getString("ref"), p.getString("plan")));
                            new Plan(p.getLong("id"), p.getString("version"), p.getString("ref"), p.getString("plan")));

                }
                return foundPlans;
            }
        }.execute(endpoint);
    }



}
