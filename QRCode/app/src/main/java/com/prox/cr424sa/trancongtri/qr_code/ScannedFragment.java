package com.prox.cr424sa.trancongtri.qr_code;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.prox.cr424sa.trancongtri.qr_code.Adapter.QRAdapter;
import com.prox.cr424sa.trancongtri.qr_code.Model.QR;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScannedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScannedFragment extends Fragment {


    List<QR> QRList;

    private QRAdapter qrListAdapter;

    private ListView listView;
    Database db;

    public ScannedFragment() {
        // Required empty public constructor
    }


    public static ScannedFragment newInstance(String param1, String param2) {
        ScannedFragment fragment = new ScannedFragment();
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
        View view  =inflater.inflate(R.layout.fragment_scanned, container, false);

        db = new Database(getContext());
        QRList  = (List<QR>) db.getAllQR();
        listView = view.findViewById(R.id.listViewScanned);
        qrListAdapter = new QRAdapter(getContext(), QRList);
        listView.setAdapter(qrListAdapter);

        return view;
    }
}