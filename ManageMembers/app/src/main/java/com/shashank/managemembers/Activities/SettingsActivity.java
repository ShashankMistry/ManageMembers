package com.shashank.managemembers.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.shashank.managemembers.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class SettingsActivity extends AppCompatActivity {
    private final String DATABASE_NAME = "Members.DB";
    protected SharedPreferences sharedPreferences;
    private EditText drink1No, drink2No, drink3No, drink4No, res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView importDB = findViewById(R.id.importDB);
        TextView export = findViewById(R.id.exportDB);
        drink1No = findViewById(R.id.drink1no);
        drink2No = findViewById(R.id.drink2no);
        drink3No = findViewById(R.id.drink3no);
        drink4No = findViewById(R.id.drink4no);
        res = findViewById(R.id.res);
        Button save = findViewById(R.id.save);
        sharedPreferences = getSharedPreferences("Manage", MODE_PRIVATE);
        LoadDrinkLimits();
        export.setOnClickListener(v -> exportDB());

        importDB.setOnClickListener(v -> importDB());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = Integer.parseInt(drink1No.getText().toString()) + Integer.parseInt(drink2No.getText().toString()) + Integer.parseInt(drink3No.getText().toString())
                        + Integer.parseInt(drink4No.getText().toString());
                if (total == 10) {
                    saveNewDrinkLimits();
                    onBackPressed();
                } else {
                    Toast.makeText(SettingsActivity.this, "Only 10 drinks can be offered", Toast.LENGTH_SHORT).show();
                }
            }

            private void saveNewDrinkLimits() {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("coffee", Integer.parseInt(drink1No.getText().toString()));
                editor.putInt("moji", Integer.parseInt(drink2No.getText().toString()));
                editor.putInt("ice", Integer.parseInt(drink3No.getText().toString()));
                editor.putInt("milk", Integer.parseInt(drink4No.getText().toString()));
                editor.putInt("rem", Integer.parseInt(res.getText().toString()));
                editor.apply();
            }
        });
    }

    private void LoadDrinkLimits() {
        drink1No.setText(String.valueOf(sharedPreferences.getInt("coffee", 3)));
        drink2No.setText(String.valueOf(sharedPreferences.getInt("moji", 3)));
        drink3No.setText(String.valueOf(sharedPreferences.getInt("ice", 2)));
        drink4No.setText(String.valueOf(sharedPreferences.getInt("milk", 2)));
        res.setText(String.valueOf(sharedPreferences.getInt("rem",2)));
    }


    public void exportDB() {
        try {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            if (externalStorageDirectory.canWrite()) {
                File file = new File(getDatabasePath(DATABASE_NAME).getPath());
                File file3 = new File(externalStorageDirectory, "/manage_backup/Members.DB");
                FileChannel channel = new FileInputStream(file).getChannel();
                FileChannel channel3 = new FileOutputStream(file3).getChannel();
                channel3.transferFrom(channel, 0, channel.size());
                channel.close();
                channel3.close();
                Toast.makeText(getBaseContext(), "Database has been exported to : Storage/manage_backup", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SettingsActivity.this, "does not have permission", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void importDB(){
        try {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
//            File dataDirectory = Environment.getDataDirectory();
            if (externalStorageDirectory.canWrite()) {
                File file = new File(getDatabasePath(DATABASE_NAME).getPath());
                File file3 = new File(externalStorageDirectory, "/manage_backup/Members.DB");
                FileChannel channel = new FileInputStream(file3).getChannel();
                FileChannel channel3 = new FileOutputStream(file).getChannel();
                channel3.transferFrom(channel, 0, channel.size());
                channel.close();
                channel3.close();
                Toast.makeText(getBaseContext(), "DATA have been imported from the previously backed up database.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception unused) {
            Toast.makeText(getBaseContext(), "Could not locate the required database to import. Please export notes first.", Toast.LENGTH_SHORT).show();
        }

    }
}
