package com.sncf.itif.Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.sncf.itif.Services.Gare.ActTabGare;
import com.sncf.itif.Services.Info.ActTabInfo;
import com.sncf.itif.Services.PlanIDF.ActTabPlanIDF;

import java.util.HashMap;
import java.util.Map;

public class MainActPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;
    private FragmentManager mFragmentManager;
    private Map<Integer, String> mFragmentTags;

    public MainActPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        mFragmentManager = fm;
        mFragmentTags = new HashMap<Integer, String>();
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ActTabGare tab1 = new ActTabGare();
                return tab1;
            case 1:
                ActTabPlanIDF tab2 = new ActTabPlanIDF();
                return tab2;
            case 2:
                ActTabInfo tab3 = new ActTabInfo();
                return tab3;
            default:
                return null;
        }
    }


//    @Override
//    public int getItemPosition(Object object) {
//
//        return POSITION_NONE;
//    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if(obj instanceof  Fragment){
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
            Log.d("instantiateItem-------",mFragmentTags.toString());
        }
        return obj;
    }

    public Fragment getFragment(int position){
        String tag = mFragmentTags.get(position);
        Log.d("getFragment----------",mFragmentTags.toString());

        if(tag == null)
            return null;
        return mFragmentManager.findFragmentByTag(tag);
    }
}