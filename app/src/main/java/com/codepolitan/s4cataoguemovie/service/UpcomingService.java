package com.codepolitan.s4cataoguemovie.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.codepolitan.s4cataoguemovie.API.ApiClient;
import com.codepolitan.s4cataoguemovie.API.ApiInterface;
import com.codepolitan.s4cataoguemovie.R;
import com.codepolitan.s4cataoguemovie.config.ClientConfig;
import com.codepolitan.s4cataoguemovie.model.M_Date;
import com.codepolitan.s4cataoguemovie.model.Movie;
import com.codepolitan.s4cataoguemovie.response.MovieResponse;
import com.codepolitan.s4cataoguemovie.ui.detail.DetailMovieActivity;
import com.codepolitan.s4cataoguemovie.widget.MovieRemoteViewsFactory;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.CONTENT_URI;

public class UpcomingService extends GcmTaskService{
    public static final String TAG = "Upcoming/NowPlaying";
    public static String TAG_UPCOMING_TASK = "UpcomingTask";
    public static String TAG_NOWPLAYING_TASK = "NowplayingTask";
    private static final String CHANNEL_NAME = "Upcoming Task";
    private static final String CHANNEL_ID = "Chanel_2";
    private static int UPCOMING_ID = 1;
    private static int NOWPLAYING_ID = 400;

    ApiInterface apiInterface;
    public static List<Movie> movies = new ArrayList<>();

    Calendar c = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        if (taskParams.getTag().equals(TAG_UPCOMING_TASK)){
            getUpcomingMovie();
//            nowplayingMovie();
            result = GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }

    private void getUpcomingMovie() {
        Log.i(TAG_UPCOMING_TASK, "GCM : Upcoming Movie Running");

        apiInterface = new ApiClient().getRetrofit().create(ApiInterface.class);
        apiInterface.upcomingMovie(ClientConfig.API_KEY, ClientConfig.LANGUAGE)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                        if (response.isSuccessful()){
                            if (response.body() != null) {
                                movies.clear();
                                for (int i = 0; i < response.body().getResults().size(); i++){
                                    Movie movie = response.body().getResults().get(i);
                                    M_Date MDate = response.body().getDates();

                                    try {
                                        Date min = df.parse(MDate.getMinimum());
                                        Date max = df.parse(MDate.getMaximum());
                                        Date releaseDate = df.parse(movie.getRelease_date());
                                        if (releaseDate.after(min) && releaseDate.before(max)){

                                            MovieRemoteViewsFactory viewsFactory = new MovieRemoteViewsFactory();
                                            movies.add(new Movie(movie.getVote_count(), movie.getId(), movie.getVote_average(), movie.getTitle(), movie.getPopularity(), movie.getPoster_path(), movie.getBackdrop_path(), movie.getOverview(), movie.getRelease_date()));
                                            viewsFactory.setList(movies);

                                            Log.i(TAG_UPCOMING_TASK, "GCM Service Running : " + movie.getTitle());
                                            Log.i(TAG_UPCOMING_TASK, "GCM Service Running : " + movies.size());
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                        Log.d(TAG, t.getMessage());
                    }
                });
    }

    private void nowplayingMovie(){
        Log.i(TAG_NOWPLAYING_TASK, "GCM : Nowplaying Movie Running");

        apiInterface = new ApiClient().getRetrofit().create(ApiInterface.class);
        apiInterface.nowplayingMovie(ClientConfig.API_KEY, ClientConfig.LANGUAGE)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                        if (response.isSuccessful()){
                            if (response.body() != null){
                                for(int i = 0; i < response.body().getResults().size(); i++) {
                                    M_Date date = response.body().getDates();
                                    Movie movie = response.body().getResults().get(i);

                                    try {
                                        Date releaseDate = df.parse(movie.getRelease_date());
                                        Date min = df.parse(date.getMinimum());
                                        Date max = df.parse(date.getMaximum());
                                        if (releaseDate.after(min) && releaseDate.before(max)){
//                                            widgetManager = AppWidgetManager.getInstance(getBaseContext());
//
//                                            remoteViews = new RemoteViews(getPackageName(), R.layout.up_now_playing_widget);
//                                            componentName = new ComponentName(getBaseContext(), UpNowPlayingWidgetProvider.class);
//
//                                            if (!movie.getRelease_date().isEmpty()) {
//                                                remoteViews.setTextViewText(R.id.release_date, Util.parseDate(movie.getRelease_date()));
//                                            }else {
//                                                remoteViews.setTextViewText(R.id.release_date, "date");
//                                            }
//
//                                            Bitmap bitmap = null;
//                                            if (!movie.getBackdrop_path().isEmpty()) {
//                                                bitmap = Glide.with(getBaseContext())
//                                                        .asBitmap()
//                                                        .load(ClientConfig.BACKDROP_URL + movie.getBackdrop_path())
//                                                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                                                        .get();
//                                            }else {
//                                                bitmap  = BitmapFactory.decodeResource(getResources(), R.drawable.logo_codepolitan);
//                                            }
//
//                                            remoteViews.setImageViewBitmap(R.id.iv_banner, bitmap);
//
//                                            widgetManager.updateAppWidget(componentName, remoteViews);
//                                            showNotification(getApplicationContext(), String.valueOf(movie.getId()), "Nowplaying Movie : " + movie.getTitle(), movie.getOverview(), i);
//                                            NOWPLAYING_ID++;
//                                            new MovieRemoteViewsFactory(response.body().getResults());
//                                            movies = response.body().getResults();
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                        Log.d(TAG, t.getMessage());
                    }
                });
    }

    private void showNotification(Context context, String id, String title, String overview, int notifId) {
        Intent intent = new Intent(context, DetailMovieActivity.class);
        Uri uri = Uri.parse(CONTENT_URI + "/" + id);
        intent.setData(uri);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent, 0);

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_access_alarm)
                .setContentText(overview)
                .setColor(ContextCompat.getColor(context, android.R.color.black))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(alarm);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, builder.build());
        }

    }

    public static List<Movie> getList() {
        return movies;
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        UpcomingTask upcomingTask = new UpcomingTask(this);
        upcomingTask.createPeriodicTask();
    }
}
