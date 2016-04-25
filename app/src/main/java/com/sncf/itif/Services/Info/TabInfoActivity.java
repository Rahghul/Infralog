package com.sncf.itif.Services.Info;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.sncf.itif.R;
import com.sncf.itif.Services.Gare.TabGareActivity;
import com.sncf.itif.Services.Network.NetworkOpt;
import com.sncf.itif.Services.ServiceCallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabInfoActivity extends Fragment implements ServiceCallBack {

    ServiceInfo serviceInfo;

    List<Info> infosList = new ArrayList<Info>();
    ListView infoListView;
    CustomAdapterInfo infoAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_info, container, false);

        infoListView = (ListView) view.findViewById(R.id.infoListView);
        Collections.reverse(infosList);
        infoAdapter = new CustomAdapterInfo(getContext(), infosList);
        infoListView.setAdapter(infoAdapter);


//        if (isNetworkAvailable() == true) {
//            callServiceInfoGet();
//        }

        return view;
    }

    @Override
    public void onResume() {

        if (NetworkOpt.isNetworkAvailable(getContext()) == true) {
            callServiceInfoGet();
        }

        //Hide Soft Keyboard Android if it s available
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        super.onResume();
    }

    @Override
    public void serviceSuccess(Object object, int id_srv) {
        infosList.clear();
        if (id_srv == 3) {
            infosList.addAll((List<Info>) object);

            //showMessage("Infos List", infosList.toString());
            Collections.reverse(infosList);
            infoAdapter.notifyDataSetChanged();

           /* //Put the value
            TabGareActivity ldf = new TabGareActivity ();
            Bundle args = new Bundle();
            args.putString("YourKey", "YourValue");
            ldf.setArguments(args);
            //Inflate the fragment
            getFragmentManager().beginTransaction().add(R.id.container, ldf).commit();*/
        } else
            Toast.makeText(getContext(), "La liste des infos est vide.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(getContext(), "Catch Error in Info Failure :" + exception.getMessage().toString(), Toast.LENGTH_LONG).show();
    }

    public void callServiceInfoGet() {
        serviceInfo = new ServiceInfo(this, getContext(), "getAllInfo");
        String urlGetInfo = getActivity().getResources().getString(R.string.dns) + getActivity().getResources().getString(R.string.url_info);
        serviceInfo.enquiry(urlGetInfo);
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }



}