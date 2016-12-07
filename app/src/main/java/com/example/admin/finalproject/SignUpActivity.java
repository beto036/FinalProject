package com.example.admin.finalproject;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.admin.finalproject.entities.User;
import com.example.admin.finalproject.helpers.RetrofitHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.aSignUpEditName)
    EditText editTextName;

    @BindView(R.id.aSignUpEditNameLayout)
    TextInputLayout editTextNameLayout;

    @BindView(R.id.aSignUpEditLastName)
    EditText editTextLastName;

    @BindView(R.id.aSignUpEditLastNameLayout)
    TextInputLayout editTextLastNameLayout;

    @BindView(R.id.aSignUpEditAge)
    EditText editTextAge;

    @BindView(R.id.aSignUpEditAgeLayout)
    TextInputLayout editTextAgeLayout;

    @BindView(R.id.aSignUpEditEmail)
    EditText editTextEmail;

    @BindView(R.id.aSignUpEditEmailLayout)
    TextInputLayout editTextEmailLayout;

    @BindView(R.id.aSignUpEditUsername)
    EditText editTextUsername;

    @BindView(R.id.aSignUpEditUsernameLayout)
    TextInputLayout editTextUsernameLayout;

    @BindView(R.id.aSignUpEditPass)
    EditText editTextPass;

    @BindView(R.id.aSignUpEditPassLayout)
    TextInputLayout editTextPassLayout;


    private static final String TAG = "SignUpActivityTAG_";

    private ProgressDialog alertDialog;
    private AlertDialog failSignUpAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    public void signUp(View view) {
        clearErrors();
        if(validate()) {

            alertDialog = new ProgressDialog(this);
            alertDialog.setTitle("Signing Up");
            alertDialog.setMessage("Almost done...");
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

            String name = editTextName.getText().toString();
            String lastname = editTextLastName.getText().toString();
            String email = editTextEmail.getText().toString();
            String username = editTextUsername.getText().toString();
            String password = editTextPass.getText().toString();
            String age = editTextAge.getText().toString();

            User user = new User(name, lastname, Integer.parseInt(age), username, password, email);

            Observable<User> observable = RetrofitHelper.Factory.insert(user);

            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<User>() {
                        @Override
                        public void onCompleted() {
                            alertDialog.dismiss();
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError: " + e.toString());
                            alertDialog.dismiss();
                            failSignUpAlert = new AlertDialog.Builder(SignUpActivity.this)
                                    .setMessage("The service is unavailable. Please try later.")
                                    .setTitle(R.string.fail_login_title)
                                    .setPositiveButton("OK", null).create();
                            failSignUpAlert.show();
                        }

                        @Override
                        public void onNext(User user) {
                            Log.d(TAG, "onNext: ");
                        }
                    });
        }else{
            AlertDialog failSignUpAlert = new AlertDialog.Builder(SignUpActivity.this)
                    .setMessage("Please check the errors in the form and try again")
                    .setTitle("Error")
                    .setPositiveButton("OK", null).create();
            failSignUpAlert.show();
        }
    }

    private void clearErrors() {
        editTextNameLayout.setError("");
        editTextLastNameLayout.setError("");
        editTextAgeLayout.setError("");
        editTextEmailLayout.setError("");
        editTextPassLayout.setError("");
        editTextUsernameLayout.setError("");
    }

    private boolean validate() {
        boolean res = true;
        if(editTextName.getText().toString().isEmpty()){
            editTextNameLayout.setError("Name cannot be empty");
            res = false;
        }

        if(editTextLastName.getText().toString().isEmpty()){
            editTextLastNameLayout.setError("Lastname cannot be empty");
            res = false;
        }
        if(editTextAge.getText().toString().isEmpty()){
            editTextAgeLayout.setError("Age cannot be empty");
            res = false;
        }
        if(editTextEmail.getText().toString().isEmpty()){
            editTextEmailLayout.setError("Email cannot be empty");
            res = false;
        }
        if(editTextPass.getText().toString().isEmpty()){
            editTextPassLayout.setError("Password cannot be empty");
            res = false;
        }
        if(editTextUsername.getText().toString().isEmpty()){
            editTextUsernameLayout.setError("Username cannot be empty");
            res = false;
        }
        return res;
    }
}
