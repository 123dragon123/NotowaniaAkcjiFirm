package com.przybylskik.stachn.notowaniaakcjifirm.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.przybylskik.stachn.notowaniaakcjifirm.R;

public class HistoricalFragment extends android.app.Fragment {

    public HistoricalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historical, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
