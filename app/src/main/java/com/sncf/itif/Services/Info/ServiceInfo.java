package com.sncf.itif.Services.Info;

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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by Rahghul on 07/04/2016.
 */
public class ServiceInfo {

    private AlertDialog dialog;
    private ServiceCallBack callBack;
    private Exception error;
    private String service;
    private int result;

    public ServiceInfo(ServiceCallBack callBack, Context context, String service) {
        this.callBack = callBack;
        this.service = service;
        this.dialog = new SpotsDialog(context, R.style.Custom);

    }

    public void enquiry(final String endpoint){
        new AsyncTask<String, Void, List<Info>>() {

            @Override
            protected void onPreExecute() {
                dialog.show();
            }


            @Override
            protected List<Info> doInBackground(String... params) {
                try{
                    switch (service){
                        case "getAllInfo" :
                            return getInfo(params[0]);

                        default:
                            throw (new Exception("Unknown Service : Info"));
                    }
                }catch (Exception e){
                    error = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Info> infos) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    if (error != null) {
                        callBack.serviceFailure(error);
                        return;
                    }
                    callBack.serviceSuccess(infos, 3);
                }
                catch (Exception e) {
                    error = e;
                }
            }

            public List<Info> getInfo(String endpoint) throws JSONException {
                String s = WebServiceUtil.requestWebServiceGET(endpoint);
                if(s == null){
                    return null;
                }
                List<Info> foundInfos = new ArrayList<>();

                JSONArray data = new JSONArray(s);
                for(int i =0; i<data.length(); i++){
                    JSONObject p = data.getJSONObject(i);

                    foundInfos.add(
                            new Info(p.getLong("id"),
                                    p.getString("title"),
                                    p.getString("context"),
                                    p.getString("degree"),
                                    new Date(Long.parseLong(p.getString("dateTime")))));
                }
                return foundInfos;
            }
        }.execute(endpoint);
    }

}
