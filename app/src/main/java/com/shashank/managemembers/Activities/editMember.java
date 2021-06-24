package com.shashank.managemembers.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shashank.managemembers.DB.DBHelper;
import com.shashank.managemembers.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class editMember extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);
        EditText name = findViewById(R.id.nameEdit);
        EditText lastName = findViewById(R.id.lastEdit);
        EditText mobile = findViewById(R.id.mobileEdit);
        EditText dob = findViewById(R.id.dobEdit1);
        Button update = findViewById(R.id.update);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String code = intent.getStringExtra("CODE");
        DBHelper dbHelper = new DBHelper(this, null,1);
        dbHelper.fetchMember(code,name,lastName,mobile,dob);
        update.setOnClickListener(v -> {
            try {
                if (name.getText().toString().equals("") || lastName.getText().toString().equals("") || mobile.getText().toString().equals("") || mobile.getText().toString().length() < 10 || mobile.getText().toString().length() > 10) {
                    if (name.getText().toString().equals("")) {
                        name.setError("Name is Required");
                    }
                    if (lastName.getText().toString().equals("")) {
                        lastName.setError("Last name is Required");
                    }
                    if (mobile.getText().toString().equals("") || mobile.getText().toString().length() < 10 || mobile.getText().toString().length() > 10) {
                        mobile.setError("Wrong number");
                    }
                    if (dob.getText().toString().equals("")){
                        dob.setError("Wrong date");
                    }
                }else{
                    dbHelper.updateMember(code, name.getText().toString(), lastName.getText().toString(), mobile.getText().toString(), dob.getText().toString());
                    exportDB();
                    onBackPressed();
                }
            }catch ( Exception e){
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
                Toast.makeText(editMember.this, "does not have permission", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}