package com.codepolitan.s4cataoguemovie.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class MovieWidgetService  extends RemoteViewsService{

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.i(MovieWidgetService.class.getSimpleName(), "onGetViewFactory()");
        return new MovieRemoteViewsFactory(this.getApplicationContext(), intent);
    }

}
