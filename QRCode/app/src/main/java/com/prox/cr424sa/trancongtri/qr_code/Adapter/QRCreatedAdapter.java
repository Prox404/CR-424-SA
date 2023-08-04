package com.prox.cr424sa.trancongtri.qr_code.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.prox.cr424sa.trancongtri.qr_code.Database;
import com.prox.cr424sa.trancongtri.qr_code.Model.QR;
import com.prox.cr424sa.trancongtri.qr_code.R;

import java.util.List;

public class QRCreatedAdapter extends ArrayAdapter<QR> {

    public QRCreatedAdapter(Context context, List<QR> qrList) {
        super(context, 0, qrList);
    }
    Database db;

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QR qr = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_qr_layout, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewOrg = convertView.findViewById(R.id.textViewOrg);
        TextView textViewText = convertView.findViewById(R.id.textViewText);
        TextView textViewURL = convertView.findViewById(R.id.textViewURL);
        TextView textViewPhone = convertView.findViewById(R.id.textViewPhone);
        TextView textViewAddress = convertView.findViewById(R.id.textViewAddress);
        TextView textViewEmail = convertView.findViewById(R.id.textViewEmail);
        TextView textViewWebsite = convertView.findViewById(R.id.textViewWebsite);
        TextView textViewType = convertView.findViewById(R.id.textViewType);
        ImageView imageViewQR =  convertView.findViewById(R.id.imageViewQR);

        Button btnDelete = convertView.findViewById(R.id.btn_Delete);
        db = new Database(getContext());
        int id = qr.getID();
        Resources res = getContext().getResources();

        if (qr.getText() != null) {
            textViewText.setVisibility(View.VISIBLE);
            textViewText.setText(String.format(res.getString(R.string.qr_adapter_text), qr.getText()));
            textViewType.setText(getContext().getString(R.string.text));
        } else {
            textViewText.setVisibility(View.GONE);
        }

        if (qr.getURL() != null) {
            textViewURL.setVisibility(View.VISIBLE);
            textViewURL.setText(String.format(res.getString(R.string.qr_adapter_url), qr.getURL()));
            textViewType.setText(getContext().getString(R.string.link));
        } else {
            textViewURL.setVisibility(View.GONE);
        }

        if (qr.getName() != null) {
            textViewName.setVisibility(View.VISIBLE);
            textViewName.setText(String.format(res.getString(R.string.qr_adapter_name), qr.getName()));
        } else {
            textViewName.setVisibility(View.GONE);
        }

        if (qr.getOrg() != null) {
            textViewOrg.setVisibility(View.VISIBLE);
            textViewOrg.setText(String.format(res.getString(R.string.qr_adapter_org), qr.getOrg()));
        } else {
            textViewOrg.setVisibility(View.GONE);
        }

        if (qr.getPhone() != null) {
            textViewPhone.setVisibility(View.VISIBLE);
            textViewPhone.setText(String.format(res.getString(R.string.qr_adapter_phone), qr.getPhone()));
            textViewType.setText(getContext().getString(R.string.phone_number));
        } else {
            textViewPhone.setVisibility(View.GONE);
        }

        if (qr.getAddress() != null) {
            textViewAddress.setVisibility(View.VISIBLE);
            textViewAddress.setText(String.format(res.getString(R.string.qr_adapter_address), qr.getAddress()));
        } else {
            textViewAddress.setVisibility(View.GONE);
        }

        if (qr.getEmail() != null) {
            textViewEmail.setVisibility(View.VISIBLE);
            textViewEmail.setText(String.format(res.getString(R.string.qr_adapter_email), qr.getEmail()));
        } else {
            textViewEmail.setVisibility(View.GONE);
        }

        if (qr.getWebsite() != null) {
            textViewWebsite.setVisibility(View.VISIBLE);
            textViewWebsite.setText(String.format(res.getString(R.string.qr_adapter_website), qr.getWebsite()));
        } else {
            textViewWebsite.setVisibility(View.GONE);
        }

        if (qr.getImage() != null) {
            imageViewQR.setVisibility(View.VISIBLE);
            Bitmap imageBitmap = loadImageFromPath(qr.getImage());
            imageViewQR.setImageBitmap(imageBitmap);
        } else {
            imageViewQR.setVisibility(View.GONE);
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean delete_ =  db.deleteCreatedQR(id);
                Toast toast;
                if (delete_){
                    toast = Toast.makeText(getContext(), "Delete QR successful !", Toast.LENGTH_SHORT);
                    List<QR> updatedQRs = db.getAllCreatedQR();
                    updateQRList(updatedQRs);
                }else{
                    toast = Toast.makeText(getContext(), "Delete QR failed !", Toast.LENGTH_SHORT);
                }
                toast.show();
            }
        });

        // Add similar logic for other TextViews (Phone, Email, Website, etc.) based on your requirement

        return convertView;
    }

    public void updateQRList(List<QR> qr) {
        clear();
        addAll(qr);
        notifyDataSetChanged();
    }

    private Bitmap loadImageFromPath(String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }
}
