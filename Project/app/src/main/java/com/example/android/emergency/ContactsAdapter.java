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

/**
 * Created by Ruben on 2/04/2018.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    private Cursor mCursor;
    private static final String TAG = ContactsAdapter.class.getSimpleName();
    private Context mContext;

    public ContactsAdapter(Context context,Cursor cursor){
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutForListItem = R.layout.contacts_list_item;
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

        int priority = mCursor.getInt(mCursor.getColumnIndex(ContactsContract.ContactsEntry.COLUMN_PRIORITY));
        String name = mCursor.getString(mCursor.getColumnIndex(ContactsContract.ContactsEntry.COLUMN_NAME));
        long number = mCursor.getLong(mCursor.getColumnIndex(ContactsContract.ContactsEntry.COLUMN_NUMBER));
        long id = mCursor.getLong(mCursor.getColumnIndex(ContactsContract.ContactsEntry._ID));

        holder.priorityTextView.setText(String.valueOf(priority));
        holder.nameTextView.setText(name);
        holder.numberTextView.setText(String.valueOf(number));
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
        TextView priorityTextView;

        public ContactsViewHolder(View itemView){
            super(itemView);
            priorityTextView = (TextView) itemView.findViewById(R.id.textview_contacts_priority);
            nameTextView = (TextView) itemView.findViewById(R.id.textview_contacts_name);
            numberTextView = (TextView) itemView.findViewById(R.id.textview_contacts_number);
        }
    }
}
