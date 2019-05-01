package com.codepolitan.s4cataoguemovie.ui.setting;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import com.codepolitan.s4cataoguemovie.R;
import com.codepolitan.s4cataoguemovie.service.AlarmReceiver;
import com.codepolitan.s4cataoguemovie.helper.PrefManager;
import com.codepolitan.s4cataoguemovie.service.UpcomingNPJobService;
import com.codepolitan.s4cataoguemovie.service.UpcomingTask;
import com.codepolitan.s4cataoguemovie.ui.main.MainActivity;

import java.util.Locale;


@SuppressLint("ExportedPreferenceActivity")
public class SettingActivity extends AppCompatPreferenceActivity {

    private static final String TAG = SettingActivity.class.getSimpleName();
    private static final String TAG_JOB = "JOB_SCHEDULER";
    private static final String TAG_GCM = "GCM_NETWORK";
    private static final String TAG_DAILY = "DAILY_REMINDER";
    public static final String KEY_DAILY_REMINDER = "swDailyReminder";
    public static final String KEY_UPNP_REMINDER = "swUpnpReminder";
    public static final String KEY_LANGUANGE = "language";
    private static final int JOB_ID = 10;

    SharedPreferences sharedPref;
    Boolean swDailyReminder, swUpnpReminder;
    String language;

    private AlarmReceiver alarmReceiver;
    private UpcomingTask task;
    private PrefManager prefManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.setting));
        alarmReceiver = new AlarmReceiver();
        task = new UpcomingTask(this);
        prefManager = new PrefManager(this);
        context = this;

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        loadLocale();

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit();
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            swDailyReminder = sharedPreferences.getBoolean(KEY_DAILY_REMINDER, true);
            swUpnpReminder = sharedPreferences.getBoolean(KEY_UPNP_REMINDER, true);
            language = sharedPreferences.getString(KEY_LANGUANGE, "");

                switch (key) {
                    case SettingActivity.KEY_DAILY_REMINDER:
                        if (swDailyReminder) {
                            Log.i(TAG_DAILY, "Daily Reminder Running");
                            prefManager.setIsActiveReminder(true);
                            alarmReceiver.setDailyReminder(context, AlarmReceiver.TYPE_REPEATING);
                        } else {
                            Log.i(TAG_DAILY, "Daily Reminder Stopped");
                            prefManager.setIsActiveReminder(false);
                            alarmReceiver.cancelAlarm(context);
                        }
                        break;
                    case SettingActivity.KEY_UPNP_REMINDER:
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            if (swUpnpReminder) {
                                Log.i(TAG_JOB, "JOB_SERVICE : " + String.valueOf(swUpnpReminder));
                                prefManager.setIsActiveUpcoming(true);
                                prefManager.setIsJobScheduler(true);
                                startJob();
                            } else {
                                Log.i(TAG_JOB, "JOB_SERVICE : " + String.valueOf(swUpnpReminder));
                                prefManager.setIsActiveUpcoming(false);
                                prefManager.setIsJobScheduler(false);
                                cancelJob();
                            }
                        } else {
                            if (swUpnpReminder) {
                                Log.i(TAG_GCM, "GCM : " + String.valueOf(swUpnpReminder));
                                prefManager.setIsActiveUpcoming(true);
                                prefManager.setIsGcm(true);
                                task.createPeriodicTask();
                            } else {
                                Log.i(TAG_GCM, "GCM : " + String.valueOf(swUpnpReminder));
                                prefManager.setIsActiveUpcoming(false);
                                prefManager.setIsGcm(false);
                                task.cancelPeriodicTask();
                            }
                        }
                        break;
                    case SettingActivity.KEY_LANGUANGE:
                        if (language.equals("English")) {
                            Log.i(TAG, "Language selected is en("+language+")");
                            changeLang("English");
                        }else if (language.equals("Indonesia")) {
                            Log.i(TAG, "Language selected is id("+ language +")");
                            changeLang("Indonesia");
                        }
                        break;
                }
        }
    };

    private void startJob(){
        if (isJobRunning(this)) {
            Log.i(TAG, "JOB_SCHEDULER service is already running");
            return;
        }

        ComponentName componentName = new ComponentName(this, UpcomingNPJobService.class);
        JobInfo.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder = new JobInfo.Builder(JOB_ID, componentName);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setRequiresDeviceIdle(false);
            builder.setRequiresCharging(false);
            builder.setMinimumLatency(86000);
            builder.setOverrideDeadline(10*1000);

            JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler != null) {
                jobScheduler.schedule(builder.build());
            }
        }
    }

    private void cancelJob(){
        JobScheduler jobScheduler  = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler != null) {
                jobScheduler.cancel(JOB_ID);
            }
        }
    }

    private boolean isJobRunning(Context context) {
        boolean isScheduled = false;

        JobScheduler scheduler = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

            if (scheduler != null) {
                for (JobInfo jobInfo : scheduler.getAllPendingJobs()) {
                    if (jobInfo.getId() == JOB_ID) {
                        isScheduled = true;
                        break;
                    }
                }
            }
        }

        return isScheduled;
    }

    private void changeLang(String langpos){
        switch (langpos) {
            case "Indonesia": // Indonesia
                setLocale("id");
                recreate();
                break;
            case "English": // Indonesia
                setLocale("en");
                recreate();
                break;
            default: // By default set to english
                setLocale("en");
                recreate();
                break;
        }
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Configuration config = new Configuration();

        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        prefManager.setLANGUAGE(lang);
    }

    private void loadLocale() {
        if (prefManager.getLANGUAGE().equals("")){
            prefManager.setLANGUAGE("en");
        }
        setLocale(prefManager.getLANGUAGE());
    }

    @Override
    protected void onResume() {
        sharedPref.registerOnSharedPreferenceChangeListener(listener);
        Log.i(TAG, "ON_RESUME : Register_On_Shared_Preference_Change_Listener");
        super.onResume();
    }

    @Override
    protected void onPause() {
        sharedPref.unregisterOnSharedPreferenceChangeListener(listener);
        Log.i(TAG, "ON_PAUSE : Unregister_On_Shared_Preference_Change_Listener");
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
