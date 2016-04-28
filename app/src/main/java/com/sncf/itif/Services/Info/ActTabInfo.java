package com.sncf.itif.Services.Info;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.sncf.itif.R;
import com.sncf.itif.Global.Network.NetworkMonitor;
import com.sncf.itif.Services.ServiceCallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActTabInfo extends Fragment implements ServiceCallBack {

    ServiceInfo serviceInfo;

    List<Info> infosList = new ArrayList<Info>();
    ListView infoListView;
    InfoListViewAdapter infoAdapter = null;

    //Refresh
    SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_tab_info, container, false);

        infoListView = (ListView) view.findViewById(R.id.infoListView);
        Collections.reverse(infosList);
        infoAdapter = new InfoListViewAdapter(getContext(), infosList);
        infoListView.setAdapter(infoAdapter);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        // sets the colors used in the refresh animation
        swipeLayout.setColorSchemeResources(R.color.color1, R.color.color2,
                R.color.color3, R.color.color4_1, R.color.color4_2);

        return view;
    }

    @Override
    public void onStart() {
        //Refresh when we swipe layout
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        onResume();
                    }
                }, 1000);
            }
        });

        //enable refresh button only if first element of Info listView is visible
        infoListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (infoListView != null && infoListView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = infoListView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = infoListView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeLayout.setEnabled(enable);
            }
        });
        super.onStart();
    }

    @Override
    public void onResume() {

        if (NetworkMonitor.isNetworkAvailable(getContext()) == true) {
            callServiceInfoGet();
        }

        //Hide Soft Keyboard Android if it s available
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
            ActTabGare ldf = new ActTabGare ();
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
        String urlGetInfo = getActivity().getResources().getString(R.string.global_server_endpoint) + getActivity().getResources().getString(R.string.global_server_url_info);
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