package com.sncf.itif.Services.Gare;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sncf.itif.Services.Info.Info;
import com.sncf.itif.Services.Info.ServiceInfo;
import com.sncf.itif.Global.Localisation.GPSTracker;
import com.sncf.itif.Global.Network.NetworkMonitor;
import com.sncf.itif.Services.ServiceCallBack;
import com.sncf.itif.R;
import com.sncf.itif.Global.Localisation.ServiceLocalisation;
import com.sncf.itif.Services.Secteur.ActSecteur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActTabGare extends Fragment implements ServiceCallBack{

    String urlGareGet;
    ServiceGare serviceGare;
    List<Gare> garesList = new ArrayList<Gare>();
    GareAutoCompleteAdapter gareAdapter = null;
    AutoCompleteTextView txtSearchGare;

    Button btnLocaliser;
    TextView display_closest_gare;
    Boolean my_position_clicked = false;

    Gare selectedGareFromSearch;

    GPSTracker gps;
    String nearestGare;

    ServiceLocalisation serviceLocalisation;

    ListView infoListView;
    GareListViewInfoAdapter infoAdapterHome = null;
    List<Info> infosList = new ArrayList<Info>();
    ServiceInfo serviceInfo;

    ImageView refresh_gps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_tab_gare, container, false);
        urlGareGet = getActivity().getResources().getString(R.string.global_server_endpoint) + getActivity().getResources().getString(R.string.global_server_url_gare);

        display_closest_gare = (TextView) view.findViewById(R.id.tv_nearest_gare);
        txtSearchGare = (AutoCompleteTextView) view.findViewById(R.id.txt_search_gare);

        //Bouton invisible derrière image localiseur et le texte "Ma position"
        btnLocaliser = (Button) view.findViewById(R.id.btn_localiser);

        //Liste concernant les dernières mises à jours
        infoListView = (ListView) view.findViewById(R.id.infoInGareListView);
        infoAdapterHome = new GareListViewInfoAdapter(getContext(), infosList);
        infoListView.setAdapter(infoAdapterHome);

        refresh_gps = (ImageView) view.findViewById(R.id.btn_refresh);

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
                goToSecteurActivity(selectedGareFromSearch);
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
                                goToSecteurActivity(g);
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
                my_position_clicked = true;
                LocateUser();
            }
        });

        refresh_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocateUser();
            }
        });

        super.onStart();
    }

    @Override
    public void onResume() {
        // vérifie si l'internet est présent
        if (NetworkMonitor.isNetworkAvailable(getContext()) == false) {
            // en absence de l'internet, pop up un alerte dialog box
            NetworkMonitor.showNetworkAlert(getContext());
        } else {
            // Call Gare service and Info service to update the content
            callServiceGareGet();
            callServiceInfoGet();
            my_position_clicked = false;
            LocateUser();
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
                gareAdapter = new GareAutoCompleteAdapter(getContext(), R.layout.act_tab_gare, R.id.lbl_name, garesList);
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
                //  Toast.makeText(getContext(), "Gare trouvée: *" + nearestGare_Modif + "*", Toast.LENGTH_SHORT).show();

                // Dans un premier temps, on vérifie si la gare localisé est égale(aux lettres près) dans notre base de gare.
                for (Gare g : garesList) {
                    String g_name = g.getName().toUpperCase().trim().replaceAll(regex, "").replaceAll(regex2, " ");
                    if (nearestGare_Modif.equals(g_name)) {
                        //  showPositionAlert(getContext(), g);
                        if (my_position_clicked == false)
                            display_closest_gare.setText(g.getName());
                        else
                            goToSecteurActivity(g);
                    }
                }
                // Dans un second temps, on vérifie si la gare localisé est contenu dans notre base de gare.
                for (Gare g : garesList) {
                    String g_name = g.getName().toUpperCase().trim().replaceAll(regex, "").replaceAll(regex2, " ");
                    if (nearestGare_Modif.contains(g_name)) {
                        //  showPositionAlert(getContext(), g);
                        if (my_position_clicked == false)
                            display_closest_gare.setText(g.getName());
                        else
                            goToSecteurActivity(g);
                    }
                }
                //showMessage("Nearest Gare", nearestGare);
            } else {
                //Toast.makeText(getContext(), "Aucune gare SNCF détectée aux alentours de 500m.", Toast.LENGTH_LONG).show();
                display_closest_gare.setText("Aucune à proximité");
            }
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

    public void callServiceNearestGare(Double lat, Double lon) {
        serviceLocalisation = new ServiceLocalisation(this, getContext(), "getNearestStation");
        String url_half_1 = getActivity().getResources().getString(R.string.act_tab_gare_api_url_navitia_1);
        String url_half_2 = getActivity().getResources().getString(R.string.act_tab_gare_api_url_navitia_2);
        serviceLocalisation.enquiry(url_half_1 + lon + ";" + lat + url_half_2);
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

    /*public void showPositionAlert(final Context mContext, final Gare gare) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Position estimée:");

        // Setting Dialog Message
        alertDialog.setMessage("Souhaitez vous consulter les plans de la gare " + gare.getName() + " ?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                goToSecteurActivity(gare);
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
*/

    void goToSecteurActivity(Gare gare) {
        Intent intent = new Intent(getContext(), ActSecteur.class);
        intent.putExtra("SelectedGareId", gare.getId());
        intent.putExtra("SelectedGareName", gare.getName());
        startActivityForResult(intent, 1);
    }

    void LocateUser() {
        Double latitude;
        Double longitude;
        gps = new GPSTracker(getContext());
        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            Log.d("------->Coordinates GPS", "Lat: " + latitude + " Lon: " + longitude);
            if (latitude != 0.0 && longitude != 0.0) {
                if (NetworkMonitor.isNetworkAvailable(getContext()) == false) {
                    NetworkMonitor.showNetworkAlert(getContext());
                    return;
                } else {
                    callServiceNearestGare(latitude, longitude);
                }
            }
            //Toast.makeText(getContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            display_closest_gare.setText("Erreur position !");
            if (my_position_clicked == true)
                gps.showSettingsAlert(getContext());
        }
    }


}