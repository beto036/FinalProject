package com.example.admin.finalproject;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.admin.finalproject.entities.Event;
import com.example.admin.finalproject.entities.User;
import com.example.admin.finalproject.helpers.RetrofitHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewEventFragment extends Fragment {


    private static final String TAG = "NewEventTAG_";

    private Event event;
    private User user;
    private Unbinder unbinder;
    private GoogleMap googleMap;
    private Calendar now;

    @BindView(R.id.fNewEventName)
    public EditText name;

    @BindView(R.id.fNewEventDesc)
    public EditText description;

    @BindView(R.id.fNewEventMap)
    public MapView mMapView;

    @BindView(R.id.fNewEventDate)
    public EditText date;

    @BindView(R.id.fNewEventHour)
    public EditText time;

    @BindView(R.id.fab)
    public FloatingActionButton save;


    private ProgressDialog alertDialog;
    private AlertDialog failSaveAlert;
    private boolean saveSuccess;

    public AddNewEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_event, container, false);
        unbinder = ButterKnife.bind(this,view);

        user = ((App)getActivity().getApplication()).getUser();

        now = Calendar.getInstance();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });

        event = new Event();

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                addListenerToMap();
            }
        });


        return view;
    }

    private void showTime() {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setCallBack(onTimeSetListener);
        timePickerFragment.show(getActivity().getSupportFragmentManager(),"timePicker");

    }

    public void saveEvent(){
        alertDialog = new ProgressDialog(getActivity());
        alertDialog.setTitle("New Event");
        alertDialog.setMessage("Saving...");
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        event.setEvent(name.getText().toString());
        event.setDescription(description.getText().toString());
        event.setIsAdmin(true);
        event.setUserId(user.getId().get$oid());
        Log.d(TAG, "saveEvent: " + event);

        Observable<Event> observable = RetrofitHelper.Factory.insertEvent(event);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onCompleted() {
                        alertDialog.dismiss();

                        if(saveSuccess){
                            EventsFragment eventsFragment = new EventsFragment();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.aHomeFragFrame, eventsFragment).commit();

                            ((HomeActivity)getActivity()).getEvents(true);
                        }else{
                            failSaveAlert = new AlertDialog.Builder(getContext())
                                    .setMessage("Something is wrong. Please try later...")
                                    .setTitle(R.string.fail_save_title)
                                    .setPositiveButton("OK",null).create();
                            failSaveAlert.show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(Event event) {
                        if (event != null)
                            saveSuccess = true;
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void addListenerToMap(){
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                event.setLatitude(latLng.latitude);
                event.setLongitude(latLng.longitude);

                Log.d(TAG, "onMapClick: " + event);
                // Clears the previously touched position
                googleMap.clear();

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);
            }
        });
    }

    public void showDate(){
        Log.d(TAG, "showDate: ");
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setCallBack(ondate);
        datePickerFragment.show(getActivity().getSupportFragmentManager(),"datepicker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Log.d(TAG, "onDateSet: ");
            date.setText((monthOfYear+1) + "/" + dayOfMonth + "/" + year);
            now.set(Calendar.YEAR, year);
            now.set(Calendar.MONTH, monthOfYear);
            now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            event.setDate(simpleDateFormat.format(now.getTime()));
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            time.setText(hourOfDay + ":" + minute);
            now.set(Calendar.HOUR_OF_DAY, hourOfDay);
            now.set(Calendar.MINUTE, minute);
            now.set(Calendar.SECOND,0);
            now.set(Calendar.MILLISECOND,0);
            event.setDate(simpleDateFormat.format(now.getTime()));
        }
    };
}
