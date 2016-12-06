package com.example.admin.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.admin.finalproject.entities.Event;
import com.example.admin.finalproject.entities.Friendship;
import com.example.admin.finalproject.entities.Invitation;
import com.example.admin.finalproject.entities.User;
import com.example.admin.finalproject.helpers.EventAdapter;
import com.example.admin.finalproject.helpers.FriendRequestsAdapter;
import com.example.admin.finalproject.helpers.FriendsAdapter;
import com.example.admin.finalproject.helpers.InvitationsAdapter;
import com.example.admin.finalproject.helpers.RetrofitHelper;
import com.example.admin.finalproject.helpers.UserAdapter;


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
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener{

    private static final String TAG = "HomeActivityTAG_";
    private User user;

//    @BindView(R.id.navHeadHomeTxtUser)
    public TextView userTxt;

//    @BindView(R.id.navHeadHomeTxtMail)
    public TextView mailTxt;

    public SearchView searchView;

    @BindView(R.id.nav_view)
    public NavigationView navigationView;

    private RecyclerView mRecyclerView;

    private ArrayList<Invitation> mInvitationsArrayList;
    private InvitationsAdapter invitationsAdapter;

    private ArrayList<Friendship> mFriendsArrayList;
    private FriendsAdapter friendsAdapter;

    private ArrayList<User> mUserArrayList;
    private UserAdapter userAdapter;

    private FriendRequestsAdapter friendRequestsAdapter;

    private ArrayList<Event> mArrayList;
    private EventAdapter eventAdapter;
    private boolean findVisible;

    private ProgressDialog alertDialog;
    private AlertDialog failAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mArrayList = new ArrayList<Event>();
        mUserArrayList = new ArrayList<User>();
        mFriendsArrayList = new ArrayList<Friendship>();
        mInvitationsArrayList = new ArrayList<Invitation>();
        user = ((App)getApplication()).getUser();

        Log.d(TAG, "onCreate: " + user.toString());

        EventsFragment eventsFragment = new EventsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.aHomeFragFrame, eventsFragment).commit();
        showProgress("Scheduled Events", "Loading...");
        getEvents(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void fillUserRecycler(List<User> users){
        mUserArrayList.clear();
        mUserArrayList.addAll(users);
        userAdapter = new UserAdapter(mUserArrayList);
        mRecyclerView = (RecyclerView) findViewById(R.id.fUsersRecycler);
        mRecyclerView.setAdapter(userAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        userAdapter.notifyDataSetChanged();
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

    private void fillFriendsRecycler(List<Friendship> friendships) {
        mFriendsArrayList.clear();
        mFriendsArrayList.addAll(friendships);
        friendsAdapter = new FriendsAdapter(mFriendsArrayList);
        mRecyclerView = (RecyclerView) findViewById(R.id.fFriendsRecycler);
        mRecyclerView.setAdapter(friendsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        friendsAdapter.notifyDataSetChanged();
    }

    private void fillRequestRecycler(List<Friendship> requests) {
        mFriendsArrayList.clear();
        mFriendsArrayList.addAll(requests);
        friendRequestsAdapter = new FriendRequestsAdapter(mFriendsArrayList);
        mRecyclerView = (RecyclerView) findViewById(R.id.fRequestsRecycler);
        mRecyclerView.setAdapter(friendRequestsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        friendRequestsAdapter.notifyDataSetChanged();
    }

    private void fillInvitationsRecycler(List<Invitation> invitations) {
        mInvitationsArrayList.clear();
        mInvitationsArrayList.addAll(invitations);
        invitationsAdapter = new InvitationsAdapter(mInvitationsArrayList);
        mRecyclerView = (RecyclerView) findViewById(R.id.fInvitationsRecycler);
        mRecyclerView.setAdapter(invitationsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        invitationsAdapter.notifyDataSetChanged();
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

        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem search = menu.findItem(R.id.search);
        search.setVisible(findVisible);
        if(findVisible){
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.requestFocusFromTouch();
            searchView.setOnQueryTextListener(this);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_event) {
            findVisible = false;
            AddNewEventFragment addNewEventFragment = new AddNewEventFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.aHomeFragFrame,addNewEventFragment).commit();
            getSupportActionBar().setTitle("New Event");
        } else if (id == R.id.nav_find_event) {
            findVisible = false;
            EventsFragment eventsFragment = new EventsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.aHomeFragFrame, eventsFragment).commit();
            getSupportActionBar().setTitle("Scheduled Events");
            showProgress("Scheduled Events", "Loading...");
            getEvents(false);

        } else if (id == R.id.nav_find_friend) {
            findVisible = true;
            UsersFragment usersFragment = new UsersFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.aHomeFragFrame,usersFragment).commit();
            getSupportActionBar().setTitle("Find Friend");
        } else if (id == R.id.nav_friends) {
            findVisible = false;
            FriendsFragment friendsFragment = new FriendsFragment();
            this.getSupportFragmentManager().beginTransaction().replace(R.id.aHomeFragFrame,friendsFragment).commit();
            getSupportActionBar().setTitle("My Friends");
            showProgress("Friends","Loading...");
            this.getFriends(false);
        } else if (id == R.id.nav_events) {
            findVisible = false;
            EventsFragment eventsFragment = new EventsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.aHomeFragFrame, eventsFragment).commit();
            getSupportActionBar().setTitle("My Events");
            showProgress("Events", "Loading...");
            getEvents(true);
        }else if (id == R.id.nav_invitations) {
            findVisible = false;
            String query = "{\"userId\":\"" + user.getId().get$oid() + "\","+
                    "\"declined\":false, \"confirmed\":false}";
            InvitationsFragment invitationsFragment = new InvitationsFragment();
            this.getSupportFragmentManager().beginTransaction().replace(R.id.aHomeFragFrame,invitationsFragment).commit();
            getSupportActionBar().setTitle("My Invitations");
            showProgress("Invitations", "Loading");
            getInvitations(query);
        }else if (id == R.id.nav_friend_requests) {
            findVisible = false;
            FriendRequestsFragment friendRequestsFragment = new FriendRequestsFragment();
            this.getSupportFragmentManager().beginTransaction().replace(R.id.aHomeFragFrame,friendRequestsFragment).commit();
            getSupportActionBar().setTitle("Friend Requests");
            showProgress("Requests","Loading...");
            getFriends(true);
        }
        invalidateOptionsMenu();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showProgress(String title, String message) {
        alertDialog = new ProgressDialog(this);
        // Setting Dialog Title
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void getEvents(boolean isAdmin){
        Observable<List<Event>> observable = RetrofitHelper.Factory.getEvents(user.getId().get$oid(), isAdmin);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Event>>() {
                    @Override
                    public void onCompleted() {
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                        alertDialog.dismiss();
                        showErrorDialog();
                    }

                    @Override
                    public void onNext(List<Event> events) {
                        Iterator<Event> iterator = events.iterator();
                        while(iterator.hasNext()) {
                            Event event = iterator.next();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                            try {
                                Date date = sdf.parse(event.getDate());
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

    private void showErrorDialog() {
        failAlert = new AlertDialog.Builder(HomeActivity.this)
                .setMessage("The service is unavailable. Please try later.")
                .setTitle("Error")
                .setPositiveButton("OK",null).create();
        failAlert.show();
    }

    private void getUsers(String email){
        Observable<List<User>> observable = RetrofitHelper.Factory.getUserByEmail(email);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onCompleted() {
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                        alertDialog.dismiss();
                        showErrorDialog();
                    }

                    @Override
                    public void onNext(List<User> users) {
                        fillUserRecycler(users);
                    }
                });
    }

    public void getFriends(final boolean isRequest){
        Observable<List<Friendship>> observable = RetrofitHelper.Factory.getFriends(user, null, isRequest);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Friendship>>() {
                    @Override
                    public void onCompleted() {
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                        alertDialog.dismiss();
                        showErrorDialog();
                    }

                    @Override
                    public void onNext(List<Friendship> friendships) {
                        if(isRequest)
                            fillRequestRecycler(friendships);
                        else
                            fillFriendsRecycler(friendships);
                    }
                });
    }

    public void getInvitations(String query){
        Observable<List<Invitation>> observable = RetrofitHelper.Factory.getInvitations(query);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Invitation>>() {
                    @Override
                    public void onCompleted() {
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                        alertDialog.dismiss();
                        showErrorDialog();
                    }

                    @Override
                    public void onNext(List<Invitation> invitations) {
                        fillInvitationsRecycler(invitations);
                    }
                });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, "onQueryTextSubmit: ");
        showProgress("Find Friends", "Searching...");
        getUsers(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void logout(MenuItem item) {
        ((App)getApplication()).setUser(null);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
