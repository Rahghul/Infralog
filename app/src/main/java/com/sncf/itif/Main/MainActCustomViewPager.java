package com.sncf.itif.Main;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Rahghul on 04/04/2016.
 * //§!!!!!!!!java.lang.IllegalArgumentException: pointerIndex out of range handle this exception
 resolve the problem at plan IDF Ratp plan on zoom in and out from out of pointer exception
 */


public class MainActCustomViewPager extends ViewPager {

    private boolean isLocked;

    public MainActCustomViewPager(Context context) {
        super(context);
        isLocked = false;
    }

    public MainActCustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isLocked = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isLocked) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !isLocked && super.onTouchEvent(event);
    }

    public void toggleLock() {
        isLocked = !isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isLocked() {
        return isLocked;
    }



}