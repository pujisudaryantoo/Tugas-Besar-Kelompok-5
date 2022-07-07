package com.pujisudaryanto.tubesmbkelompok5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class Frag2_Pesan extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frag2_pesan = inflater.inflate(R.layout.activity_frag2__pesan, container, false);
        return frag2_pesan;
    }
}
