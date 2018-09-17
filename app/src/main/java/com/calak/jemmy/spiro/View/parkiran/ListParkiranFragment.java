package com.calak.jemmy.spiro.View.parkiran;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calak.jemmy.spiro.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListParkiranFragment extends Fragment {


    public ListParkiranFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_parkiran, container, false);
    }

}
