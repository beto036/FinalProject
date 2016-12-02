package com.example.admin.finalproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.finalproject.entities.Friendship;
import com.example.admin.finalproject.entities.User;
import com.example.admin.finalproject.entities.notification.Data;
import com.example.admin.finalproject.entities.notification.Notification;
import com.example.admin.finalproject.entities.notification.SendNotificationRequest;
import com.example.admin.finalproject.entities.notification.SendNotificationResponse;
import com.example.admin.finalproject.helpers.NotificationRetrofitHelper;
import com.example.admin.finalproject.helpers.RetrofitHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersDetailFragment extends Fragment {

    private static final String TAG = "UserDetFragTAG_";
    @BindView(R.id.fUserDetailTxtName)
    public TextView name;

    @BindView(R.id.fUserDetailTxtEmail)
    public TextView email;

    @BindView(R.id.fUserDetailTxtMsg)
    public TextView msg;

    @BindView(R.id.fUsersDetailReqButton)
    public Button send;

    @BindView(R.id.fUsersDetailReqAccept)
    public Button accept;

    @BindView(R.id.fUsersDetailReqCancel)
    public Button decline;

    private Unbinder unbinder;
    private User user;
    private Friendship friendship;
    private boolean saved;
    private boolean isRequest;
    private boolean isReceiver;


    public UsersDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        saved = false;
        isReceiver = false;
        isRequest = false;
        View view = inflater.inflate(R.layout.fragment_users_detail, container, false);
        unbinder = ButterKnife.bind(this,view);
        user = this.getArguments().getParcelable("USER");
        friendship = this.getArguments().getParcelable("FRIENDSHIP");
        if(this.getArguments().containsKey("IS_REQUEST")) {
            isRequest = this.getArguments().getBoolean("IS_REQUEST");
            isReceiver = this.getArguments().getBoolean("RECEIVER");
            msg.setVisibility(View.VISIBLE);
            send.setVisibility(View.INVISIBLE);
            send.setActivated(false);
            if(isRequest){
                if(isReceiver) {
                    if(friendship.getDeclined()){
                        msg.setVisibility(View.VISIBLE);
                        msg.setText("You declined the friend request from this user");
                        send.setVisibility(View.INVISIBLE);
                        send.setActivated(true);
                    }else {
                        msg.setText("You have a friend request from this user. Add as a friend?");
                        decline.setVisibility(View.VISIBLE);
                        accept.setVisibility(View.VISIBLE);
                        decline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (friendship != null) {
                                    friendship.setDeclined(true);
                                    updateFriendship();
                                }
                            }
                        });

                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (friendship != null) {
                                    friendship.setIsRequest(false);
                                    updateFriendship();
                                }
                            }
                        });
                    }
                }else
                    msg.setText("You've already sent a friend request to this user");
            }
            else msg.setText("This user is your friend");

        }else{
            msg.setVisibility(View.INVISIBLE);
            send.setVisibility(View.VISIBLE);
            send.setActivated(true);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User userApp = ((App)v.getContext().getApplicationContext()).getUser();
                    Friendship friendship = new Friendship();
                    friendship.setSenderId(userApp.getId().get$oid());
                    friendship.setNameSender(userApp.getName());
                    friendship.setLastnameSender(userApp.getLastname());
                    friendship.setEmailSender(userApp.getEmail());
                    friendship.setReceiverId(user.getId().get$oid());
                    friendship.setNameReceiver(user.getName());
                    friendship.setLastnameReceiver(user.getLastname());
                    friendship.setEmailReceiver(user.getEmail());
                    friendship.setIsRequest(true);
                    friendship.setDeclined(false);
                    saveAndSendRequest(friendship);
                }
            });
        }

        name.setText(user.getName() + " " + user.getLastname());
        email.setText(user.getEmail());

        return view;
    }

    private void updateFriendship() {
        Observable<Friendship> observable = RetrofitHelper.Factory.updateFriendship(friendship);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Friendship>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                        sendToFriendsView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onNext(Friendship friendship) {
                        Log.d(TAG, "onNext: " + friendship);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void saveAndSendRequest(Friendship friendship){
        Observable<Friendship> observable = RetrofitHelper.Factory.saveFriend(friendship);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Friendship>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                        if(saved){
                            SendNotificationRequest sendNotificationRequest = new SendNotificationRequest();
                            sendNotificationRequest.setTo(user.getDeviceToken());
                            Data data = new Data();
                            data.setScore("654654");
                            Notification notification = new Notification();
                            notification.setTitle("Friend Request");
                            sendNotificationRequest.setData(data);
                            sendNotificationRequest.setNotification(notification);

                            Call<SendNotificationResponse> call = NotificationRetrofitHelper.Factory.sendFriendRequest(sendNotificationRequest);

                            call.enqueue(new Callback<SendNotificationResponse>() {
                                @Override
                                public void onResponse(Call<SendNotificationResponse> call, Response<SendNotificationResponse> response) {
                                    SendNotificationResponse sendNotificationResponse = response.body();
                                    Log.d(TAG, "onResponse: " + sendNotificationResponse);
                                    if(sendNotificationResponse.getSuccess() == 1) send.setEnabled(false);
                                    sendToFriendsView();
                                }

                                @Override
                                public void onFailure(Call<SendNotificationResponse> call, Throwable t) {
                                    Log.d(TAG, "onFailure: " + t.toString());
                                    sendToFriendsView();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onNext(Friendship friendship) {
                        saved = friendship != null;
                    }
                });
    }

    private void sendToFriendsView(){
        FriendsFragment friendsFragment = new FriendsFragment();
        this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.aHomeFragFrame,friendsFragment).commit();
        ((HomeActivity)this.getActivity()).getFriends(false);
    }

}
