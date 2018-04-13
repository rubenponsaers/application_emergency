package com.example.android.emergency;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.emergency.data.ContactsContract;
import com.example.android.emergency.data.ContactsDbHelper;

import java.math.BigInteger;

public class ContactsActivity extends AppCompatActivity {

    private ContactsAdapter mAdapter;
    private RecyclerView mContactsList;
    private SQLiteDatabase mDb;
    private EditText mNewContactNameEditText;
    private EditText mNewContactNumberEditText;
    private final static String LOG_TAG = ContactsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mContactsList = (RecyclerView) findViewById(R.id.recyclerView_contacts);
        mContactsList.setLayoutManager(new LinearLayoutManager(this));
        mContactsList.setHasFixedSize(true);

        mNewContactNameEditText = (EditText) findViewById(R.id.editText_new_contact_name);
        mNewContactNumberEditText = (EditText) findViewById(R.id.editText_new_contact_phoneNumber);

        ContactsDbHelper dbHelper = new ContactsDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = getAllGuests();
        mAdapter = new ContactsAdapter(this,cursor);
        mContactsList.setAdapter(mAdapter);

        //Item gets deleted
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //Do nothing yet
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                removeContact(id);
                mAdapter.swapCursor(getAllGuests());
            }
        }).attachToRecyclerView(mContactsList);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        if(id==R.id.action_settings){
            Intent startSettingsAvtivity = new Intent(this,SettingsActivity.class);
            startActivity(startSettingsAvtivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //What to show from
    private Cursor getAllGuests() {
        return mDb.query(
                ContactsContract.ContactsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ContactsContract.ContactsEntry.COLUMN_PRIORITY
        );
    }

    //add new contact
    public void addToContacts(View view){
        if(mNewContactNumberEditText.getText().length()==0||mNewContactNameEditText.getText().length()==0){
            /**Context context = ContactsActivity.this;
            Toast messageToast = Toast.makeText(context, "Please, fill in everything!", Toast.LENGTH_SHORT);
            messageToast.show();*/
            return;
        }
        long number = 1;

        // TODO(1) Priority is voorlopig altijd 1, maak een functie of zoek een methode waardoor de priority reeks doorlopend is (1,2,3,4)
        int priority = 1;

        try {
            number = Long.parseLong(mNewContactNumberEditText.getText().toString());
            // TODO(2) Wanneer de nummer 0471... wordt meegegevens krijg je 471.. in de datbase

        }catch (Exception ex){
            Log.e(LOG_TAG, "Failed to parse priority and number:  " + ex.getMessage());
        }

        addNewContact(priority, mNewContactNameEditText.getText().toString(),number);
        mAdapter.swapCursor(getAllGuests());
        mNewContactNameEditText.getText().clear();
        mNewContactNumberEditText.getText().clear();
    }

    //add contact
    private long addNewContact(int priority, String name,Long number){
        ContentValues contentValues = new ContentValues();

        contentValues.put(ContactsContract.ContactsEntry.COLUMN_PRIORITY, priority);
        contentValues.put(ContactsContract.ContactsEntry.COLUMN_NAME,name);
        contentValues.put(ContactsContract.ContactsEntry.COLUMN_NUMBER,number);

        return mDb.insert(ContactsContract.ContactsEntry.TABLE_NAME,null,contentValues);
    }

    //delete contact
    private boolean removeContact(long id){
        return mDb.delete(ContactsContract.ContactsEntry.TABLE_NAME,ContactsContract.ContactsEntry._ID+ "=" + id,null)>0;
    }
}
