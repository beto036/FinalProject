package com.example.admin.finalproject.helpers;

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
public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.ViewHolder>{

    private ArrayList<Friendship> mFriendList;

    public FriendRequestsAdapter(ArrayList<Friendship> mFriendList) {
        this.mFriendList = mFriendList;
    }

    @Override
    public FriendRequestsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.requests_recycler_item, parent, false);
        return new FriendRequestsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendRequestsAdapter.ViewHolder holder, int position) {
        Friendship friendship = mFriendList.get(position);
        TextView name = holder.name;
        TextView email = holder.email;
        name.setText(friendship.getNameSender() + " " + friendship.getLastnameSender());
        email.setText(friendship.getEmailSender());
        holder.friendship = friendship;
        holder.position = position;
        holder.context = this;
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "ViewHolderUsersTAG_";
        public final TextView name;
        public final TextView email;
        public final CardView cardView;
        public final Button accept;
        public final Button decline;
        public int position;
        public Friendship friendship;
        public FriendRequestsAdapter context;
        public User userApp;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.reqestName);
            email = (TextView) itemView.findViewById(R.id.requestEmail);
            cardView = (CardView) itemView.findViewById(R.id.cvRequest);
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
                    Log.d(TAG, "onClick: " + friendship.toString());
                    friendship.setIsRequest(false);
                    updateFriendship();
                    context.mFriendList.remove(position);
                    context.notifyItemRemoved(position);
                }
            });

            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friendship.setDeclined(true);
                    updateFriendship();
                    context.mFriendList.remove(position);
                    context.notifyItemRemoved(position);
                }
            });
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
    }
}
