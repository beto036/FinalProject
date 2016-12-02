package com.example.admin.finalproject.helpers;

import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.finalproject.App;
import com.example.admin.finalproject.R;
import com.example.admin.finalproject.entities.Event;
import com.example.admin.finalproject.entities.Friendship;
import com.example.admin.finalproject.entities.Invitation;
import com.example.admin.finalproject.entities.User;
import com.example.admin.finalproject.entities.notification.Data;
import com.example.admin.finalproject.entities.notification.Notification;
import com.example.admin.finalproject.entities.notification.SendNotificationRequest;
import com.example.admin.finalproject.entities.notification.SendNotificationResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 12/1/2016.
 */

public class UsersDialogAdapter extends RecyclerView.Adapter<UsersDialogAdapter.ViewHolder>{

    private ArrayList<Friendship> mFriendList;
    private Event event;

    public UsersDialogAdapter(ArrayList<Friendship> mFriendList, Event event) {
        this.mFriendList = mFriendList;
        this.event = event;
    }

    @Override
    public UsersDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.user_recycler_item, parent, false);
        return new UsersDialogAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UsersDialogAdapter.ViewHolder holder, int position) {
        Friendship friendship = mFriendList.get(position);
        TextView userName = holder.userName;
        TextView userEmail = holder.userEmail;
        if(holder.userApp.getId().get$oid().equals(friendship.getReceiverId())) {
            userName.setText(friendship.getNameSender());
            userEmail.setText(friendship.getEmailSender());
        }else {
            userName.setText(friendship.getNameReceiver());
            userEmail.setText(friendship.getEmailReceiver());
        }
        holder.friendship = friendship;
        holder.event = this.event;
        holder.position = position;
        holder.context = this;
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "ViewHolderUsersTAG_";
        public final TextView userName;
        public final TextView userEmail;
        public final CardView cardView;
        public int position;
        public Friendship friendship;
        public Event event;
        public User userApp;
        private boolean saved;
        private UsersDialogAdapter context;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userEmail = (TextView) itemView.findViewById(R.id.userEmail);
            cardView = (CardView) itemView.findViewById(R.id.cvUser);
            userApp = ((App)itemView.getContext().getApplicationContext()).getUser();
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setEnabled(false);
                    Invitation invitation = new Invitation();
                    invitation.setConfirmed(false);
                    if(userApp.getId().get$oid().equals(friendship.getReceiverId())){
                        invitation.setEmail(friendship.getEmailSender());
                        invitation.setUserId(friendship.getSenderId());
                        invitation.setName(friendship.getNameSender());
                        invitation.setLastname(friendship.getLastnameSender());
                    }else{
                        invitation.setEmail(friendship.getEmailReceiver());
                        invitation.setUserId(friendship.getReceiverId());
                        invitation.setName(friendship.getNameReceiver());
                        invitation.setLastname(friendship.getLastnameReceiver());
                    }
                    invitation.setEventDesc(event.getDescription());
                    invitation.setEventId(event.getId().get$oid());
                    invitation.setEventTitle(event.getEvent());
                    invitation.setEventTime(event.getDate());
                    invitation.setLongitude(event.getLongitude());
                    invitation.setLatitude(event.getLatitude());
                    invitation.setDeclined(false);
                    saveAndSendNotification(invitation);
                }
            });
        }
        private void saveAndSendNotification(final Invitation invitation){
            Observable<Invitation> observable = RetrofitHelper.Factory.insertInvitation(invitation);
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Invitation>() {
                        @Override
                        public void onCompleted() {
                            if(saved){
                                getUser(invitation);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError: " + e.toString());
                        }

                        @Override
                        public void onNext(Invitation invitation) {
                            saved = invitation != null;
                        }
                    });
        }

        private void sendNotification(User user){
            SendNotificationRequest sendNotificationRequest = new SendNotificationRequest();
            sendNotificationRequest.setTo(user.getDeviceToken());
            Data data = new Data();
            data.setScore("654654");
            Notification notification = new Notification();
            notification.setTitle("New Event Invitation");
            sendNotificationRequest.setData(data);
            sendNotificationRequest.setNotification(notification);

            Call<SendNotificationResponse> call = NotificationRetrofitHelper.Factory.sendFriendRequest(sendNotificationRequest);

            call.enqueue(new Callback<SendNotificationResponse>() {
                @Override
                public void onResponse(Call<SendNotificationResponse> call, Response<SendNotificationResponse> response) {
                    SendNotificationResponse sendNotificationResponse = response.body();
                    Log.d(TAG, "onResponse: " + sendNotificationResponse);
                    context.mFriendList.remove(position);
                    context.notifyItemRemoved(position);
                }

                @Override
                public void onFailure(Call<SendNotificationResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.toString());
                }
            });
        }

        private void getUser(Invitation invitation){
            Observable<User> observable = RetrofitHelper.Factory.getUserById(invitation.getUserId());
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<User>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError: " + e.toString());
                        }

                        @Override
                        public void onNext(User user) {
                            sendNotification(user);
                        }
                    });
        }
    }


}
