package com.example.admin.finalproject.helpers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.finalproject.R;
import com.example.admin.finalproject.entities.Event;

import java.util.ArrayList;

/**
 * Created by admin on 10/28/2016.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{

    private ArrayList<Event> mArrayList;

    public EventAdapter(ArrayList<Event> mArrayList) {
        this.mArrayList = mArrayList;
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

        TextView textViewName = holder.textViewName;
//        textViewName.setText(Event.getTitle());
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
        public final TextView textViewName;
        public Event event;
        public int position;

        public ViewHolder(final View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.r_item_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        }
    }
}