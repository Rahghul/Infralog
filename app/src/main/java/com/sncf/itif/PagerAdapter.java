package com.sncf.itif;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sncf.itif.Services.Gare.TabGareActivity;
import com.sncf.itif.Services.PlanIDF.TabPlanIDFActivity;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabGareActivity tab1 = new TabGareActivity();
                return tab1;
            case 1:
                TabPlanIDFActivity tab2 = new TabPlanIDFActivity();
                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}