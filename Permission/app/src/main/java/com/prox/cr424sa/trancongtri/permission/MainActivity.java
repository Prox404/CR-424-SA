package com.prox.cr424sa.trancongtri.permission;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 10 ;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 11 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String sendSMSPermission= Manifest.permission.SEND_SMS;
        String callPermission = Manifest.permission.CALL_PHONE;
        try{
            if (ActivityCompat.checkSelfPermission(this,sendSMSPermission)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{sendSMSPermission, callPermission},MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        phoneCall();
    }

    private void phoneCall(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:0868009674"));
            startActivity(callIntent);
        }else{
            Toast.makeText(this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private  void sendSMS() {
        try {
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage("0868009674", null, "hi cui bap", null, null);
            Toast.makeText(MainActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }



        public boolean checkPermission() {

        int CallPermissionResult = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
        int ContactPermissionResult = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);

        return CallPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ContactPermissionResult == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {
        String sendSMSPermission= Manifest.permission.SEND_SMS;
        String callPermission = Manifest.permission.CALL_PHONE;
        ActivityCompat.requestPermissions(this,new String[]{sendSMSPermission, callPermission},MY_PERMISSIONS_REQUEST_SEND_SMS);

    }

}