package com.codepolitan.s4cataoguemovie.ui.main;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;


import com.codepolitan.s4cataoguemovie.ui.favorite.FavoriteFragment;
import com.codepolitan.s4cataoguemovie.R;
import com.codepolitan.s4cataoguemovie.service.AlarmReceiver;
import com.codepolitan.s4cataoguemovie.helper.PrefManager;
import com.codepolitan.s4cataoguemovie.service.UpcomingNPJobService;
import com.codepolitan.s4cataoguemovie.service.UpcomingTask;
import com.codepolitan.s4cataoguemovie.ui.homeFragment.HomeFragment;
import com.codepolitan.s4cataoguemovie.ui.nowPlaying.NowplayingFragment;
import com.codepolitan.s4cataoguemovie.ui.searchFragment.SearchMovieFragment;
import com.codepolitan.s4cataoguemovie.ui.setting.SettingActivity;
import com.codepolitan.s4cataoguemovie.ui.upcomingMovie.UpcomingFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int JOB_ID = 10;
    FragmentManager fm;
    String lang;

    private AlarmReceiver alarmReceiver;
    private PrefManager prefManager;
    private UpcomingTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        prefManager = new PrefManager(this);
        alarmReceiver = new AlarmReceiver();
        task = new UpcomingTask(this);

        loadLocale();
        loadDailyReminder();
        loadUpcomingNowplayingReminder();

        setSupportActionBar(toolbar);

        fm = getSupportFragmentManager();
        addFragment(new HomeFragment());

        /* Navigation bar */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Configuration config = new Configuration();

        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        if (prefManager.getFirst() == 0) {
            prefManager.setFirst(1);
            recreate();
        }else {
            prefManager.setFirst(0);
        }
    }

    void loadLocale() {
        Log.i(TAG, "SHARED_PREFERENCE_FILLED_OUT : " + prefManager.getLANGUAGE());
        lang = prefManager.getLANGUAGE();

        switch (lang) {
            case "en":
                setLocale("en");
                break;
            case "id":
                setLocale("id");
                break;
            default:
                setLocale("en");
                break;
        }
    }

    private void loadDailyReminder() {
        if (prefManager.getIsActiveReminder()) {
            Log.i(TAG, "ON " + String.valueOf(prefManager.getIsActiveReminder()));
            alarmReceiver.setDailyReminder(this, AlarmReceiver.TYPE_REPEATING);
        }else {
            Log.i(TAG, "Off " + String.valueOf(prefManager.getIsActiveReminder()));
            alarmReceiver.cancelAlarm(this);
        }
    }

    private void loadUpcomingNowplayingReminder() {
        if (prefManager.getIsActiveUpcoming()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                startJob();
            }else {
                Log.i(TAG, "GCM_NETWORK_RUNNING");
                task.createPeriodicTask();
            }
        }else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                cancelJob();
            }else {
                Log.i(TAG, "GCM_NETWORK_CANCELED");
                task.cancelPeriodicTask();
            }
        }
    }

    @SuppressLint("JobSchedulerService")
    private void startJob(){
        if (isJobRunning(this)) {
            Log.i(TAG, "Job Service is already scheduled");
            return;
        }
        ComponentName componentName = new ComponentName(this, UpcomingNPJobService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setMinimumLatency(8000);
            builder.setOverrideDeadline(10*1000);

            JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler != null) {
                Log.i(TAG, "JOB_SCHEDULAR_RUNNING");
                jobScheduler.schedule(builder.build());
            }
        }
    }

    private void cancelJob(){
        JobScheduler jobScheduler  = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler != null) {
                Log.i(TAG, "JOB_SCHEDULAR_CANCELED");
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                addFragment(new HomeFragment());
                break;
            case R.id.nav_search:
                addFragment(new SearchMovieFragment());
                break;
            case R.id.nav_nowplaying:
                addFragment(new NowplayingFragment());
                break;
            case R.id.nav_upcoming:
                addFragment(new UpcomingFragment());
                break;
            case R.id.nav_favorite:
                addFragment(new FavoriteFragment());
                break;
            case R.id.action_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("PrivateResource")
    private void addFragment(Fragment fragment){
        fm.beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.frame_container, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

}
