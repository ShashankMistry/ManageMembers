package com.shashank.managemembers.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.shashank.managemembers.DB.DBHelper;
import com.shashank.managemembers.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class BuyActivity extends AppCompatActivity {
    private  ArrayList<String> checkedItems = new ArrayList<>();
    private ArrayList<String> selectedInListView = new ArrayList<>();
    private StringBuilder order = new StringBuilder();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        TextView drinks = findViewById(R.id.textView);
        TextView name = findViewById(R.id.nameSelection);
        TextView mobile = findViewById(R.id.MobileSelection);
        TextView dob = findViewById(R.id.dboTV);
        ListView leftDrinks = findViewById(R.id.lv);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        leftDrinks.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        Button button = findViewById(R.id.done);
        DBHelper dbHelper = new DBHelper(this, null, 1);
        Intent intent = getIntent();
        String code = intent.getStringExtra("member");
//        Toast.makeText(this, dbHelper.drinkNo(code)+"", Toast.LENGTH_SHORT).show();
        dbHelper.availableDrinks(code, checkedItems, name, mobile, dob);
        drinks.append(checkedItems.size() + "");
//        Toast.makeText(this,checkedItems.size() + "", Toast.LENGTH_SHORT).show();
        ArrayAdapter<String> RemainingDrinks = new ArrayAdapter<>(this, R.layout.checkbox_lv, R.id.CTV, checkedItems);
        leftDrinks.setAdapter(RemainingDrinks);
        //
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String time = dateFormat.format(cal.getTime());

        Date now = new Date();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(now);
        //
        leftDrinks.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) leftDrinks.getItemAtPosition(position);
            if (selectedInListView.contains(selectedItem)) {
                selectedInListView.remove(selectedItem);
            } else {
                selectedInListView.add(selectedItem);
            }
        });

        int noOfLeftDrinks = checkedItems.size();
        if (noOfLeftDrinks == 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(BuyActivity.this);
            builder.setTitle("Membership Expired!!");
            builder.setMessage("Renew your Membership");
            builder.setCancelable(true);
            builder.setNegativeButton("Renew", (dialog, which) -> {
                Intent intent1 = new Intent(BuyActivity.this, UserSelectionActivity.class);
                intent1.putExtra("CODE", code);
                startActivity(intent1);
                finish();
            });
            builder.setPositiveButton("cancel", (dialog, which) -> {
                dialog.cancel();
                onBackPressed();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
//
        button.setOnClickListener(v -> {
            try {
                SmsManager mySmsManager = SmsManager.getDefault();
//            StringBuilder message1 = new StringBuilder(");
                StringBuilder message = new StringBuilder("Thank you for visiting Sharbat Wala. \nYour Order summery:\n");
                String[] left = drinks.getText().toString().split(": ");
                for (int i = 0; i < selectedInListView.size(); i++) {
                    message.append(selectedInListView.get(i)).append("\n");
                }
                if ((Integer.parseInt(left[1]) - selectedInListView.size()) != 0) {
                    message.append(Integer.parseInt(left[1]) - selectedInListView.size())
                            .append("/10 drinks are remaining.\n").append("happy Drinking!!");
                }
                dbHelper.updateAvailable(code, selectedInListView);
                ArrayList<String> parts  = mySmsManager.divideMessage(message.toString());
                mySmsManager.sendMultipartTextMessage(mobile.getText().toString(), null, parts, null, null);
//            mySmsManager.sendTextMessage(mobile.getText().toString(), null, message.toString(), null, null);
                if ((Integer.parseInt(left[1]) - selectedInListView.size()) == 0) {
                    mySmsManager.sendTextMessage(mobile.getText().toString(), null, "Your membership is expired, you can renew your membership at sharbat wala shop.", null, null);
                }
                //
                String[] nameHistory = name.getText().toString().split(": ");
                String[] mobileHistory = mobile.getText().toString().split(": ");
                for (int i = 0; i < selectedInListView.size() ; i++){
                    order.append(selectedInListView.get(i)).append(",");
                }
                dbHelper.insertHistory(code,nameHistory[1], mobileHistory[1], order.toString(), time, date);
                //
                exportDB();
                onBackPressed();
            } catch (Exception e){
                Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
            }

        });
    }
    private void exportDB() {
        try {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            if (externalStorageDirectory.canWrite()) {
                String DATABASE_NAME = "Members.DB";
                File file = new File(getDatabasePath(DATABASE_NAME).getPath());
                File file3 = new File(externalStorageDirectory, "/manage_backup/Members.DB");
                FileChannel channel = new FileInputStream(file).getChannel();
                FileChannel channel3 = new FileOutputStream(file3).getChannel();
                channel3.transferFrom(channel, 0, channel.size());
                channel.close();
                channel3.close();
//                Toast.makeText(getBaseContext(), "Database has been exported to : Storage/Sharbat_Wala_backup", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BuyActivity.this, "does not have permission", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}