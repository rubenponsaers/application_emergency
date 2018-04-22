package com.example.android.emergency;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.emergency.data.MyContactsContract;

/**
 * Created by Ruben on 2/04/2018.
 */

public class MyContactsAdapter extends RecyclerView.Adapter<MyContactsAdapter.MyContactsViewHolder> {

    private static final String TAG = MyContactsAdapter.class.getSimpleName();
    private Cursor cursor;
    private Context context;

    public MyContactsAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public MyContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutListItem = R.layout.mycontacts_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutListItem,parent,false);

        MyContactsViewHolder viewHolder = new MyContactsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyContactsViewHolder holder, int position) {
        Log.d(TAG, "Contact#" + position);

        if (!cursor.moveToPosition(position)){
            return;
        }

        String name = cursor.getString(cursor.getColumnIndex(MyContactsContract.MyContactsEntry.COLUMN_NAME));
        String number = cursor.getString(cursor.getColumnIndex(MyContactsContract.MyContactsEntry.COLUMN_NUMBER));
        long id = cursor.getLong(cursor.getColumnIndex(MyContactsContract.MyContactsEntry._ID));

        holder.name.setText(name);
        holder.number.setText("+32" + number);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount(){
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if (cursor != null){
            cursor.close();}
        cursor = newCursor;
        if (newCursor != null){
            this.notifyDataSetChanged();
        }
    }

    //1 item lijst wordt aangemaakt
    class MyContactsViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView number;

        public MyContactsViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textview_contacts_name);
            number = (TextView) itemView.findViewById(R.id.textview_contacts_number);
        }
    }
}
