package com.codetroopers.makemytrip;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Srujan Jha on 9/25/2016.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.CardViewHolder>{
    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    TextView txtName,txtSource,txtDestination,txtStart,txtEnd;
    CardViewHolder(View convertView) {
        super(convertView);
        txtName = (TextView) convertView.findViewById(R.id.txtEventName);
        txtSource = (TextView) convertView.findViewById(R.id.txtSource);
        txtStart = (TextView) convertView.findViewById(R.id.txtStart);
        txtEnd = (TextView) convertView.findViewById(R.id.txtEnd);
        txtDestination = (TextView) convertView.findViewById(R.id.txtDestination);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

        public interface ClickListener {

            /**
             * Called when the view is clicked.
             *
             * @param v view that is clicked
             * @param position of the clicked item
             * @param isLongClick true if long click, false otherwise
             */
            public void onClick(View v, int position, boolean isLongClick);

        }
        private ClickListener clickListener;

        /* Setter for listener. */
        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }
        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return false;
        }
    }
    ArrayList<Events> eventsItems=new ArrayList<>();
    private static Context mContext;
    public EventAdapter(ArrayList<Events> eventsItems, Context context) {
        this.eventsItems.clear();
        this.eventsItems=eventsItems;
        this.mContext=context;
    }
    private void updateAdapter() {
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return eventsItems.size();
    }
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_row, viewGroup, false);
        CardViewHolder pvh = new CardViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(final CardViewHolder cardViewHolder, final int i) {
        final Events m=eventsItems.get(i);
        cardViewHolder.txtName.setText(m.EventName);
        cardViewHolder.txtSource.setText("Source: "+m.Source);
        cardViewHolder.txtDestination.setText("Destination: "+m.Destination);
        try {
            Calendar cal=Calendar.getInstance();
            cal.setTimeInMillis(m.StartDate);

            cardViewHolder.txtStart.setText("From: " + cal.getTime().toString());
            cal.setTimeInMillis(m.EndDate);

            cardViewHolder.txtEnd.setText("Till: " + cal.getTime().toString());
        }catch(Exception e){}
        cardViewHolder.setClickListener(new CardViewHolder.ClickListener() {
            @Override
            public void onClick(final View v, final int pos, boolean isLongClick) {
                if (isLongClick) {
                } else {
                    Intent it=new Intent(mContext,AddEventActivity.class);
                    it.putExtra("Edit",false);
                    it.putExtra("Events", i);
                    mContext.startActivity(it);
                }
            }
        });
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
