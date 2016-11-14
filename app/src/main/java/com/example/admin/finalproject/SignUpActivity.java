package com.example.admin.finalproject;

import android.app.ProgressDialog;
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

    @BindView(R.id.aSignUpEditLastName)
    EditText editTextLastName;

    @BindView(R.id.aSignUpEditAge)
    EditText editTextAge;

    @BindView(R.id.aSignUpEditEmail)
    EditText editTextEmail;

    @BindView(R.id.aSignUpEditUsername)
    EditText editTextUsername;

    @BindView(R.id.aSignUpEditPass)
    EditText editTextPass;

    @BindView(R.id.aSignUpRadioGrp)
    RadioGroup radioGroup;

    @BindView(R.id.aSignUpRadioMale)
    RadioButton radioGroupMale;

    @BindView(R.id.aSignUpRadioFemale)
    RadioButton radioGroupFemale;

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
        String orientation = radioGroupMale.isChecked() ? "M" : "F";

        User user = new User(name,lastname,Integer.parseInt(age),orientation,username,password,email);

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
                                .setPositiveButton("OK",null).create();
                        failSignUpAlert.show();
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "onNext: ");
                    }
                });
    }
}
