package com.sncf.itif.Menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.sncf.itif.R;

public class ActAboutUs extends AppCompatActivity {

    TextView tvAboutUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about_us);

        getSupportActionBar().setTitle(getResources().getString(R.string.main_act_menu_action_a_propos));
        getSupportActionBar().setSubtitle(R.string.global_tv_short_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvAboutUs = (TextView) findViewById(R.id.tv_about_us);


    }

    @Override
    protected void onStart() {

        tvAboutUs.setText(getString(R.string.act_about_us_tv_context));
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;

        }
         return super.onOptionsItemSelected(item);
    }
}
