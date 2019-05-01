package com.codepolitan.s4cataoguemovie.ui.upcomingMovie;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepolitan.s4cataoguemovie.R;
import com.codepolitan.s4cataoguemovie.adapter.MovieAdapter;
import com.codepolitan.s4cataoguemovie.helper.PrefManager;
import com.codepolitan.s4cataoguemovie.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UpcomingFragment extends Fragment implements UpcomingView {
    @BindView(R.id.recycler_upcoming)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.not_found)
    TextView not_found;

    MovieAdapter adapter;
    UpcomingPresenter<UpcomingFragment> presenter;
    PrefManager prefManager;

    public UpcomingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new UpcomingPresenter<>(this);
        adapter = new MovieAdapter(getContext());
        prefManager = new PrefManager(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_upcoming, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String lang = prefManager.getLANGUAGE();
        presenter.loadUpcoming(lang);
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadUpcoming(lang);
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
    public void showUpcomingList(List<Movie> movies) {
        if (movies.size() > 0) {
            adapter.replace_date(movies);
            not_found.setVisibility(View.GONE);
        }else {
            not_found.setVisibility(View.VISIBLE);
        }
    }
}
