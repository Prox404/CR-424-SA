package com.prox.cr424sa.trancongtri.qr_code;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class GenerateTextQRActivity extends BaseActivity {

    private ImageView imageViewResult;
    private EditText editTextText;
    private Button btnGenerate,btnShare, btnSave;

    LinearLayout showActionLayout;
    Database db;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    String scannedContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_text_qractivity);

        imageViewResult = findViewById(R.id.imageView_Result);
        editTextText = findViewById(R.id.editText_Text);
        btnGenerate = findViewById(R.id.btn_generate);
        btnShare = findViewById(R.id.btn_share);
        btnSave = findViewById(R.id.btn_save);
        showActionLayout = findViewById(R.id.qr_action);
        showActionLayout.setVisibility(View.GONE);
        db = new Database(this);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToEncode = editTextText.getText().toString().trim();
                scannedContent = textToEncode;
                if (!textToEncode.isEmpty()) {
                    generateQRCode(textToEncode);
                    showActionLayout.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(GenerateTextQRActivity.this, "Please input a message !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareQRCode();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQRCode();
            }
        });
    }

    private void shareQRCode() {
        // Get the bitmap from the ImageView
        Bitmap qrCodeBitmap = ((BitmapDrawable) imageViewResult.getDrawable()).getBitmap();

        // Create a share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");

        // Save the bitmap to a file
        String qrCodePath = saveImageToStorage(qrCodeBitmap);

        // Attach the file to the share intent
        if (qrCodePath != null) {
            Uri qrCodeUri = Uri.parse(qrCodePath);
            shareIntent.putExtra(Intent.EXTRA_STREAM, qrCodeUri);
        }

        // Show the share chooser dialog
        startActivity(Intent.createChooser(shareIntent, "Share QR Code"));
    }

    private String saveImageToStorage(Bitmap bitmap) {
        // Get the root directory of the external storage
        String root = Environment.getExternalStorageDirectory().toString();

        // Create a directory for the app's images
        File myDir = new File(root + "/QRCodeImages");
        myDir.mkdirs();

        // Generate a unique filename for the image
        String filename = "QR_" + System.currentTimeMillis() + ".jpg";

        // Create the file
        File file = new File(myDir, filename);
        if (file.exists()) file.delete();

        try {
            // Write the bitmap to the file
            OutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            // Add the image to the device's gallery
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, filename);
            values.put(MediaStore.Images.Media.DESCRIPTION, "QR Code");
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase().hashCode());
            values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase());
            values.put("_data", file.getAbsolutePath());
            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveQRCode() {
        // Get the bitmap from the ImageView
        Bitmap qrCodeBitmap = ((BitmapDrawable) imageViewResult.getDrawable()).getBitmap();

        // Check if the app has the WRITE_EXTERNAL_STORAGE permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Save the bitmap to the device's gallery
            String qrCodePath = saveImageToStorage(qrCodeBitmap);

            if (qrCodePath != null) {
                boolean isInserted = db.insertCreatedQR(scannedContent,null, null, null, null, null, null, null, qrCodePath);
                if (isInserted) {
                    Toast.makeText(this, "QR Code data saved successfully !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to save QR Code data !", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to save QR Code", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Request the permission if it has not been granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    private void generateQRCode(String textToEncode) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(textToEncode, BarcodeFormat.QR_CODE, 300, 300);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? ContextCompat.getColor(this, R.color.black) : ContextCompat.getColor(this, R.color.white));
                }
            }

            imageViewResult.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, save the QR code
                saveQRCode();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Permission denied to save QR Code", Toast.LENGTH_SHORT).show();
            }
        }
    }

}