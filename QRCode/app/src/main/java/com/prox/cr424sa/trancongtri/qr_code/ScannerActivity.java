package com.prox.cr424sa.trancongtri.qr_code;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScannerActivity extends BaseActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_WRITE_CONTACTS_PERMISSION = 2;
    private static final int REQUEST_CALL_PHONE_PERMISSION = 11;
    private ZXingScannerView scannerView;
    private boolean isPopupShown = false;
    private boolean isProcessing = false;
    private boolean flash = false;
    private String scannedContent;
    ImageButton btnFlash;

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scanner);

        scannerView = findViewById(R.id.zxingScannerView);;
        btnFlash = findViewById(R.id.btn_flash);
        db = new Database(this);

        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flash = !flash;
                Log.i("flash", String.valueOf(flash));
                scannerView.setFlash(flash);
            }
        });

//        setContentView(scannerView);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

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
            scannedContent = scanResult;

            if (isURL(scanResult)) {
                if (!isPopupShown) {
                    showURLOptions(scanResult);
                    isPopupShown = true;
                }
            } else if (isPhoneNumber(scanResult)) {
                if (!isPopupShown) {
                    showPhoneResult(scanResult);
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
            isProcessing = false;
            scannerView.resumeCameraPreview(this);
        }, 3000);
    }

    private boolean isURL(String str) {
        return str != null && (str.startsWith("http://") || str.startsWith("https://"));
    }

    private void showURLOptions(String url) {
        String message = "Detected URL:\n" + url + "\n\nChoose an action:";
        saveLinkToDB(url);
        new AlertDialog.Builder(this)
                .setTitle("QR Scan Result")
                .setMessage(message)
                .setPositiveButton("Open in Browser", (dialog, which) -> {
                    openLinkInBrowser(url);
                    finish();
                })
                .setNegativeButton("Copy Link", (dialog, which) -> {
                    copyToClipboard(url);
                    finish();
                })
                .setCancelable(true)
                .show();
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Text", text);
        clipboard.setPrimaryClip(clip);
    }

    private void showURLText(String text) {
        Log.i("Content", text);
        String message = "Detected text:\n" + text + "\n\n";
        saveTextToDB(text);
        new AlertDialog.Builder(this)
                .setTitle("QR Scan Result")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("Copy", (dialog, which) -> {
                    copyToClipboard(text);
                    finish();
                })
                .setCancelable(true)
                .show();
    }

    private void showPhoneResult(String content) {
        saveToContactToDB();

        new AlertDialog.Builder(this)
                .setTitle("QR Scan Result")
                .setMessage("Do you want to save this contact to your phone book?")
                .setPositiveButton("Save", (dialog, which) -> requestWriteContactsPermission())
                .setNegativeButton("Cancel", (dialog, which) -> finish())
                .setNeutralButton("Call", (dialog, which) -> makePhoneCall())
                .setCancelable(true)
                .show();
    }

    private void makePhoneCall() {
        // Replace "phoneNumber" with the actual phone number extracted from the QR code
        String phoneNumber = extractPhoneNumberFromVCard(scannedContent);

        // Check if the device supports making phone calls
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // If the permission is granted, initiate the call
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        } else {
            // If the permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
        }
    }

    private void requestWriteContactsPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            saveToContacts();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, REQUEST_WRITE_CONTACTS_PERMISSION);
        }
    }

    private void saveLinkToDB(String link){
        db.insertQR(null,link,null,null,null,null,null,null);
    }

    private void saveTextToDB(String text){
        db.insertQR(text,null,null,null,null,null,null,null);
    }

    private void saveToContactToDB(){
        String namePattern = "N:(.*?);(.*?)\n";
        String orgPattern = "ORG:(.*?)\n";
        String cellPhonePattern = "TEL;TYPE=CELL:(.*?)\n";
        String phonePattern = "TEL:(.*?)\n";
        String urlPattern = "URL:(.*?)\n";
        String faxPattern = "TEL;TYPE=FAX:(.*?)\n";
        String addressPattern = "ADR:(.*?)\n";
        String emailPattern = "EMAIL;TYPE=INTERNET:(.*?)\n";

        Pattern namePat = Pattern.compile(namePattern);
        Pattern orgPat = Pattern.compile(orgPattern);
        Pattern cellPhonePat = Pattern.compile(cellPhonePattern);
        Pattern phonePat = Pattern.compile(phonePattern);
        Pattern faxPat = Pattern.compile(faxPattern);
        Pattern addressPat = Pattern.compile(addressPattern);
        Pattern emailPat = Pattern.compile(emailPattern);
        Pattern urlPat = Pattern.compile(urlPattern);

        Matcher nameMatcher = namePat.matcher(scannedContent);
        Matcher orgMatcher = orgPat.matcher(scannedContent);
        Matcher cellPhoneMatcher = cellPhonePat.matcher(scannedContent);
        Matcher phoneMatcher = phonePat.matcher(scannedContent);
        Matcher faxMatcher = faxPat.matcher(scannedContent);
        Matcher addressMatcher = addressPat.matcher(scannedContent);
        Matcher emailMatcher = emailPat.matcher(scannedContent);
        Matcher urlMatcher = urlPat.matcher(scannedContent);

        String name = null;
        String org = null;
        String cellPhone = null;
        String phone = null;
        String fax = null;
        String address = null;
        String email = null;
        String url = null;

        if (nameMatcher.find()) {
            name = nameMatcher.group(2) + " " + nameMatcher.group(1);
        }

        if (orgMatcher.find()) {
            org = orgMatcher.group(1);
        }

        if (cellPhoneMatcher.find()) {
            cellPhone = cellPhoneMatcher.group(1);
        }

        if (phoneMatcher.find()) {
            phone = phoneMatcher.group(1);
        }

        if (faxMatcher.find()) {
            fax = faxMatcher.group(1);
        }

        if (addressMatcher.find()) {
            address = addressMatcher.group(1);
        }

        if (emailMatcher.find()) {
            email = emailMatcher.group(1);
        }

        if (urlMatcher.find()) {
            url = urlMatcher.group(1);
        }

        db.insertQR(null,null,name,org,phone,address,email,url);
    }

    private void saveToContacts() {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // Lưu tên vào danh bạ

        String namePattern = "N:(.*?);(.*?)\n";
        String orgPattern = "ORG:(.*?)\n";
        String cellPhonePattern = "TEL;TYPE=CELL:(.*?)\n";
        String phonePattern = "TEL:(.*?)\n";
        String urlPattern = "URL:(.*?)\n";
        String faxPattern = "TEL;TYPE=FAX:(.*?)\n";
        String addressPattern = "ADR:(.*?)\n";
        String emailPattern = "EMAIL;TYPE=INTERNET:(.*?)\n";

        Pattern namePat = Pattern.compile(namePattern);
        Pattern orgPat = Pattern.compile(orgPattern);
        Pattern cellPhonePat = Pattern.compile(cellPhonePattern);
        Pattern phonePat = Pattern.compile(phonePattern);
        Pattern faxPat = Pattern.compile(faxPattern);
        Pattern addressPat = Pattern.compile(addressPattern);
        Pattern emailPat = Pattern.compile(emailPattern);
        Pattern urlPat = Pattern.compile(urlPattern);

        Matcher nameMatcher = namePat.matcher(scannedContent);
        Matcher orgMatcher = orgPat.matcher(scannedContent);
        Matcher cellPhoneMatcher = cellPhonePat.matcher(scannedContent);
        Matcher phoneMatcher = phonePat.matcher(scannedContent);
        Matcher faxMatcher = faxPat.matcher(scannedContent);
        Matcher addressMatcher = addressPat.matcher(scannedContent);
        Matcher emailMatcher = emailPat.matcher(scannedContent);
        Matcher urlMatcher = urlPat.matcher(scannedContent);

        String name = null;
        String org = null;
        String cellPhone = null;
        String phone = null;
        String fax = null;
        String address = null;
        String email = null;
        String url = null;

        if (nameMatcher.find()) {
            name = nameMatcher.group(2) + " " + nameMatcher.group(1);
        }

        if (orgMatcher.find()) {
            org = orgMatcher.group(1);
        }

        if (cellPhoneMatcher.find()) {
            cellPhone = cellPhoneMatcher.group(1);
        }

        if (phoneMatcher.find()) {
            phone = phoneMatcher.group(1);
        }

        if (faxMatcher.find()) {
            fax = faxMatcher.group(1);
        }

        if (addressMatcher.find()) {
            address = addressMatcher.group(1);
        }

        if (emailMatcher.find()) {
            email = emailMatcher.group(1);
        }

        if (urlMatcher.find()) {
            url = urlMatcher.group(1);
        }

        Log.i("Phone: ", scannedContent);

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, org)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, address)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Website.URL, url)
                .build());

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }

        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Contact has been saved to your phone book.")
                .setPositiveButton("OK", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_CONTACTS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveToContacts();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Denied")
                        .setMessage("You have denied the permission to save contact.")
                        .setPositiveButton("OK", (dialog, which) -> finish())
                        .setCancelable(false)
                        .show();
            }
        }

        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If the call phone permission is granted after the request, make the phone call
                makePhoneCall();
            } else {
                // If the permission is denied, show a message or handle accordingly
                Toast.makeText(this, "Call Phone Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String extractPhoneNumberFromVCard(String vcard) {
        // Biểu thức chính quy để tìm chuỗi TEL;TYPE=CELL:<số điện thoại>
        String phonePattern = "TEL:(\\d+)";

        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcher = pattern.matcher(vcard);

        if (matcher.find()) {
            // Lấy số điện thoại từ nhóm ngoặc trong biểu thức chính quy
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public boolean isPhoneNumber(String vcard) {
        // Biểu thức chính quy để tìm chuỗi TEL;TYPE=CELL:<số điện thoại>
        String phonePattern = "TEL:(\\d+)";

        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcher = pattern.matcher(vcard);

        if (matcher.find()) {
            return matcher.group(1).length() > 0;
        } else {
            return false;
        }
    }

//    @SuppressLint("QueryPermissionsNeeded")
    private void openLinkInBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No browser app found to open the link.", Toast.LENGTH_SHORT).show();
        }
    }
}


