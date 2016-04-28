package com.sncf.itif.Services.PlanIDF.Telechargement;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

//custom Display all plans in gridview without scrolling...
public class TelechargementCustomGridView extends GridView {

    public TelechargementCustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TelechargementCustomGridView(Context context) {
        super(context);
    }

    public TelechargementCustomGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //Display all plans in gridview without scrolling...
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}