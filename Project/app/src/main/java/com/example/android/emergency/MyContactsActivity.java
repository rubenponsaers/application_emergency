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

import com.example.android.emergency.data.MyContactsContract;
import com.example.android.emergency.data.MyContactsDBHelper;

public class MyContactsActivity extends AppCompatActivity {

    private final static String TAG = MyContactsActivity.class.getSimpleName();
    private Button addExistingContact;
    private Button addNewContact;
    private EditText newContactName;
    private EditText newContactNumber;
    private MyContactsAdapter myContactsAdapter;
    private RecyclerView myContactsList;
    private SQLiteDatabase sqLiteDatabase;
    private MyContactsDBHelper myContactsDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycontacts);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        myContactsList = (RecyclerView) findViewById(R.id.recyclerView_mycontacts);
        myContactsList.setLayoutManager(new LinearLayoutManager(this));
        myContactsList.setHasFixedSize(true);

        addExistingContact = (Button) findViewById(R.id.button_add_existing_contact);
        addNewContact = (Button) findViewById(R.id.button_add_new_contact);
        newContactName = (EditText) findViewById(R.id.editText_new_contact_name);
        newContactNumber = (EditText) findViewById(R.id.editText_new_contact_phoneNumber);

        myContactsDBHelper = new MyContactsDBHelper(this);
        sqLiteDatabase = myContactsDBHelper.getWritableDatabase();
        myContactsAdapter = new MyContactsAdapter(this,showAll());
        myContactsList.setAdapter(myContactsAdapter);

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
                myContactsAdapter.swapCursor(showAll());
            }
        }).attachToRecyclerView(myContactsList);

        addNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToContacts();
            }
        });
        addExistingContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 2:
                    contactPicked(data);
                    break;
            }
        }else {
            Log.e(TAG, "Failed to pick contact from phone");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try{
            String number = null;
            String name = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri,null,null,null,null);
            cursor.moveToFirst();

            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            number = cursor.getString(numberIndex);
            name = cursor.getString(nameIndex);
            addNewContact(name,number);
            myContactsAdapter.swapCursor(showAll());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //add new contact
    public void addToContacts(){
        if(newContactNumber.getText().length()==0|| newContactName.getText().length()==0){
            Toast messageToast = Toast.makeText(MyContactsActivity.this, "Please, fill in everything!", Toast.LENGTH_SHORT);
            messageToast.show();
            return;
        }
        addNewContact(newContactName.getText().toString(), newContactNumber.getText().toString());
        myContactsAdapter.swapCursor(showAll());
        newContactName.getText().clear();
        newContactNumber.getText().clear();
    }

    public long addNewContact(String name,String number){
        ContentValues contentValues = new ContentValues();

        contentValues.put(MyContactsContract.MyContactsEntry.COLUMN_NAME,name);
        contentValues.put(MyContactsContract.MyContactsEntry.COLUMN_NUMBER,number);

        return sqLiteDatabase.insert(MyContactsContract.MyContactsEntry.TABLE_NAME,null,contentValues);
    }

    private boolean removeContact(long id){
        Log.e(TAG, "Contact removed");
        return sqLiteDatabase.delete(MyContactsContract.MyContactsEntry.TABLE_NAME, MyContactsContract.MyContactsEntry._ID+ "=" + id,null)>0;
    }

    private Cursor showAll() {
        return sqLiteDatabase.query(
                MyContactsContract.MyContactsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MyContactsContract.MyContactsEntry._ID
        );
    }
}
