package com.prox.cr424sa.trancongtri.qr_code;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.prox.cr424sa.trancongtri.qr_code.Adapter.QRCreatedAdapter;
import com.prox.cr424sa.trancongtri.qr_code.Model.QR;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatedFragment extends Fragment {

    List<QR> QRList;

    private QRCreatedAdapter qrListAdapter;

    private ListView listView;
    Database db;

    public CreatedFragment() {
        // Required empty public constructor
    }

    public static CreatedFragment newInstance(String param1, String param2) {
        CreatedFragment fragment = new CreatedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_created, container, false);
        db = new Database(getContext());
        QRList  = (List<QR>) db.getAllCreatedQR();
        listView = view.findViewById(R.id.listViewCreated);
        qrListAdapter = new QRCreatedAdapter(getContext(), QRList);
        listView.setAdapter(qrListAdapter);
        return view;
    }
}