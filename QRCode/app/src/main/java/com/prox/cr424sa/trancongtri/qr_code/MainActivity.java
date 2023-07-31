package com.prox.cr424sa.trancongtri.qr_code;

import androidx.appcompat.app.AppCompatActivity;

import com.prox.cr424sa.trancongtri.qr_code.ScannerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QR_SCAN = 101;
    LinearLayout subGenerator;
    Button btnScanner, btnGenerator, btnGeneratorText;

    private boolean showSubGenerate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScanner = findViewById(R.id.btn_scanner);
        btnGenerator = findViewById(R.id.btn_generator);
        subGenerator = findViewById(R.id.sub_generator);
        btnGeneratorText = findViewById(R.id.btn_generate_text);

        subGenerator.setVisibility(View.GONE);

        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScannerActivity.class);
                startActivityForResult(intent, REQUEST_CODE_QR_SCAN);
            }
        });

        btnGenerator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSubGenerate = !showSubGenerate;
                if (showSubGenerate) {
                    subGenerator.setVisibility(View.VISIBLE);
                }else{
                    subGenerator.setVisibility(View.GONE);
                }
            }
        });

        btnGeneratorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(MainActivity.this, GenerateTextQRActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QR_SCAN && resultCode == RESULT_OK && data != null) {
            String result = data.getStringExtra("SCAN_RESULT");
            // Ở đây, bạn có thể làm bất kỳ thao tác gì với kết quả quét mã QR như hiển thị lên TextView, xử lý dữ liệu, vv.
        }
    }
}