package com.sncf.itif.Services.Gare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sncf.itif.Services.Info.Info;
import com.sncf.itif.Services.Info.ServiceInfo;
import com.sncf.itif.Services.Localisation.GPSTracker;
import com.sncf.itif.Services.Network.NetworkOpt;
import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.R;
import com.sncf.itif.Services.Localisation.ServiceLocalisation;
import com.sncf.itif.Services.Secteur.SecteurActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabGareActivity extends Fragment implements ServiceCallBack {

    String urlGareGet;
    ServiceGare serviceGare;
    List<Gare> garesList = new ArrayList<>();
    CustomAdapterGare gareAdapter = null;
    AutoCompleteTextView txtSearchGare;

    Button btnLocaliser;
    Gare selectedGareFromSearch;

    GPSTracker gps;
    String nearestGare;
    Double latitude;
    Double longitude;

    ServiceLocalisation serviceLocalisation;

    ListView infoListView;
    CustomAdapterInfoHome infoAdapterHome = null;
    List<Info> infosList = new ArrayList<>();
    ServiceInfo serviceInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_gare, container, false);
        urlGareGet = getActivity().getResources().getString(R.string.dns) + getActivity().getResources().getString(R.string.url_gare);

        txtSearchGare = (AutoCompleteTextView) view.findViewById(R.id.txt_search_gare);

        //Bouton invisible derrière image localiseur et le texte "Ma position"
        btnLocaliser = (Button) view.findViewById(R.id.btn_localiser);

        //Liste concernant les dernières mises à jours
        infoListView = (ListView) view.findViewById(R.id.infoInGareListView);
        infoAdapterHome = new CustomAdapterInfoHome(getContext(), infosList);
        infoListView.setAdapter(infoAdapterHome);

        return view;
    }

    @Override
    public void onStart() {

        //click event on list item of "autocomplete" object.
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

        //event excuted when you click on "OK" button on keyboard
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

        // click event on "Ma position" and "Image Localiser"; le bouton est invisible
        btnLocaliser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(getContext());
                // check if GPS enabled
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    Log.d("------->Coordinates GPS", "Lat: " + latitude + " Lon: " + longitude);
                    if (latitude != 0.0 && longitude != 0.0) {
/*                        if (garesList.size() < 10) {
                                callServiceGareGet();
                        }*/

                        if (NetworkOpt.isNetworkAvailable(getContext()) == false) {
                            NetworkOpt.showNetworkAlert(getContext());
                            return;
                        } else {
                            callServiceNearestGare();
                        }
                    }
                    //Toast.makeText(getContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert(getContext());
                }
            }
        });

        super.onStart();
    }

    @Override
    public void onResume() {
        // vérifie si l'internet est présent
        if (NetworkOpt.isNetworkAvailable(getContext()) == false) {
            // en absence de l'internet, pop up un alerte dialog box
            NetworkOpt.showNetworkAlert(getContext());
        } else {
            // Call Gare service and Info service to update the content
            callServiceGareGet();
            callServiceInfoGet();
        }

        super.onResume();
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

                // Utilisation des expressions régulières
                String regex = "\\s*\\bGARE D(E|ES|'?\\W)\\b\\s*|\\s*\\bRER\\b\\s*";
                String regex2 = "\\s*-\\s*";
                String regex3 = "\\bST\\b";
                String regex4 = "\\bSS\\b";
                String nearestGare_Modif = nearestGare.trim().toUpperCase()
                        .replaceAll(regex, "").replaceAll(regex2, " ").replaceAll(regex3, "SAINT").replaceAll(regex4, "SOUS");

                //Affichage de la gare trouvée
                Toast.makeText(getContext(), "Gare trouvée: *" + nearestGare_Modif + "*", Toast.LENGTH_SHORT).show();

                // Dans un premier temps, on vérifie si la gare localisé est égale(aux lettres près) dans notre base de gare.
                for (Gare g : garesList) {
                    String g_name = g.getName().toUpperCase().trim().replaceAll(regex, "").replaceAll(regex2, " ");
                    if (nearestGare_Modif.equals(g_name)) {
                        showPositionAlert(getContext(), g);
                    }
                }
                // Dans un second temps, on vérifie si la gare localisé est contenu dans notre base de gare.
                for (Gare g : garesList) {
                    String g_name = g.getName().toUpperCase().trim().replaceAll(regex, "").replaceAll(regex2, " ");
                    if (nearestGare_Modif.contains(g_name)) {
                        showPositionAlert(getContext(), g);
                    }
                }
                //showMessage("Nearest Gare", nearestGare);
            } else
                Toast.makeText(getContext(), "Aucune gare SNCF détectée aux alentours de 500m.", Toast.LENGTH_LONG).show();

        }

        if (id_srv == 3) {
            //Récupération le contenu de la liste Info(MAJ)
            infosList.clear();
            if (object != null) {
                infosList.addAll((List<Info>) object);
                //On inverse la liste obtenue pour afficher les messages plus récentes en premier
                Collections.reverse(infosList);
                infoAdapterHome.notifyDataSetChanged();
            } else
                Toast.makeText(getContext(), "La liste des mises à jours est vide.", Toast.LENGTH_LONG).show();
    }
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(getContext(), "Catch Error in Service Failure :" + exception.getMessage().toString(), Toast.LENGTH_LONG).show();
    }

    public void callServiceGareGet() {
        serviceGare = new ServiceGare(this, getContext(), "getAll");
        serviceGare.enquiry(urlGareGet);
    }

    public void callServiceNearestGare() {
        serviceLocalisation = new ServiceLocalisation(this, getContext(), "getNearestStation");
        String url_half_1 = getActivity().getResources().getString(R.string.url_navitia_half_1);
        String url_half_2 = getActivity().getResources().getString(R.string.url_navitia_half_2);
        serviceLocalisation.enquiry(url_half_1 + longitude + ";" + latitude + url_half_2);
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