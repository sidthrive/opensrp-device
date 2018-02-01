package org.smartregister.deviceinterface.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
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
import org.smartregister.deviceinterface.listener.BpmActionListener;
import org.smartregister.deviceinterface.repository.BpmRepository;
import org.smartregister.deviceinterface.sample.utils.SampleUtils;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.util.DateUtil;
import org.smartregister.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BpmActionListener {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private static final String DIALOG_TAG = "MY_TAG";

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

    @Override
    protected void onResume() {
        super.onResume();
        View recordBpm = findViewById(R.id.record_bpm);
        recordBpm.setClickable(true);
        recordBpm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SampleUtils.showBpmDialog(MainActivity.this, view, DIALOG_TAG);
            }
        });


        ImageButton btnStartBPM = (ImageButton) findViewById(R.id.bpm_button);
        btnStartBPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startAsyncTask(new ShowBpmChartTask(), null);
            }
        });

//        listBpmLayout();
    }

    private void listBpmLayout() {
        View weightWidget = findViewById(R.id.weight_widget);

        LinkedHashMap<Long, Pair<String, String>> weightmap = new LinkedHashMap<>();
        ArrayList<Boolean> weighteditmode = new ArrayList<Boolean>();
        ArrayList<View.OnClickListener> listeners = new ArrayList<>();

        BpmRepository br = DeviceInterfaceLibrary.getInstance().bpmRepository();
        List<Bpm> bpmList = br.findLast5(SampleUtils.ENTITY_ID);

        for (int i = 0; i < bpmList.size(); i++) {
            Bpm bpm = bpmList.get(i);
            String formattedAge = "";
            if (bpm.getDate() != null) {

                Date weighttaken = bpm.getDate();
                DateTime birthday = new DateTime(SampleUtils.STR_DOB);
                Date birth = birthday.toDate();
                long timeDiff = weighttaken.getTime() - birth.getTime();
                Log.v("timeDiff is ", timeDiff + "");
                if (timeDiff >= 0) {
                    formattedAge = DateUtil.getDuration(timeDiff);
                    Log.v("age is ", formattedAge);
                }
            }
            if (!formattedAge.equalsIgnoreCase("0d")) {
                weightmap.put(bpm.getId(), Pair.create(formattedAge, Utils.kgStringSuffix(bpm.getSistole())));

                ////////////////////////check 3 months///////////////////////////////
                boolean less_than_three_months_event_created = false;

                org.smartregister.domain.db.Event event = null;
                EventClientRepository db = DeviceInterfaceLibrary.getInstance().eventClientRepository();
                if (bpm.getEventId() != null) {
                    event = db.convert(db.getEventsByEventId(bpm.getEventId()), org.smartregister.domain.db.Event.class);
                } else if (bpm.getFormSubmissionId() != null) {
                    event = db.convert(db.getEventsByFormSubmissionId(bpm.getFormSubmissionId()), org.smartregister.domain.db.Event.class);
                }
                if (event != null) {
                    Date weight_create_date = event.getDateCreated().toDate();
                    if (!DateUtil.checkIfDateThreeMonthsOlder(weight_create_date)) {
                        less_than_three_months_event_created = true;
                    }
                } else {
                    less_than_three_months_event_created = true;
                }
                ///////////////////////////////////////////////////////////////////////
                if (less_than_three_months_event_created) {
                    weighteditmode.add(true);
                } else {
                    weighteditmode.add(false);
                }

                final int finalI = i;
                View.OnClickListener onclicklistener = new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        SampleUtils.showEditBpmDialog(MainActivity.this, finalI, DIALOG_TAG);
                    }
                };
                listeners.add(onclicklistener);
            }

        }
        if (weightmap.size() < 5) {
            weightmap.put(0l, Pair.create(DateUtil.getDuration(0), SampleUtils.BIRTH_WEIGHT + " mm/hg"));
            weighteditmode.add(false);
            listeners.add(null);
        }

        if (weightmap.size() > 0) {
            SampleUtils.createWeightWidget(MainActivity.this, weightWidget, weightmap, listeners, weighteditmode);
        }
    }


    private class ShowBpmChartTask extends AsyncTask<Void, Void, List<Bpm>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Bpm> doInBackground(Void... params) {
            BpmRepository bpmRepository = DeviceInterfaceLibrary.getInstance().bpmRepository();
            Log.e(TAG, "doInBackground:bpmRepository "+ bpmRepository);
            List<Bpm> allBpms = bpmRepository.findByEntityId(SampleUtils.ENTITY_ID);
            Log.e(TAG, "doInBackground:allBpms "+ allBpms );

            try {
                DateTime dateTime = new DateTime(SampleUtils.STR_DOB);
                Bpm bpm = new Bpm(-1l, null, SampleUtils.SISTOLE, SampleUtils.DIASTOLE, dateTime.toDate(), null, null, null, Calendar.getInstance().getTimeInMillis(), null, null, 0);
                allBpms.add(bpm);
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            return allBpms;
        }

        @Override
        protected void onPostExecute(List<Bpm> allBpms) {
            super.onPostExecute(allBpms);

            BpmDialogFragment bpmDialogFragment = BpmDialogFragment.newInstance(SampleUtils.dummyDetails(), allBpms);
            // Todo Add null assert for Fragment
            bpmDialogFragment.show(SampleUtils.initFragmentTransaction(MainActivity.this, DIALOG_TAG), DIALOG_TAG);
        }
    }

    //Bpm Listener
    @Override
    public void onBpmTaken(BpmWrapper bpmWrapper) {
        Toast.makeText(this, bpmWrapper.getBpm() + " Taken", Toast.LENGTH_SHORT).show();
    }
    //Bpm Listener
    @Override
    public void onBpmTaken() {
        Toast.makeText(this, "Bpm Taken", Toast.LENGTH_SHORT).show();
    }
}
