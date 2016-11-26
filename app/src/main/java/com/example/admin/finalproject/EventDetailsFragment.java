package com.example.admin.finalproject;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.finalproject.entities.Event;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment {

    @BindView(R.id.fEventDetailTxtTitle)
    public TextView title;

    @BindView(R.id.fEventDetailTxtDesc)
    public TextView description;

    private Event event;
    private Unbinder unbinder;

    private static final String TAG = "EDetailFragTAG_";

    public EventDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        unbinder = ButterKnife.bind(this,view);

        event = this.getArguments().getParcelable("EVENT");
        title.setText(event.getEvent());
        description.setText(event.getDescription());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
