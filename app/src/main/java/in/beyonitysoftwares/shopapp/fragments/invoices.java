package in.beyonitysoftwares.shopapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import in.beyonitysoftwares.shopapp.R;
import in.beyonitysoftwares.shopapp.adapters.customerAdapter;
import in.beyonitysoftwares.shopapp.adapters.invoiceAdapter;
import in.beyonitysoftwares.shopapp.model.Invoice;
import in.beyonitysoftwares.shopapp.model.customer;
import in.beyonitysoftwares.shopapp.utils.Helper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link invoices#newInstance} factory method to
 * create an instance of this fragment.
 */
public class invoices extends Fragment {
    private static final String TAG = "invoices";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView rv;
    List<Invoice> invoiceList= new ArrayList<>();
    invoiceAdapter adapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public invoices() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment invoices.
     */
    // TODO: Rename and change types and number of parameters
    public static invoices newInstance(String param1, String param2) {
        invoices fragment = new invoices();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        invoiceList.clear();
        invoiceList.addAll(Helper.getInvoiceList());
        adapter = new invoiceAdapter(invoiceList,getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_invoices, container, false);
        //getActivity().getActionBar().setTitle("Invoices");

        rv = (RecyclerView) view.findViewById(R.id.invoicerv);
        rv.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rv.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        Log.d(TAG, "onCreateView: "+invoiceList.size());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(invoiceList!=null && adapter!=null){
            invoiceList.clear();
            invoiceList.addAll(Helper.getInvoiceList());
            Log.d(TAG, "onResume: "+invoiceList.size());
            adapter.notifyDataSetChanged();
        }

    }
}
