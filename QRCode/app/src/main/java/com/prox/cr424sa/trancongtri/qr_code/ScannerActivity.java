package com.prox.cr424sa.trancongtri.qr_code;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_WRITE_CONTACTS_PERMISSION = 2;
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
        setContentView(R.layout.activity_scanner);

//        textViewResult = findViewById(R.id.textViewResult);
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

//        textViewResult.setText(scanResult);
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
            // Đếm ngược đã kết thúc, cho phép xử lý kết quả mới
            isProcessing = false;
            scannerView.resumeCameraPreview(this);
        }, 3000);
    }

    private boolean isURL(String str) {
        // Kiểm tra xem chuỗi có bắt đầu bằng "http://" hoặc "https://" không
        return str != null && (str.startsWith("http://") || str.startsWith("https://"));
    }

    private void showURLOptions(String url) {
        String message = "Detected URL:\n" + url + "\n\nChoose an action:";
        saveLinkToDB(url);
        new AlertDialog.Builder(this)
                .setTitle("QR Scan Result")
                .setMessage(message)
                .setPositiveButton("Open in Browser", (dialog, which) -> {
                    //TODO: Thực hiện thao tác mở trình duyệt ở đây (nếu cần)
                    openLinkInBrowser(url);
                    finish();
                })
                .setNegativeButton("Copy Link", (dialog, which) -> {
                    //TODO: Thực hiện thao tác sao chép đường dẫn ở đây (nếu cần)
                    finish();
                })
                .setCancelable(true)
                .show();
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
                .setCancelable(true)
                .show();
    }

    private void showPhoneResult(String content) {
        //Lưu vào lịch sử
        saveToContactToDB();

        // Hiển thị popup xác nhận lưu thông tin vào danh bạ
        // Nếu người dùng đồng ý lưu, yêu cầu cấp quyền ghi danh bạ
        new AlertDialog.Builder(this)
                .setTitle("QR Scan Result")
                .setMessage("Do you want to save this contact to your phone book?")
                .setPositiveButton("Save", (dialog, which) -> requestWriteContactsPermission())
                .setNegativeButton("Cancel", (dialog, which) -> finish())
                .setCancelable(true)
                .show();
    }

    private void requestWriteContactsPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // Đã có quyền, lưu thông tin vào danh bạ
            saveToContacts();
        } else {
            // Xin cấp quyền WRITE_CONTACTS tại thời điểm chạy
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
        // Tạo một danh bạ mới
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
            // Thực hiện thêm thông tin vào danh bạ
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }

        // Hoàn thành và hiển thị thông báo thành công
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
                // Người dùng đã cho phép, lưu thông tin vào danh bạ
                saveToContacts();
            } else {
                // Người dùng không cho phép, hiển thị thông báo và kết thúc
                new AlertDialog.Builder(this)
                        .setTitle("Permission Denied")
                        .setMessage("You have denied the permission to save contact.")
                        .setPositiveButton("OK", (dialog, which) -> finish())
                        .setCancelable(false)
                        .show();
            }
        }
    }

    public String extractPhoneNumberFromVCard(String vcard) {
        // Biểu thức chính quy để tìm chuỗi TEL;TYPE=CELL:<số điện thoại>
        String phonePattern = "TEL;TYPE=CELL:(\\d+)";

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
        String phonePattern = "TEL;TYPE=CELL:(\\d+)";

        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcher = pattern.matcher(vcard);

        if (matcher.find()) {
            // Lấy số điện thoại từ nhóm ngoặc trong biểu thức chính quy
            return matcher.group(1).length() > 0;
        } else {
            return false;
        }
    }

//    @SuppressLint("QueryPermissionsNeeded")
    private void openLinkInBrowser(String url) {
        // Tạo một Intent để mở trình duyệt và chuyển đến địa chỉ URL được quét từ mã QR
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        // Kiểm tra xem có ứng dụng trình duyệt nào có sẵn để xử lý Intent này hay không
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Nếu không có ứng dụng trình duyệt nào có sẵn, bạn có thể hiển thị một thông báo hoặc xử lý tùy ý
            Toast.makeText(this, "No browser app found to open the link.", Toast.LENGTH_SHORT).show();
        }
    }
}


