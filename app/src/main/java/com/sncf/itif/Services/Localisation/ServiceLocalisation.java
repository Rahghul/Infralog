package com.sncf.itif.Services.Localisation;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.Services.WebServiceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rahghul on 29/03/2016.
 */
public class ServiceLocalisation {

    final static String TAG = "---->Localisation";
    private ProgressDialog dialog;
    private ServiceCallBack callBack;
    private Exception error;
    private String service;
    private int result;

    public ServiceLocalisation(ServiceCallBack callBack, ProgressDialog dialog, String service) {
        this.dialog = dialog;
        this.callBack = callBack;
        this.service = service;
    }

    public void enquiry(final String endpoint) {
        new AsyncTask<String, Void, String>() {

            @Override
            protected void onPreExecute() {
                // TODO i18n
                //   dialog = new ProgressDialog(c);
                dialog.setMessage("Please wait..");
                dialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    switch (service) {
                        case "getNearestStation":
                            return getNearestGare(params[0]);

                        default:
                            throw (new Exception("Unknown Service : Gare"));
                    }
                } catch (Exception e) {
                    error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    if (error != null) {
                        callBack.serviceFailure(error);
                        return;
                    }
                    callBack.serviceSuccess(s, 2);
                } catch (Exception e) {
                    error = e;
                }
            }

            public String getNearestGare(String endpoint) throws JSONException {
                String s = WebServiceUtil.requestWebServiceGETWithAuthBasic(endpoint);
                //Log.d("------------",s);
                if(s==null){
                    return null;
                }
                JSONObject reader = new JSONObject(s);
                JSONArray stop_points = reader.getJSONArray("stop_points");


                for (int i = 0; i < stop_points.length(); i++) {
                    JSONObject p = stop_points.getJSONObject(i);

                    //JSONObject p = new JSONObject(stop_point.getString("stop_area").toString());
                    //   if(p.getString("name").toUpperCase().contains("GARE DE")) {
                    //  '\\b' gives you the word boundaries
                    //  '\\s*' sops up any white space on either side of the word being removed (if you want to remove this too).
                    String data = p.getString("name");
                    if (!data.isEmpty())
                        return data;
                    else
                        return null;
                    //}
                }
                return null;
            }
        }.execute(endpoint);
    }
}
