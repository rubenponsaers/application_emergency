package com.example.android.emergency;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.emergency.data.PriorityContactsContract;

/**
 * Created by Ruben on 2/04/2018.
 */

public class PriorityContactsAdapter extends RecyclerView.Adapter<PriorityContactsAdapter.ContactsViewHolder> {
    private Cursor mCursor;
    private static final String TAG = PriorityContactsAdapter.class.getSimpleName();
    private Context mContext;

    public PriorityContactsAdapter(Context context, Cursor cursor){
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutForListItem = R.layout.prioritycontacts_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutForListItem,parent,false);
        ContactsViewHolder viewHolder = new ContactsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        Log.d(TAG, "Contact#" + position);
        if (!mCursor.moveToPosition(position)){
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(PriorityContactsContract.ContactsEntry.COLUMN_NAME));
        long number = mCursor.getLong(mCursor.getColumnIndex(PriorityContactsContract.ContactsEntry.COLUMN_NUMBER));
        long id = mCursor.getLong(mCursor.getColumnIndex(PriorityContactsContract.ContactsEntry._ID));

        holder.nameTextView.setText(name);
        holder.numberTextView.setText("+32" + String.valueOf(number));
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount(){
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if (mCursor != null){mCursor.close();}
        mCursor = newCursor;
        if (newCursor != null){
            this.notifyDataSetChanged();
        }
    }

    //1 item lijst wordt aangemaakt
    class ContactsViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;
        TextView numberTextView;

        public ContactsViewHolder(View itemView){
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.textview_contacts_name);
            numberTextView = (TextView) itemView.findViewById(R.id.textview_contacts_number);
        }
    }
}
