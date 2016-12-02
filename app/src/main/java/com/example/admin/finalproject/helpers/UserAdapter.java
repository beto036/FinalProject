package com.example.admin.finalproject.helpers;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.finalproject.App;
import com.example.admin.finalproject.EventDetailsFragment;
import com.example.admin.finalproject.R;
import com.example.admin.finalproject.UsersDetailFragment;
import com.example.admin.finalproject.entities.Friendship;
import com.example.admin.finalproject.entities.User;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 11/29/2016.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private ArrayList<User> mArrayList;

    public UserAdapter(ArrayList<User> mArrayList) {
        this.mArrayList = mArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.user_recycler_item, parent, false);
        return new UserAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mArrayList.get(position);
        TextView userName = holder.userName;
        TextView userEmail = holder.userEmail;
        userName.setText(user.getName());
        userEmail.setText(user.getEmail());
        holder.user = user;
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return this.mArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "ViewHolderUsersTAG_";
        public final TextView userName;
        public final TextView userEmail;
        public final CardView cardView;
        public int position;
        public User user;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userEmail = (TextView) itemView.findViewById(R.id.userEmail);
            cardView = (CardView) itemView.findViewById(R.id.cvUser);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getData(v.getContext(),user);
                }
            });
        }

        public static void getData(final Context context, final User user){
            //Make http call to validate friendship or request
            final User userApp = ((App)context.getApplicationContext()).getUser();
            Log.d(TAG, "getData: userApp*****************************************************" + userApp);
            Log.d(TAG, "getData: userApp*****************************************************" + user);
            Observable<List<Friendship>> observable = RetrofitHelper.Factory.getFriends(userApp,user,false);

            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Friendship>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError: " + e.toString());
                        }

                        @Override
                        public void onNext(List<Friendship> friendships) {
                            Bundle bundle = new Bundle();
                            if(!friendships.isEmpty()){
                                Friendship friendship = friendships.get(0);
                                bundle.putParcelable("FRIENDSHIP",friendship);
                                Log.d(TAG, "onNext: "+ friendship.getReceiverId().equals(userApp.getId().get$oid()));
                                if(friendship.getReceiverId().equals(userApp.getId().get$oid()))
                                    bundle.putBoolean("RECEIVER", true);
                                bundle.putBoolean("IS_REQUEST", friendship.getIsRequest());
                                Log.d(TAG, "onNext: " + friendship);
                            }
                            bundle.putParcelable("USER", user);
                            AppCompatActivity appCompatActivity = (AppCompatActivity) context;
                            UsersDetailFragment usersDetailFragment = new UsersDetailFragment();
                            usersDetailFragment.setArguments(bundle);
                            appCompatActivity
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.aHomeFragFrame,usersDetailFragment)
                                    .commit();
                        }
                    });
        }
    }
}
