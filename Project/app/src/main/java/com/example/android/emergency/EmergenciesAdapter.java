package com.example.android.emergency;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.emergency.data.EmergenciesContract;

import java.text.DecimalFormat;

/**
 * Created by Ruben on 7/03/2018.
 */

public class EmergenciesAdapter extends RecyclerView.Adapter<EmergenciesAdapter.EmergenciesViewHolder> {

    private static final String TAG = EmergenciesAdapter.class.getSimpleName();
    private Cursor cursor;
    private Context context;
    private DecimalFormat df6 = new DecimalFormat(".######");

    public EmergenciesAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public EmergenciesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutListItem = R.layout.emergencies_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutListItem,parent,false);

        EmergenciesViewHolder viewHolder = new EmergenciesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EmergenciesViewHolder holder, int position) {
        Log.d(TAG, "Emergency#" + position);

        if(!cursor.moveToPosition(position)){
            return;
        }

        String date = cursor.getString(cursor.getColumnIndex(EmergenciesContract.EmergenciesEntry.COLUMN_DATE));
        String time = cursor.getString(cursor.getColumnIndex(EmergenciesContract.EmergenciesEntry.COLUMN_TIME));
        Double latitude = cursor.getDouble(cursor.getColumnIndex(EmergenciesContract.EmergenciesEntry.COLUMN_LATITUDE));
        Double longitude = cursor.getDouble(cursor.getColumnIndex(EmergenciesContract.EmergenciesEntry.COLUMN_LONGITUDE));
        long id = cursor.getLong(cursor.getColumnIndex(EmergenciesContract.EmergenciesEntry._ID));

        holder.date.setText(date);
        holder.time.setText(time);
        holder.latitude.setText("Latitude: " + String.valueOf(df6.format(latitude)));
        holder.longitude.setText("Longitude: " + String.valueOf(df6.format(longitude)));
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount(){
        return cursor.getCount();
    }

    class EmergenciesViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView latitude;
        TextView longitude;
        TextView time;


        public EmergenciesViewHolder(View itemView){
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.textview_emergencies_date);
            time = (TextView) itemView.findViewById(R.id.textview_emergencies_time);
            latitude = (TextView) itemView.findViewById(R.id.textview_emergencies_latitude);
            longitude = (TextView) itemView.findViewById(R.id.textview_emergencies_longitude);
        }
    }
}
