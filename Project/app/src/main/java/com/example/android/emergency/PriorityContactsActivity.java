package com.example.android.emergency;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
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

import com.example.android.emergency.data.PriorityContactsContract;
import com.example.android.emergency.data.PriorityContactsDbHelper;

public class PriorityContactsActivity extends AppCompatActivity {

    private Button addExistingContact;
    private PriorityContactsAdapter mAdapter;
    private RecyclerView mContactsList;
    private SQLiteDatabase mDb;
    private EditText mNewContactNameEditText;
    private EditText mNewContactNumberEditText;
    private final static String TAG = PriorityContactsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prioritycontacts);

        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mContactsList = (RecyclerView) findViewById(R.id.recyclerView_contacts);
        mContactsList.setLayoutManager(new LinearLayoutManager(this));
        mContactsList.setHasFixedSize(true);

        mNewContactNameEditText = (EditText) findViewById(R.id.editText_new_contact_name);
        mNewContactNumberEditText = (EditText) findViewById(R.id.editText_new_contact_phoneNumber);

        PriorityContactsDbHelper dbHelper = new PriorityContactsDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = getAllGuests();
        mAdapter = new PriorityContactsAdapter(this,cursor);
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

        addExistingContact = (Button) findViewById(R.id.button_add_existing_contact);
        addExistingContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 1:
                    contactPicked(data);
                    break;
            }
        }else {
            Log.e("PContactsActivity", "Failed to pick contact from phone");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try{
            String number = "12";
            String name = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri,null,null,null,null);
            cursor.moveToFirst();

            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            number = cursor.getString(numberIndex);
            name = cursor.getString(nameIndex);
            addNewContact(name,number);
            mAdapter.swapCursor(getAllGuests());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(true);
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



    //add new contact
    public void addToContacts(View view){
        if(mNewContactNumberEditText.getText().length()==0||mNewContactNameEditText.getText().length()==0){
            Context context = PriorityContactsActivity.this;
            Toast messageToast = Toast.makeText(context, "Please, fill in everything!", Toast.LENGTH_SHORT);
            messageToast.show();
            return;
        }
        addNewContact(mNewContactNameEditText.getText().toString(),mNewContactNumberEditText.getText().toString());
        mAdapter.swapCursor(getAllGuests());
        mNewContactNameEditText.getText().clear();
        mNewContactNumberEditText.getText().clear();
    }

    public long addNewContact(String name,String number){
        ContentValues contentValues = new ContentValues();

        contentValues.put(PriorityContactsContract.ContactsEntry.COLUMN_NAME,name);
        contentValues.put(PriorityContactsContract.ContactsEntry.COLUMN_NUMBER,number);

        return mDb.insert(PriorityContactsContract.ContactsEntry.TABLE_NAME,null,contentValues);
    }

    private boolean removeContact(long id){
        return mDb.delete(PriorityContactsContract.ContactsEntry.TABLE_NAME, PriorityContactsContract.ContactsEntry._ID+ "=" + id,null)>0;
    }

    private Cursor getAllGuests() {
        return mDb.query(
                PriorityContactsContract.ContactsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                PriorityContactsContract.ContactsEntry._ID
        );
    }
}
