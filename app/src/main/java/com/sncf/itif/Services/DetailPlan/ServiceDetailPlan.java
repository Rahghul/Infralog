package com.sncf.itif.Services.DetailPlan;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.sncf.itif.Services.Plan.Plan;
import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.Services.WebServiceUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rahghul on 04/04/2016.
 */
public class ServiceDetailPlan {

    private ProgressDialog dialog;
    private ServiceCallBack callBack;
    private Exception error;
    private String service;
    private int result;

    public ServiceDetailPlan(ServiceCallBack callBack, ProgressDialog dialog, String service) {
        this.dialog = dialog;
        this.callBack = callBack;
        this.service = service;
    }

    public void enquiry(final String endpoint){
        new AsyncTask<String, Void, Plan>() {

            @Override
            protected void onPreExecute() {
                // TODO i18n
                //   dialog = new ProgressDialog(c);
                dialog.setMessage("Chargement...");
                dialog.show();
            }


            @Override
            protected Plan doInBackground(String... params) {
                try{
                    switch (service){
                        case "getPlanById" :
                            return getPlanById(params[0]);

                        default:
                            throw (new Exception("Unknown Service : Detail Plan"));
                    }
                }catch (Exception e){
                    error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Plan plan) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    if (error != null) {
                        callBack.serviceFailure(error);
                        return;
                    }
                    callBack.serviceSuccess(plan, 1);
                }
                catch (Exception e) {
                    error = e;
                }
            }


            public Plan getPlanById(String endpoint) throws JSONException {
                String s = WebServiceUtil.requestWebServiceGET(endpoint);
                if(s == null){
                    return null;
                }
                Plan foundPlan;
                JSONObject p = new JSONObject(s);
                foundPlan = new Plan(p.getLong("id"), p.getString("version"), p.getString("ref"), p.getString("plan"));

                return foundPlan;
            }
        }.execute(endpoint);
    }
}
