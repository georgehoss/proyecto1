package com.tenneco.tennecoapp.Graphics;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by ghoss on 08/01/2019.
 */
public class MyXAsisDate implements IAxisValueFormatter {
    private String[] mValues;

    public MyXAsisDate(String[] mValues) {
        this.mValues = mValues;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (value>=0 && value<mValues.length)
        return mValues[(int)value];
        else
            if (value<0)
                return mValues[0];
        else
                return mValues[mValues.length-1];
    }
}
