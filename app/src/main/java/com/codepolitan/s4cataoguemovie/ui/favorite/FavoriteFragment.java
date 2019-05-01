package com.codepolitan.s4cataoguemovie.ui.favorite;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepolitan.s4cataoguemovie.R;
import com.codepolitan.s4cataoguemovie.adapter.FavoriteAdapter;
import com.codepolitan.s4cataoguemovie.adapter.MovieAdapter;
import com.codepolitan.s4cataoguemovie.database.FavoriteHelper;
import com.codepolitan.s4cataoguemovie.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.codepolitan.s4cataoguemovie.database.DatabaseContract.FavoriteColumn.CONTENT_URI;

public class FavoriteFragment extends Fragment {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recycler_favorite)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;

    private FavoriteAdapter favoriteAdapter;
    private FavoriteHelper favoriteHelper;
    private Cursor cur;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteAdapter = new FavoriteAdapter(getContext());
        favoriteHelper = new FavoriteHelper(getContext());
        favoriteHelper.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        favoriteAdapter.setListFavorite(cur);
        recyclerView.setAdapter(favoriteAdapter);
        new LoadFavorite().execute();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);
                new LoadFavorite().execute();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }

    private class LoadFavorite extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Cursor cursor = getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            progressBar.setVisibility(View.GONE);
            refreshLayout.setRefreshing(false);

            cur = cursor;
            favoriteAdapter.setListFavorite(cur);
        }
    }
}
