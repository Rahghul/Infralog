package com.sncf.itif.Services.Network;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Button;

import com.sncf.itif.R;

/**
 * Created by Rahghul on 12/04/2016.
 */
public final class  NetworkOpt {

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void showNetworkAlert(final Context mContext) {
        AlertDialog.Builder customBuilder = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        customBuilder.setTitle("Paramètre Internet :");
        customBuilder.setIcon(R.drawable.ic_warning_violet_18dp);

        // Setting Dialog Message
        customBuilder.setMessage("Vous n'avez pas accès à l'Internet. Merci de vérifier votre connexion.");

        // On pressing Settings button
        customBuilder.setPositiveButton("Paramètres", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        customBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = customBuilder.create();
        dialog.show();

        Button btn_negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        btn_negative.setTextColor(mContext.getResources().getColor(R.color.color3));

        Button btn_positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btn_positive.setTextColor(mContext.getResources().getColor(R.color.color3));
    }
}
