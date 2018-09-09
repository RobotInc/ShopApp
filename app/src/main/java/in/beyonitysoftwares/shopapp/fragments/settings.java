package in.beyonitysoftwares.shopapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.beyonitysoftwares.shopapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class settings extends Fragment {


    public settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // getActivity().getActionBar().setTitle("Settings");
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

}
