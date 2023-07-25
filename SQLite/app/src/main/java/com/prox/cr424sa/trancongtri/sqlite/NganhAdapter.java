package com.prox.cr424sa.trancongtri.sqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NganhAdapter extends ArrayAdapter<Nganh> {

    public NganhAdapter(@NonNull Context context, @NonNull List<Nganh> nganh) {
        super(context, 0, nganh);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_nganh, parent, false);
        }

        Nganh nganh = getItem(position);
        TextView tvTenNganh = view.findViewById(R.id.tv_tenNganh);
        tvTenNganh.setText(nganh.getTenNganh());

        return view;
    }


}
