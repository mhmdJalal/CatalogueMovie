package com.codepolitan.s4cataoguemovie.model;

import com.google.gson.annotations.SerializedName;

public class M_Date {

    @SerializedName("maximum")
    private String maximum;
    @SerializedName("minimum")
    private String minimum;

    public String getMaximum() {
        return maximum;
    }

    public void setMaximum(String maximum) {
        this.maximum = maximum;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }
}
