package com.example.admin.finalproject.helpers;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.finalproject.App;
import com.example.admin.finalproject.R;
import com.example.admin.finalproject.entities.Friendship;
import com.example.admin.finalproject.entities.User;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by admin on 12/1/2016.
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>{

    private ArrayList<Friendship> mFriendList;

    public FriendsAdapter(ArrayList<Friendship> mFriendList) {
        this.mFriendList = mFriendList;
    }

    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.user_recycler_item, parent, false);
        return new FriendsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendsAdapter.ViewHolder holder, int position) {
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
        holder.position = position;
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
        public User userApp;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userEmail = (TextView) itemView.findViewById(R.id.userEmail);
            cardView = (CardView) itemView.findViewById(R.id.cvUser);
            userApp = ((App)itemView.getContext().getApplicationContext()).getUser();
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }
}
