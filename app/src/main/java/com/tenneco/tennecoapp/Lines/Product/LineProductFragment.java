package com.tenneco.tennecoapp.Lines.Product;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenneco.tennecoapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LineProductFragment extends Fragment {


    public LineProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_line_product, container, false);
    }

}
