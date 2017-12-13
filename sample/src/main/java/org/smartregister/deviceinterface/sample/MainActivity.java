package org.smartregister.deviceinterface.sample;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.smartregister.deviceinterface.DeviceInterfaceLibrary;
import org.smartregister.deviceinterface.domain.Bpm;
import org.smartregister.deviceinterface.domain.BpmWrapper;
import org.smartregister.deviceinterface.fragment.BpmDialogFragment;
import org.smartregister.deviceinterface.fragment.RecordBpmDialogFragment;
import org.smartregister.deviceinterface.listener.BpmActionListener;
import org.smartregister.deviceinterface.repository.BpmRepository;
import org.smartregister.util.Utils;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BpmActionListener {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private static final String DIALOG_TAG = "DIALOG_TAG_DUUH";
    private String entityId = "1";
    private double birthBpm = 3.3d;
    private String dobString = "2016-06-10T00:00:00.000Z";
    private Float sistole, diastole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recordBpm = findViewById(R.id.record_bpm);
        recordBpm.setClickable(true);
        recordBpm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBpmDialog(view);
            }
        });


        ImageButton growthChartButton = (ImageButton) findViewById(R.id.growth_chart_button);
        growthChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startAsyncTask(new ShowBpmChartTask(), null);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showBpmDialog(View view) {
        BpmWrapper bpmWrapper = (BpmWrapper) view.getTag();
        RecordBpmDialogFragment recordBpmDialogFragment = RecordBpmDialogFragment.newInstance(bpmWrapper);
        recordBpmDialogFragment.show(initFragmentTransaction(), DIALOG_TAG);

    }

    private class ShowBpmChartTask extends AsyncTask<Void, Void, List<Bpm>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Bpm> doInBackground(Void... params) {
            BpmRepository bpmRepository = DeviceInterfaceLibrary.getInstance().bpmRepository();
            List<Bpm> allBpms = bpmRepository.findByEntityId(entityId);
            try {
                DateTime dateTime = new DateTime(dobString);
                Bpm bpm = new Bpm(-1l, null, (float) sistole, (float) diastole, dateTime.toDate(), null, null, null, Calendar.getInstance().getTimeInMillis(), null, null, 0);
                allBpms.add(bpm);
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            return allBpms;
        }

        @Override
        protected void onPostExecute(List<Bpm> allBpms) {
            super.onPostExecute(allBpms);

            BpmDialogFragment growthDialogFragment = BpmDialogFragment.newInstance(null, allBpms);
            growthDialogFragment.show(initFragmentTransaction(), DIALOG_TAG);
        }
    }

    private FragmentTransaction initFragmentTransaction() {
        FragmentTransaction ft = this.getFragmentManager().beginTransaction();
        Fragment prev = this.getFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        return ft;
    }

    //Bpm Listener

    @Override
    public void onBpmTaken(BpmWrapper bpmWrapper) {
        Toast.makeText(this, bpmWrapper.getBpm() + " Taken", Toast.LENGTH_SHORT).show();
    }
}
