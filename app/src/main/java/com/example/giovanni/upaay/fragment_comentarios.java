package com.example.giovanni.upaay;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fragment_comentarios extends Fragment{

    View RootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RootView=inflater.inflate(R.layout.ventana_comentarios, container, false);
        return RootView;


    }
}
