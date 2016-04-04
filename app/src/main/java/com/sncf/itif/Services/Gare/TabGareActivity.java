package com.sncf.itif.Services.Gare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sncf.itif.Services.Localisation.GPSTracker;
import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.R;
import com.sncf.itif.Services.Localisation.ServiceLocalisation;
import com.sncf.itif.Services.Secteur.SecteurActivity;

import java.util.ArrayList;
import java.util.List;

public class TabGareActivity extends Fragment implements ServiceCallBack {

    //String urlGareGet = "http://itif.cloudapp.net/sncf/rest/gare/";
    String urlGareGet;
    ServiceGare serviceGare;
    List<Gare> garesList = new ArrayList<>();
    CustomAdapterGare gareAdapter = null;
    ProgressDialog dialog;
    AutoCompleteTextView txtSearchGare;
    //Button btnSearchGare;
    ImageView btnSearchGare;
    TextView tv_position;
    Gare selectedGareFromSearch;

    GPSTracker gps;
    String nearestGare;
    Double latitude;
    Double longitude;

    ServiceLocalisation serviceLocalisation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        urlGareGet = getActivity().getResources().getString(R.string.dns) + getActivity().getResources().getString(R.string.url_gare);
        View view = inflater.inflate(R.layout.tab_gare, container, false);

        dialog = new ProgressDialog(getContext());

        txtSearchGare = (AutoCompleteTextView) view.findViewById(R.id.txt_search_gare);
        txtSearchGare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                selectedGareFromSearch = (Gare) parent.getItemAtPosition(position);
                Log.d("Item clicked", selectedGareFromSearch.getName());

                //start display Secteur Activity
                Intent intent = new Intent(getContext(), SecteurActivity.class);
                intent.putExtra("SelectedGareId", selectedGareFromSearch.getId());
                startActivityForResult(intent, 1);
            }
        });


        txtSearchGare.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String gareName = txtSearchGare.getText().toString();
                    if (!gareName.isEmpty()) {
                        for (Gare g : garesList) {
                            if (g.getName().trim().equals(gareName)) {
                                //start display Secteur Activity
                                Intent intent = new Intent(getContext(), SecteurActivity.class);
                                intent.putExtra("SelectedGareId", g.getId());
                                startActivityForResult(intent, 1);
                            } /*else {
                                Toast.makeText(getContext(), "Cette gare n'est pas répertoriée dans notre base.", Toast.LENGTH_SHORT).show();
                            }*/
                        }
                    }
                }
                return false;
            }
        });

        //evenement Click : Bouton de recherche géolocalisation
        tv_position = (TextView) view.findViewById(R.id.tv_ma_position);
        tv_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(getContext());
                // check if GPS enabled
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    Log.d("------->Coordinates GPS", "Lat: " + latitude + " Lon: " + longitude);
                    if (latitude != 0.0 && longitude != 0.0)
                        if(garesList.size()<10)
                            callServiceGareGet();

                        callServiceNearestGare();
                } else {
                    gps.showSettingsAlert(getContext());
                }
            }
        });
        btnSearchGare = (ImageView) view.findViewById(R.id.btn_localiser);
        btnSearchGare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gps = new GPSTracker(getContext());
                // check if GPS enabled
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    Log.d("------->Coordinates GPS", "Lat: " + latitude + " Lon: " + longitude);
                    if (latitude != 0.0 && longitude != 0.0)
                        //callServiceGareGet();
                        if(garesList.size()<10)
                            callServiceGareGet();
                        callServiceNearestGare();
                    // \n is for new line
                    //Toast.makeText(getContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    //gps.showSettingsAlert();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert(getContext());
                }
            }
        });


        callServiceGareGet();

        return view;
    }


    @Override
    public void serviceSuccess(Object object, int id_srv) {
        if (id_srv == 1) {
            //Get List ALL Gares
            garesList.clear();
            if (object != null) {
                garesList.addAll((List<Gare>) object);
                gareAdapter = new CustomAdapterGare(getContext(), R.layout.tab_gare, R.id.lbl_name, garesList);
                txtSearchGare.setAdapter(gareAdapter);
                gareAdapter.setNotifyOnChange(true);
                //gareAdapter.notifyDataSetChanged();

                //showMessage("Show Gare List",  garesList.toString());
            } else
                Toast.makeText(getContext(), "La liste des gares est vide.", Toast.LENGTH_LONG).show();
        }

        if (id_srv == 2) {
            if (object != null) {

                nearestGare = ((String) object);

                String regex = "\\s*\\bGARE D(E|ES|'?\\W)\\b\\s*|\\s*\\bRER\\b\\s*";
                String regex2 = "\\s*-\\s*";
                String regex3 = "\\bST\\b";
                String regex4 = "\\bSS\\b";

                String nearestGare_Modif = nearestGare.trim().toUpperCase()
                        .replaceAll(regex, "").replaceAll(regex2, " ").replaceAll(regex3,"SAINT").replaceAll(regex4,"SOUS");
                Toast.makeText(getContext(), "Gare trouvée: *" + nearestGare_Modif + "*", Toast.LENGTH_SHORT).show();

                for (Gare g : garesList) {
                    String g_name = g.getName().toUpperCase().trim().replaceAll(regex, "").replaceAll(regex2, " ");
                    if (nearestGare_Modif.equals(g_name)) {
                    //if (nearestGare_Modif.contains(g_name) || nearestGare_Modif.equals(g_name) ) {
                        showPositionAlert(getContext(), g);
                    }
                }
                //showMessage("Nearest Gare", nearestGare);
            } else
                Toast.makeText(getContext(), "Aucune gare SNCF détectée aux alentours de 500m.", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(getContext(), "Catch Error in Service Failure :" + exception.getMessage().toString(), Toast.LENGTH_LONG).show();
    }

    public void callServiceGareGet() {

        serviceGare = new ServiceGare(this, dialog, "getAll");
        serviceGare.enquiry(urlGareGet);
    }

    public void callServiceNearestGare() {
        serviceLocalisation = new ServiceLocalisation(this, dialog, "getNearestStation");
        String url_half_1 = getActivity().getResources().getString(R.string.url_navitia_half_1);
        String url_half_2 = getActivity().getResources().getString(R.string.url_navitia_half_2);
        serviceLocalisation.enquiry(url_half_1 + longitude + ";" + latitude + url_half_2);
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void showPositionAlert(final Context mContext, final Gare gare) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Position estimée:");

        // Setting Dialog Message
        alertDialog.setMessage("Souhaitez vous consulter les plans de la gare " + gare.getName() + " ?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), SecteurActivity.class);
                intent.putExtra("SelectedGareId", gare.getId());
                startActivityForResult(intent, 1);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Refuser", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


}