package com.example.admin.finalproject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.admin.finalproject.entities.Event;
import com.example.admin.finalproject.entities.Friendship;
import com.example.admin.finalproject.helpers.FriendsAdapter;
import com.example.admin.finalproject.helpers.UsersDialogAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/1/2016.
 */

public class UsersFragmentDialog extends DialogFragment {

    private static final String TAG = "TAG_";
    private RecyclerView mRecyclerView;
    private UsersDialogAdapter usersDialogAdapter;
    private ArrayList<Friendship> mFriendsArrayList;



    // this method create view for your Dialog
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        //inflate layout with recycler view
//        View v = inflater.inflate(R.layout.fragment_friends, container, false);
//        mRecyclerView = (RecyclerView) v.findViewById(R.id.fFriendsRecycler);
//        mFriendsArrayList = this.getArguments().getParcelableArrayList("FRIENDSHIP_LIST");
//        usersDialogAdapter = new UsersDialogAdapter(mFriendsArrayList);
//        mRecyclerView.setAdapter(usersDialogAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
//        usersDialogAdapter.notifyDataSetChanged();
//        //get your recycler view and populate it.
//        return v;
//    }

    @Override
    public void onStart() {
        super.onStart();
        int dialogWidth = WindowManager.LayoutParams.MATCH_PARENT;
        int dialogHeight = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater i = getActivity().getLayoutInflater();

        View v = i.inflate(R.layout.fragment_friends,null,false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fFriendsRecycler);
        mFriendsArrayList = this.getArguments().getParcelableArrayList("FRIENDSHIP_LIST");
        Log.d(TAG, "onCreateDialog: " + mFriendsArrayList);
        Event event = this.getArguments().getParcelable("CURRENT_EVENT");
        usersDialogAdapter = new UsersDialogAdapter(mFriendsArrayList, event);
        mRecyclerView.setAdapter(usersDialogAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        usersDialogAdapter.notifyDataSetChanged();
        AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity(),R.style.AppTheme_Dark)
                .setTitle("Your Friends")
                .setView(v)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );
        return b.create();
    }
}
