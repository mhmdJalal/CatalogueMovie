package com.codepolitan.s4cataoguemovie.ui.nowPlaying;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepolitan.s4cataoguemovie.R;
import com.codepolitan.s4cataoguemovie.adapter.MovieAdapter;
import com.codepolitan.s4cataoguemovie.helper.EndlessScrollListener;
import com.codepolitan.s4cataoguemovie.helper.PrefManager;
import com.codepolitan.s4cataoguemovie.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NowplayingFragment extends Fragment implements NowplayingView{
    @BindView(R.id.recycler_nowplaying)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.not_found)
    TextView not_found;

    MovieAdapter adapter;
    NowplayingPresenter<NowplayingFragment> presenter;
    PrefManager prefManager;

    public NowplayingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new NowplayingPresenter<>(this);
        adapter = new MovieAdapter(getContext());
        prefManager = new PrefManager(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nowplaying, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String lang = prefManager.getLANGUAGE();
        presenter.loadNowPlaying(lang);
        recyclerView.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadNowPlaying(lang);
                recyclerView.setAdapter(adapter);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showNowplayingList(List<Movie> movies) {
        if (movies.size() > 0){
            adapter.replace_date(movies);
            not_found.setVisibility(View.GONE);
        }else {
            not_found.setVisibility(View.VISIBLE);
        }
    }
}
