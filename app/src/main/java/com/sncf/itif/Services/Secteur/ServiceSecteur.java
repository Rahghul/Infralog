package com.sncf.itif.Services.Secteur;

import android.app.ProgressDialog;
import android.os.AsyncTask;


import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.Services.WebServiceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahghul on 22/03/2016.
 */
public class ServiceSecteur {

    private ProgressDialog dialog;
    private ServiceCallBack callBack;
    private Exception error;
    private String service;
    private int result;

    public ServiceSecteur(ServiceCallBack callBack, ProgressDialog dialog, String service) {
        this.dialog = dialog;
        this.callBack = callBack;
        this.service = service;
    }

    public void enquiry(final String endpoint) {
        new AsyncTask<String, Void, List<Secteur>>() {

            @Override
            protected void onPreExecute() {
                // TODO i18n
                //   dialog = new ProgressDialog(c);
                dialog.setMessage("Chargement...");
                dialog.show();
            }

            @Override
            protected List<Secteur> doInBackground(String... params) {
                try {
                    switch (service) {
                        case "getSecteur":
                            return getSecteurFromGare(params[0]);

                        default:
                            throw (new Exception("Unknown Service : Gare"));
                    }
                } catch (Exception e) {
                    error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Secteur> secteurs) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    if (error != null) {
                        callBack.serviceFailure(error);
                        return;
                    }
                    callBack.serviceSuccess(secteurs, 2);
                } catch (Exception e) {
                    error = e;
                }
            }

            public List<Secteur> getSecteurFromGare(String endpoint) throws JSONException {
                String s = WebServiceUtil.requestWebServiceGET(endpoint);
                if(s == null){
                    return null;
                }
                List<Secteur> foundSecteurs = new ArrayList<>();

                JSONArray data = new JSONArray(s);
                for(int i =0; i<data.length(); i++){
                    JSONObject p = data.getJSONObject(i);

                    foundSecteurs.add(
                            new Secteur(p.getLong("id"), p.getString("name"))//, p.getString("ref"), p.getString("version"), p.getString("plan"))
                    );
                }
                return foundSecteurs;

            }
        }.execute(endpoint);
    }


}
