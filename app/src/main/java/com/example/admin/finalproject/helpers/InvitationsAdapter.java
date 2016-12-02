package com.example.admin.finalproject.helpers;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.finalproject.App;
import com.example.admin.finalproject.R;
import com.example.admin.finalproject.entities.Event;
import com.example.admin.finalproject.entities.Friendship;
import com.example.admin.finalproject.entities.Invitation;
import com.example.admin.finalproject.entities.User;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 12/1/2016.
 */
public class InvitationsAdapter extends RecyclerView.Adapter<InvitationsAdapter.ViewHolder>{

    private ArrayList<Invitation> mFriendList;

    public InvitationsAdapter(ArrayList<Invitation> mFriendList) {
        this.mFriendList = mFriendList;
    }

    @Override
    public InvitationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.invitation_recycler_item, parent, false);
        return new InvitationsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InvitationsAdapter.ViewHolder holder, int position) {
        Invitation invitation = mFriendList.get(position);
        TextView title = holder.title;
        TextView description = holder.description;
        title.setText(invitation.getEventTitle());
        description.setText(invitation.getEventDesc());
        holder.invitation = invitation;
        holder.position = position;
        holder.context = this;
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "ViewHolderUsersTAG_";
        public final TextView title;
        public final TextView description;
        public final CardView cardView;
        public final Button accept;
        public final Button decline;
        public int position;
        public Invitation invitation;
        public InvitationsAdapter context;
        public User userApp;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.invitationTitle);
            description = (TextView) itemView.findViewById(R.id.invitationDescription);
            cardView = (CardView) itemView.findViewById(R.id.cvInvitation);
            accept = (Button) itemView.findViewById(R.id.acceptBtn);
            decline = (Button) itemView.findViewById(R.id.declineBtn);
            userApp = ((App)itemView.getContext().getApplicationContext()).getUser();
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: " + invitation.toString());
                    invitation.setConfirmed(true);
                    updateInvitation();
                    context.mFriendList.remove(position);
                    context.notifyItemRemoved(position);
                }
            });

            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invitation.setDeclined(true);
                    updateInvitation();
                    context.mFriendList.remove(position);
                    context.notifyItemRemoved(position);
                }
            });
        }

        public void updateInvitation(){
            Log.d(TAG, "updateInvitation: " + invitation);
            Observable<Invitation> observable = RetrofitHelper.Factory.updateInvitation(invitation);
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Invitation>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError: " + e.toString());
                        }

                        @Override
                        public void onNext(Invitation invitation) {
                            Log.d(TAG, "onNext: " + invitation.toString());
                            Event event = new Event();
                            event.setIsAdmin(false);
                            event.setUserId(userApp.getId().get$oid());
                            event.setEvent(invitation.getEventTitle());
                            event.setDescription(invitation.getEventDesc());
                            event.setDate(invitation.getEventTime());
                            event.setLatitude(invitation.getLatitude());
                            event.setLongitude(invitation.getLongitude());
                            insertEvent(event);
                        }
                    });
        }

        public void insertEvent(Event event){
            Observable<Event> observable = RetrofitHelper.Factory.insertEvent(event);
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Event>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError: " + e.toString());
                        }

                        @Override
                        public void onNext(Event event) {
                            Log.d(TAG, "onNext: inserted event"+ event.toString());
                        }
                    });
        }
    }
}
