package com.shashank.managemembers.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.shashank.managemembers.DB.DBHelper;
import com.shashank.managemembers.FragmentDialog.OfferDialog;
import com.shashank.managemembers.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, OfferDialog.OnInputListener {
    private ZXingScannerView scannerView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);
        Window window = getWindow();
        window.setNavigationBarColor(Color.BLACK);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        dbHelper = new DBHelper(this, null,1);
        File file;
        file = new File(Environment.getExternalStorageDirectory(),"manage_backup");
        if (!file.exists()) {
            file.mkdirs();
        }
        Dexter.withContext(getApplicationContext())
                .withPermissions(Manifest.permission.CAMERA,  Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                }
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
//                    Toast.makeText(MainActivity.this, "pls allow", Toast.LENGTH_SHORT).show();
                    showSettingsDialog();
                }
            }
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();

        scannerView.setOnClickListener(view -> {
            if (scannerView == null){
                scannerView=new ZXingScannerView(this);
            }
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permission Required", Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("To grant permission press open settings");
                builder.setCancelable(true);
                builder.setNegativeButton("CLOSE", (dialog, which) -> {
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                });
                builder.setPositiveButton("OPEN SETTINGS", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            if(rawResult.getText().startsWith("drInk101")){
                if (!dbHelper.fetchMember(rawResult.getText())){
                    Intent intent = new Intent(MainActivity.this,UserSelectionActivity.class);
                    intent.putExtra("CODE",rawResult.getText());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this, BuyActivity.class);
                    intent.putExtra("member",rawResult.getText());
                    startActivity(intent);
                }
            }else{
                Toast.makeText(MainActivity.this, "not valid", Toast.LENGTH_SHORT).show();
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
        }catch (Exception e){
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && scannerView != null) {
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.members:
                Intent intent = new Intent(MainActivity.this, ListMembersActivity.class);
                startActivity(intent);
                return true;
            case R.id.add:
                Intent intent1 = new Intent(MainActivity.this, AddMenuItem.class);
                startActivity(intent1);
                return true;
            case R.id.smsMenu:
//                scannerView.stopCameraPreview();
                OfferDialog offerDialog = new OfferDialog();
                offerDialog.show(getSupportFragmentManager(),"Offer");
                return true;
            default:
             return super.onOptionsItemSelected(item);
        }
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void sendInput(String send) {
//        scannerView.setResultHandler(MainActivity.this);
//        scannerView.startCamera();
        SmsManager mySmsManager = SmsManager.getDefault();
        ArrayList<String> parts  = mySmsManager.divideMessage(send.toString());
        for (String p : dbHelper.getPhoneNumbers()){
        mySmsManager.sendMultipartTextMessage(p, null, parts, null, null);
        }
    }
}