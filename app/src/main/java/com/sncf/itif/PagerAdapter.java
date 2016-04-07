package com.sncf.itif;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.sncf.itif.Services.Gare.TabGareActivity;
import com.sncf.itif.Services.Info.TabInfoActivity;
import com.sncf.itif.Services.PlanIDF.TabPlanIDFActivity;

import java.util.HashMap;
import java.util.Map;

public class PagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;
    private FragmentManager mFragmentManager;
    private Map<Integer, String> mFragmentTags;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        mFragmentManager = fm;
        mFragmentTags = new HashMap<Integer, String>();
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
                TabInfoActivity tab3 = new TabInfoActivity();
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