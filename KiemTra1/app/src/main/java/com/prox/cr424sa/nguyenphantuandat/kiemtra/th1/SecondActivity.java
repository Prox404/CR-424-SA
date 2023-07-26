package com.prox.cr424sa.nguyenphantuandat.kiemtra.th1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    TextView message, tvNumber;
    Button checkButton;
    int i_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        checkButton = findViewById(R.id.buttonCheck);
        message = findViewById(R.id.textViewMessage);
        tvNumber = findViewById(R.id.textViewNumber);

        Intent intent = getIntent();
        if (intent != null){
            String number = intent.getStringExtra("number");
            tvNumber.setText(number);
            if (number != null) {
                checkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!number.isEmpty()) {
                            i_number= Integer.parseInt(number);
                            if (isPrime(i_number)) {
                                showToast(number + " là số nguyên tố!");
                            } else {
                                showToast(number + " không phải là số nguyên tố!");
                            }
                        } else {
                            showToast("Vui lòng nhập một số!");
                        }
                    }
                });
            }
        }
    }

    private boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    private void showToast(String messages) {
        message.setText(messages);
    }
}