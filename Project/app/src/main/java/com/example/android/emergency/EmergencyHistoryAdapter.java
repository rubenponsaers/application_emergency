package com.example.android.emergency;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ruben on 7/03/2018.
 */

public class EmergencyHistoryAdapter extends RecyclerView.Adapter<EmergencyHistoryAdapter.EmergencyHistoryViewHolder> {
    private int mEmergencyItems;
    private static final String TAG = EmergencyHistoryAdapter.class.getSimpleName();

    /**
     * Constructor voor een EmergencyHistoryAdapter die een aantal items accepteert om weer te geven
     *
     * @param numberItems Het aantal items die weergegeven moeten worden in de lijst
     */
    public EmergencyHistoryAdapter(int numberItems){
        mEmergencyItems = numberItems;
    }

    /**
     *
     * Wordt opgeroepen wanneer een nieuwe ViewHolder wordt gecreerd. Dit gebeurt wanneer er door
     * de RecyclerView wordt gescrold. Er zullen net genoeg viewHolders gemaakt worden zodat het
     * scherm gevuld is
     *
     * @param parent    De viewGroup die deze Viewholder bevat
     * @param viewType  Als de RecyclerView meer Items heeft kan dit gebruikt worden voor
     *                  een andere Layout
     *                  
     * @return A new NumberViewHolder that holds the View for each list item
     */
    @Override
    public EmergencyHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForListItem = R.layout.emergencyhistory_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForListItem,parent,shouldAttachToParentImmediately);
        EmergencyHistoryViewHolder viewHolder = new EmergencyHistoryViewHolder(view);

        return viewHolder;
    }

    /**
     * Wordt opgeroepen door de RecyclerView om de data weer te geven die hoort bij die positie
     *
     * @param holder   De ViewHolder die geupdate moet worden om de gegeven data weer te geven
     * @param position De postitie van het item
     */
    @Override
    public void onBindViewHolder(EmergencyHistoryViewHolder holder, int position) {
        Log.d(TAG, "Emergency#" + position);
        holder.bind(position);
    }

    /**
     * Returns het aantal items dat weergegeven worden in de RecyclerView.
     *
     * @return Het aantal items die weergegeven worden
     */
    @Override
    public int getItemCount(){
        return mEmergencyItems;
    }

    /**
     * Cache voor de children van de recyclerview voor een lijst item/bericht
     */
    class EmergencyHistoryViewHolder extends RecyclerView.ViewHolder{
        TextView listEmergencyView;

        /**
         * Constructor voor de viewHolder. Binnen deze constructor krijgen we een referentie naar onze
         * TextViews in de lijst
         *
         * @param itemView De view die we nodig hebben
         */
        public EmergencyHistoryViewHolder(View itemView){
            super(itemView);

            listEmergencyView = (TextView) itemView.findViewById(R.id.textview_message_item);
        }

        /**
         * Methode die de tekst instelt van het weergegeven item
         *
         * @param listIndex De positie van het item in de lijst
         */
        void bind (int listIndex){
            listEmergencyView.setText("Date: " + String.valueOf(listIndex));
        }
    }
}
