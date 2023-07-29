package com.prox.cr424sa.trancongtri.bai11;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView tvResult;
    Button btnCalc;
    EditText edtNumberSigned;
    ImageView imageView;

    String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        phuongPhap1();
//        tvResult = findViewById(R.id.tv_Result);
//        edtNumberSigned = findViewById(R.id.editTextNumberSigned);
//        btnCalc = findViewById(R.id.btn_calc);

//        btnCalc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int number = Integer.parseInt(edtNumberSigned.getText().toString());
//                CalculatePrimesTask calculatePrimesTask = new CalculatePrimesTask();
//                calculatePrimesTask.execute(number);
//            }
//        });
    }

    public void phuongPhap1(View view) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://scontent.fsgn5-11.fna.fbcdn.net/v/t1.6435-9/97387265_911934715945271_6195268394929881088_n.jpg?_nc_cat=111&cb=99be929b-59f725be&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=QOKT2I6kmmAAX-ukmZc&_nc_ht=scontent.fsgn5-11.fna&oh=00_AfCi-EAWd5FXR5j12y5q3ITw9bE31C0T_OLkeUfsCt_MeA&oe=64EAE0DA";
                final Bitmap bitmap = downloadImage(url);
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        });

        thread.start();
    }

    public Bitmap downloadImage(String url){
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }



//    private boolean isPrime(int number) {
//        if (number <= 1) {
//            return false;
//        }
//
//        for (int i = 2; i <= Math.sqrt(number); i++) {
//            if (number % i == 0) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private class CalculatePrimesTask extends AsyncTask<Integer, Void, String> {
//
//        @Override
//        protected String doInBackground(Integer... params) {
//            int number = params[0];
//            ArrayList<Integer> primeNumbers = new ArrayList<>();
//
//            for (int i = 1; i <= number; i++) {
//                if (isPrime(i)) {
//                    primeNumbers.add(i);
//                }
//            }
//
//            return primeNumbers.toString();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
////            tvResult.setText(result);
//            Log.i("calc", result);
//        }
//    }
}