package com.example.admin.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.admin.finalproject.entities.Event;
import com.example.admin.finalproject.entities.User;
import com.example.admin.finalproject.helpers.EventAdapter;
import com.example.admin.finalproject.helpers.RetrofitHelper;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "HomeActivityTAG_";
    private User user;

//    @BindView(R.id.navHeadHomeTxtUser)
    public TextView userTxt;

//    @BindView(R.id.navHeadHomeTxtMail)
    public TextView mailTxt;

    @BindView(R.id.nav_view)
    public NavigationView navigationView;

    private RecyclerView mRecyclerView;

    private ArrayList<Event> mArrayList;
    private EventAdapter eventAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mArrayList = new ArrayList<Event>();
        user = ((App)getApplication()).getUser();

        Log.d(TAG, "onCreate: " + user.toString());

        EventsFragment eventsFragment = new EventsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.aHomeFragFrame, eventsFragment).commit();

        getEvents();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        userTxt = (TextView) view.findViewById(R.id.navHeadHomeTxtUser);
        mailTxt = (TextView) view.findViewById(R.id.navHeadHomeTxtMail);
        userTxt.setText(user.getName() + " " + user.getLastname());
        mailTxt.setText(user.getEmail());
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void fillRecycler(List<Event> events) {
        mArrayList.clear();
        mArrayList.addAll(events);
        eventAdapter = new EventAdapter(mArrayList);
        mRecyclerView = (RecyclerView) findViewById(R.id.fEventsRecycler);
        mRecyclerView.setAdapter(eventAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        eventAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_event) {
//            Intent intent = new Intent(this, MapsActivity.class);
//            startActivity(intent);
        } else if (id == R.id.nav_find_event) {

        } else if (id == R.id.nav_find_friend) {

        } else if (id == R.id.nav_friends) {

        } else if (id == R.id.nav_events) {
            EventsFragment eventsFragment = new EventsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.aHomeFragFrame, eventsFragment).commit();

            getEvents();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getEvents(){
        Observable<List<Event>> observable = RetrofitHelper.Factory.getEvents(user.getId().get$oid(), false);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Event>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Event> events) {
                        Iterator<Event> iterator = events.iterator();
                        while(iterator.hasNext()) {
                            Event event = iterator.next();
                            Log.d(TAG, "onNext: Simple date format");
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                            Log.d(TAG, "onNext: Initialized");
                            try {
                                Log.d(TAG, "onNext: Parsing date");
                                Date date = sdf.parse(event.getDate().get$date().replaceAll("T"," ").replaceAll("Z",""));
                                Log.d(TAG, "onNext: " + date.toString());
                                Log.d(TAG, "onNext: " + event.toString());
                                Log.d(TAG, "onNext: " + (date.compareTo(new Date()) < 0));
                                if(date.compareTo(new Date()) < 0)
                                    iterator.remove();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        fillRecycler(events);
                    }
                });
    }

}
