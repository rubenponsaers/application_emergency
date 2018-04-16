package com.example.android.emergency;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.emergency.data.EmergencyHistoryContract;
import com.example.android.emergency.data.EmergencyHistoryDBHelper;
import com.example.android.emergency.data.EmergencyPreferences;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Object> {

    private Button emergencyHistory;
    private Button sendEmergency;
    private Button contacts;
    private TextView ownerName;
    private TextView ownerBloodType;

    private SQLiteDatabase mDb;

    private final static String TAG = EmergencyHistoryActivity.class.getSimpleName();
    private static final int LOADER_ID = 0;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmergencyHistoryDBHelper dbHelper = new EmergencyHistoryDBHelper(this);
        mDb = dbHelper.getWritableDatabase();

        emergencyHistory = (Button) findViewById(R.id.button_emergencyHistory);
        sendEmergency = (Button) findViewById(R.id.button_sendEmergency);
        contacts = (Button) findViewById(R.id.button_contacts);
        ownerName = (TextView) findViewById(R.id.textView_ownerName);
        ownerBloodType = (TextView) findViewById(R.id.textView_ownerBloodType);

        //Button actions
        emergencyHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = EmergencyHistoryActivity.class;
                Intent startActivity = new Intent(context,destinationActivity);
                startActivity(startActivity);
            }
        });
        sendEmergency.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Toast messageToast = Toast.makeText(context, "Sending emergency message...", Toast.LENGTH_SHORT);
                messageToast.show();
                addNewEmergency();
            }
        });
        contacts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                Class destinationActivity = ContactsActivity.class;
                Intent startActivity = new Intent(context,destinationActivity);
                startActivity(startActivity);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        String name = EmergencyPreferences.getPrefName();
        String bloodtype = EmergencyPreferences.getPrefBloodtype();
        ownerName.setText(name);
        ownerBloodType.setText(bloodtype);


        if(PREFERENCES_HAVE_BEEN_UPDATED){
            Log.d(TAG,"onStart: preferences were updated");
            getSupportLoaderManager().restartLoader(LOADER_ID,null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    public long addNewEmergency(){
        String date = "16/04/2018";
        String location = "Tongeren";

        ContentValues contentValues = new ContentValues();

        contentValues.put(EmergencyHistoryContract.EmergencyHistoryEntry.COLUMN_DATE,date);
        contentValues.put(EmergencyHistoryContract.EmergencyHistoryEntry.COLUMN_LOCATION, location);

        return mDb.insert(EmergencyHistoryContract.EmergencyHistoryEntry.TABLE_NAME,null,contentValues);
        //TODO(3) Vraagt locatie en datum op en voegt dit toe aan de database
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings){
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        return null;
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context, * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Object> loader) {
    }
}
