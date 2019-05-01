package com.codepolitan.s4cataoguemovie.response;

import com.codepolitan.s4cataoguemovie.model.M_Date;
import com.codepolitan.s4cataoguemovie.model.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {

    @SerializedName("page")
    private int page;
    @SerializedName("rotal_results")
    private int total_results;
    @SerializedName("total_pages")
    private int total_pages;
    @SerializedName("results")
    private List<Movie> results;
    @SerializedName("dates")
    private M_Date dates;

    public M_Date getDates() {
        return dates;
    }

    public void setDates(M_Date dates) {
        this.dates = dates;
    }

    public MovieResponse(List<Movie> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
