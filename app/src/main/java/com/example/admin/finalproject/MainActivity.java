package com.example.admin.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.admin.finalproject.entities.User;
import com.example.admin.finalproject.helpers.RetrofitHelper;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TOKEN_KEY = "TOKEN_KEY";
    @BindView(R.id.aMainInputUsername)
    public EditText editTextUsername;

    @BindView(R.id.aMainInputPassword)
    public EditText editTextPass;

//    @BindView(R.id.aMainBtnForgot)
//    public Button textViewForgot;


    private ProgressDialog alertDialog;
    private AlertDialog failLoginAlert;
    private boolean loginSuccess;
    private static final String TAG = "MainActivityTAG_";

    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


    }



    public void login(View view) {
        String username = editTextUsername.getText().toString();
        String password = editTextPass.getText().toString();
        alertDialog = new ProgressDialog(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Log In");
        alertDialog.setMessage("Authenticating...");
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        Observable<List<User>> observable = RetrofitHelper.Factory.create(username,password);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                        alertDialog.dismiss();
                        if(loginSuccess){
                            ((App)getApplication()).setUser(user);
                            Log.d(TAG, "onCompleted: " + user);
                            validateAndUpdateUser();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }else{
                            failLoginAlert = new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("Please check your username/password. If you are not a user yet please Sign Up.")
                                    .setTitle(R.string.fail_login_title)
                                    .setPositiveButton("OK",null).create();
                            failLoginAlert.show();
                        }
                        Log.d(TAG, "onCompleted: I completed the request for login");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                        alertDialog.dismiss();
                        failLoginAlert = new AlertDialog.Builder(MainActivity.this)
                                .setMessage("The service is unavailable. Please try later.")
                                .setTitle(R.string.fail_login_title)
                                .setPositiveButton("OK",null).create();
                        failLoginAlert.show();
                    }

                    @Override
                    public void onNext(List<User> users) {
                        for (User user : users) Log.d(TAG, "onNext: " + user);
                        if(users != null && users.size() > 0) {
                            user = users.get(0);
                            loginSuccess = true;
                        }
                    }
                });
    }

    private void validateAndUpdateUser() {
        Log.d(TAG, "validateAndUpdateUser: ");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = sharedPreferences.getString(TOKEN_KEY,"");
        Log.d(TAG, "validateAndUpdateUser: " + token);
        if(token != null){
            if(user.getDeviceToken() == null || !token.equals(user.getDeviceToken())){
                user.setDeviceToken(token);
                Log.d(TAG, "validateAndUpdateUser: different tokens**********************************************");
//                User user = new User();
//                User userUpdate = new User();
//                userUpdate.setDeviceToken(token);
//                user.setUpdateUser(userUpdate);
                //Update user token
                Log.d(TAG, "validateAndUpdateUser: User" + user.getUpdateUser());
                Observable<User> observable = RetrofitHelper.Factory.update(user, this.user.getId().get$oid());
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<User>() {
                            @Override
                            public void onCompleted() {
                                Log.d(TAG, "onCompleted: ");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: " + e.toString());
                            }

                            @Override
                            public void onNext(User user) {
                                Log.d(TAG, "validateAndUpdateUser onNext: " + user.toString());
                            }
                        });
            }
        }
    }

    public void forgot(View view) {
    }


    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
