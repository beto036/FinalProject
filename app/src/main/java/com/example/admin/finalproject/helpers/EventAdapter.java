package com.example.admin.finalproject.helpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.finalproject.EventDetailsFragment;
import com.example.admin.finalproject.HomeActivity;
import com.example.admin.finalproject.R;
import com.example.admin.finalproject.entities.Event;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by admin on 10/28/2016.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{

    private static final String TAG = "EventAdapterTAG_";
    private ArrayList<Event> mArrayList;

    public EventAdapter(ArrayList<Event> mArrayList) {
        this.mArrayList = mArrayList;
        Log.d(TAG, "EventAdapter: " + this.mArrayList);
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View termView = inflater.inflate(R.layout.recycler_item, parent, false);

        return new EventAdapter.ViewHolder(termView);
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {
        Event event = mArrayList.get(position);
        Log.d(TAG, "onBindViewHolder: " + event);
        TextView eventTitle = holder.eventTitle;
        TextView eventDesc = holder.eventDesc;
        eventTitle.setText(event.getEvent());
        eventDesc.setText(event.getDescription());
        holder.event = event;
        holder.position = position;
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private static final String TAG = "EventAdapterTAG_";
        public final TextView eventTitle;
        public final TextView eventDesc;
        public final CardView cardView;
        public Event event;
        public int position;

        public ViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            eventTitle = (TextView) itemView.findViewById(R.id.eventTitle);
            eventDesc = (TextView) itemView.findViewById(R.id.eventDesc);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: ");
                    AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                    EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
                    appCompatActivity
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.aHomeFragFrame,eventDetailsFragment)
                            .commit();

//                    EventBus.getDefault().post(event);
                }
            });
        }
    }
}
