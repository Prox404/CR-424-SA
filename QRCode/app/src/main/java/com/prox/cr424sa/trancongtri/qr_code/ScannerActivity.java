package com.prox.cr424sa.trancongtri.qr_code;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.os.Bundle;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private ZXingScannerView scannerView;
    private TextView textViewResult;
    private boolean isPopupShown = false;
    private boolean isProcessing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        textViewResult = findViewById(R.id.textViewResult);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        if (!isProcessing) {
            isProcessing = true;
            String scanResult = result.getText();
//        textViewResult.setText(scanResult);
            if (isURL(scanResult)) {
                if (!isPopupShown) {
                    showURLOptions(scanResult);
                    isPopupShown = true;
                }
            } else {
                if (!isPopupShown) {
                    showURLText(scanResult);
                    isPopupShown = true;
                }
            }
        }
    }

    private void startCountdown() {
        new Handler().postDelayed(() -> {
            Log.i("countdown", "call");
            // Đếm ngược đã kết thúc, cho phép xử lý kết quả mới
            isProcessing = false;
            scannerView.resumeCameraPreview(this);
        }, 3000); // 3000 ms = 3 giây
    }

    private boolean isURL(String str) {
        // Kiểm tra xem chuỗi có bắt đầu bằng "http://" hoặc "https://" không
        return str != null && (str.startsWith("http://") || str.startsWith("https://"));
    }

    private void showURLOptions(String url) {
        String message = "Detected URL:\n" + url + "\n\nChoose an action:";
        new AlertDialog.Builder(this)
                .setTitle("QR Scan Result")
                .setMessage(message)
                .setPositiveButton("Open in Browser", (dialog, which) -> {
                    // Thực hiện thao tác mở trình duyệt ở đây (nếu cần)
                    isPopupShown = false;
                    startCountdown();
                })
                .setNegativeButton("Copy Link", (dialog, which) -> {
                    // Thực hiện thao tác sao chép đường dẫn ở đây (nếu cần)
                    isPopupShown = false;
                    startCountdown();
                })
                .setCancelable(true)
                .show();
    }

    private void showURLText(String text) {
        String message = "Detected text:\n" + text + "\n\n";
        new AlertDialog.Builder(this)
                .setTitle("QR Scan Result")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Thực hiện thao tác mở trình duyệt ở đây (nếu cần)
                    isPopupShown = false;
                    startCountdown();
                })
                .setCancelable(true)
                .show();
    }
}