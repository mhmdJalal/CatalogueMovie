package com.codepolitan.s4cataoguemovie.service;

import android.content.Context;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

public class UpcomingTask {
    private GcmNetworkManager gcmNetworkManager;

    public UpcomingTask(Context context) {
        gcmNetworkManager = GcmNetworkManager.getInstance(context);
    }

    public void createPeriodicTask(){
        Task task = new PeriodicTask.Builder()
                .setService(UpcomingService.class)
                .setPeriod(60)
                .setFlex(10)
                .setTag(UpcomingService.TAG_UPCOMING_TASK)
                .setPersisted(true)
                .build();
        gcmNetworkManager.schedule(task);
    }

    public void cancelPeriodicTask(){
        if (gcmNetworkManager != null){
            gcmNetworkManager.cancelTask(UpcomingService.TAG_UPCOMING_TASK, UpcomingService.class);
        }
    }
}
