package com.example.admin.finalproject;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment {

    TimePickerDialog.OnTimeSetListener listener;

    public TimePickerFragment() {
        // Required empty public constructor
    }


    public void setCallBack(TimePickerDialog.OnTimeSetListener onTime) {
        listener = onTime;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Activity has to implement this interface
//        TimePickerDialog.OnTimeSetListener listener = (TimePickerDialog.OnTimeSetListener) getActivity();

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), listener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }


}
