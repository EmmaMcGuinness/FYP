package com.project.growwithsunglow.ui.dashboard;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateValueFormatter extends ValueFormatter {
    private final SimpleDateFormat mFormat;

    public DateValueFormatter(String pattern) {
        mFormat = new SimpleDateFormat(pattern);
    }

    @Override
    public String getFormattedValue(float value) {

        return mFormat.format(new Date((long) value));
    }
}
