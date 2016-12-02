package com.example.admin.finalproject;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.finalproject.entities.Event;
import com.example.admin.finalproject.entities.Friendship;
import com.example.admin.finalproject.entities.Invitation;
import com.example.admin.finalproject.entities.User;
import com.example.admin.finalproject.helpers.RetrofitHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
public class EventDetailsFragment extends Fragment {

    @BindView(R.id.fEventDetailTxtTitle)
    public TextView title;

    @BindView(R.id.fEventDetailTxtDesc)
    public TextView description;

    @BindView(R.id.fEventDetailsInviteBtn)
    public Button invite;

    @BindView(R.id.fEventDetailsMap)
    public MapView mMapView;

    private Event event;
    private Unbinder unbinder;
    private GoogleMap googleMap;

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
        if(event.getIsAdmin()){
            invite.setVisibility(View.VISIBLE);
            invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getInvitations(event.getId().get$oid());
                }
            });
        }
        title.setText(event.getEvent());
        description.setText(event.getDescription());

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
//                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(event.getLatitude(),event.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    public void getFriends(final List<Invitation> invitations, final User user){
        Observable<List<Friendship>> observable = RetrofitHelper.Factory.getFriends(user, null);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Friendship>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(List<Friendship> friendships) {
                        ArrayList<Friendship> friendshipArrayList = new ArrayList<Friendship>(friendships);
                        Iterator<Friendship> friendshipIterator = friendshipArrayList.iterator();
                        while (friendshipIterator.hasNext()){
                            Friendship friendship = friendshipIterator.next();
                            String userId = !user.getId().get$oid().equals(friendship.getReceiverId()) ? friendship.getReceiverId() : friendship.getSenderId();
                            for (Invitation invitation : invitations){
                                if(invitation.getUserId().equals(userId)){
                                    friendshipIterator.remove();
                                }
                            }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("FRIENDSHIP_LIST", friendshipArrayList);
                        bundle.putParcelable("CURRENT_EVENT", event);
                        UsersFragmentDialog usersFragmentDialog = new UsersFragmentDialog();
                        usersFragmentDialog.setArguments(bundle);
                        usersFragmentDialog.show(getActivity().getSupportFragmentManager(),"DIALOG_TAG");
                    }
                });
    }

    private void getInvitations(String eventId){
        final User user = ((App)this.getContext().getApplicationContext()).getUser();
        Observable<List<Invitation>> observable = RetrofitHelper.Factory.getInvitations(eventId);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Invitation>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Invitation> invitations) {
                        getFriends(invitations, user);
                    }
                });
    }

}
