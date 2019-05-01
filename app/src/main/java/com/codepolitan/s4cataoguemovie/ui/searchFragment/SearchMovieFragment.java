package com.codepolitan.s4cataoguemovie.ui.searchFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.codepolitan.s4cataoguemovie.R;
import com.codepolitan.s4cataoguemovie.adapter.SearchMovieAdapter;
import com.codepolitan.s4cataoguemovie.config.ClientConfig;
import com.codepolitan.s4cataoguemovie.helper.PrefManager;
import com.codepolitan.s4cataoguemovie.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchMovieFragment extends Fragment implements SearchMovieView{
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.btn_search)
    ImageButton btn_search;

    String search_query;
    SearchMovieAdapter adapter;
    SearchMoviePresenter<SearchMovieFragment> presenter;
    PrefManager prefManager;
    private Boolean isSearching = false;

    public SearchMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchMoviePresenter<>(this);
        adapter = new SearchMovieAdapter(getContext());
        prefManager = new PrefManager(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_movie, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearching = true;
                search_query = et_search.getText().toString();
                if (search_query.isEmpty()){
                    Toast.makeText(getContext(), getResources().getString(R.string.alert_search), Toast.LENGTH_SHORT).show();
                }else {
                    presenter.loadSearchMovie(ClientConfig.API_KEY, prefManager.getLANGUAGE(), search_query);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void showLoading() {
        if (isSearching){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showListMovie(List<Movie> movies) {
        adapter.replace_data(movies);
    }
}
