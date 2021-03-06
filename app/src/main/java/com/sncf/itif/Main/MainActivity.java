package com.sncf.itif.Main;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.sncf.itif.Menu.ActAboutUs;
import com.sncf.itif.R;


public class MainActivity extends AppCompatActivity {

    MainActPagerAdapter adapter;
    Boolean var = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.main_act_tv_full_title);
        getSupportActionBar().setSubtitle(R.string.main_act_tv_sub_title);

        // avoid to open keyboard automatically when activity starts.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Gares").setIcon(R.drawable.icon_gare_tab));
        tabLayout.addTab(tabLayout.newTab().setText("Plan IDF").setIcon(R.drawable.icon_plan_tab));
        tabLayout.addTab(tabLayout.newTab().setText("Infos").setIcon(R.drawable.icon_info_tab));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient lg = new LinearGradient(0, 0, tabLayout.getWidth(), 0,
                        new int[]{

                                getResources().getColor(R.color.color1),
                                getResources().getColor(R.color.color2),
                                getResources().getColor(R.color.color3),
                                getResources().getColor(R.color.color4_1),
                                getResources().getColor(R.color.color4_2)}, //substitute the correct colors for these
                        new float[]{
                                0.25f, 0.55f, 0.70f, 0.80f, 1},
                        Shader.TileMode.REPEAT);
                return lg;
            }
        };
        PaintDrawable p = new PaintDrawable();
        p.setShape(new RectShape());
        p.setShaderFactory(sf);
        //tabLayout.setBackground(p);
        toolbar.setBackground(p);


        final ViewPager viewPager = (MainActCustomViewPager) findViewById(R.id.pager);
        adapter = new MainActPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = ((MainActPagerAdapter) viewPager.getAdapter()).getFragment(position);
                if (fragment != null)
                    fragment.onResume();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_a_propos) {
            Intent intent = new Intent(this, ActAboutUs.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


}