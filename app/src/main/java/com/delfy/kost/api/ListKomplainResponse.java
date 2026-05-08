package com.delfy.kost.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ListKomplainResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<KomplainModel> data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<KomplainModel> getData() { return data; }
}