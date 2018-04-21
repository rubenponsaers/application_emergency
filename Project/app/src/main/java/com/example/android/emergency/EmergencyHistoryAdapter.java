package com.example.android.emergency;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.emergency.data.ContactsContract;
import com.example.android.emergency.data.EmergencyHistoryContract;

/**
 * Created by Ruben on 7/03/2018.
 */

public class EmergencyHistoryAdapter extends RecyclerView.Adapter<EmergencyHistoryAdapter.EmergencyHistoryViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    private static final String TAG = EmergencyHistoryAdapter.class.getSimpleName();

    public EmergencyHistoryAdapter(Context context, Cursor cursor){
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public EmergencyHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutForListItem = R.layout.emergencyhistory_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutForListItem,parent,false);
        EmergencyHistoryViewHolder viewHolder = new EmergencyHistoryViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmergencyHistoryViewHolder holder, int position) {
        Log.d(TAG, "Emergency#" + position);

        if(!mCursor.moveToPosition(position)){
            return;
        }

        String date = mCursor.getString(mCursor.getColumnIndex(EmergencyHistoryContract.EmergencyHistoryEntry.COLUMN_DATE));
        String location = mCursor.getString(mCursor.getColumnIndex(EmergencyHistoryContract.EmergencyHistoryEntry.COLUMN_LOCATION));
        long id = mCursor.getLong(mCursor.getColumnIndex(EmergencyHistoryContract.EmergencyHistoryEntry._ID));

        holder.dateTextView.setText(date);
        holder.locationTextView.setText(location);
        holder.itemView.setTag(id);

    }

    /**
     * Returns het aantal items dat weergegeven worden in de RecyclerView.
     *
     * @return Het aantal items die weergegeven worden
     */
    @Override
    public int getItemCount(){
        return mCursor.getCount();
    }

    /**
     * Cache voor de children van de recyclerview voor een lijst item/bericht
     */
    class EmergencyHistoryViewHolder extends RecyclerView.ViewHolder{
        TextView dateTextView;
        TextView locationTextView;


        public EmergencyHistoryViewHolder(View itemView){
            super(itemView);

            dateTextView = (TextView) itemView.findViewById(R.id.textview_emergencyhistory_date);
            locationTextView = (TextView) itemView.findViewById(R.id.textview_emergencyhistory_location);
        }
    }
}
