package com.codepolitan.s4cataoguemovie.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.CONTENT_URI;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class UpcomingNPJobService extends JobService {
    public static final String TAG = "JobService";
    public static final String TAG_UPCOMING = "Upcoming";
    public static final String TAG_NOWPLAYING = "Nowplaying";
    private static final String CHANNEL_NAME = "JobScheduler Channel";
    private static final String CHANNEL_ID = "Channel_3";
    private static int UPCOMING_ID = 200;
    private static int NOWPLAYING_ID = 300;

    ApiInterface apiInterface;
    public static List<Movie> movies = new ArrayList<>();

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "Job Service Running");

//        nowplayingMovie(params);
        getUpcomingMovie(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "Job Service Stopped");
        return true;
    }

    public void getUpcomingMovie(final JobParameters jobParameters) {
        Log.i(TAG_UPCOMING, "Upcoming Movie JobService Running");

        apiInterface = new ApiClient().getRetrofit().create(ApiInterface.class);
        apiInterface.upcomingMovie(ClientConfig.API_KEY, ClientConfig.LANGUAGE)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                        if (response.isSuccessful()){
                            if (response.body() != null){
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

                                            Log.i(TAG_UPCOMING, "Service Running : Title " + movie.getTitle());
                                            Log.i(TAG_UPCOMING, "Service Running : Size " + movies.size());
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                jobFinished(jobParameters, true);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                        Log.i(TAG, "onFailure Upcoming : " + t.getMessage());
                        jobFinished(jobParameters, true);
                    }
                });
    }

    public void nowplayingMovie(final JobParameters jobParameters){
        Log.i(TAG_NOWPLAYING, "Nowplaying Movie JobService Running");

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
                                    Log.i(TAG_NOWPLAYING, "Service Running");

                                    try {
                                        Date releaseDate = df.parse(movie.getRelease_date());
                                        Date min = df.parse(date.getMinimum());
                                        Date max = df.parse(date.getMaximum());
                                        if (releaseDate.after(min) && releaseDate.before(max)){
//                                            RemoteViews rv = new RemoteViews(getPackageName(), R.layout.movie_widget);
//
//                                            Bitmap bmp = null;
//                                            try {
//                                                if (movie.getBackdrop_path() != null){
//                                                    bmp = Glide.with(getBaseContext())
//                                                            .asBitmap()
//                                                            .load(ClientConfig.BACKDROP_URL + movie.getBackdrop_path())
//                                                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                                                            .get();
//                                                }else {
//                                                    bmp = Glide.with(getBaseContext())
//                                                            .asBitmap()
//                                                            .load("http://ppid.lapan.go.id/uploads/images/noimage.png")
//                                                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                                                            .get();
//                                                }
//
//                                                rv.setImageViewBitmap(R.id.img_widget, bmp);
//                                                if (movie.getTitle() != null) {
//                                                    rv.setTextViewText(R.id.tv_movie_title, movie.getTitle());
//                                                }else {
//                                                    rv.setTextViewText(R.id.tv_movie_title, "Catalogue Movie");
//                                                }
//                                                Log.d("Widgetku","Yessh");
//                                            }catch (InterruptedException | ExecutionException e){
//                                                Log.d("Widget Load Error","error");
//                                            }
//                                            Bundle extras = new Bundle();
//                                            extras.putInt(MovieWidget.EXTRA_ITEM, movie.getId());
//                                            Intent fillInIntent = new Intent();
//                                            fillInIntent.putExtras(extras);
//
//                                            rv.setOnClickFillInIntent(R.id.img_widget, fillInIntent);
//                                            showNotification(getApplicationContext(), String.valueOf(movie.getId()), "Nowplaying Movie : " + movie.getTitle(), movie.getOverview(), NOWPLAYING_ID);
//                                            NOWPLAYING_ID++;
//                                            new MovieRemoteViewsFactory(response.body().getResults());
//                                            movies = response.body().getResults();
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                jobFinished(jobParameters, true);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                        Log.i(TAG, "onFailure NowPlaying : " + t.getMessage());
                        jobFinished(jobParameters, true);
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
    }

    public static List<Movie> getList() {
        return movies;
    }
}
