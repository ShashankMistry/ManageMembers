package com.shashank.managemembers.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.shashank.managemembers.DB.DBHelper;
import com.shashank.managemembers.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserSelectionActivity extends AppCompatActivity {
    EditText firstName, lastName, mobile, dob;
    DBHelper dbHelper;
    ListView coffee, mojito, milk_shakes, ice_tea;
    Button submit;
    String CODE;
    String joinDate;
    TextView shakes, iceTea, Moji, soda_and_sharbhat;
    boolean renew;
    int MAX_SELECTABLE_SODA, MAX_SELECTABLE_MOJI, MAX_SELECTABLE_ICE, MAX_SELECTABLE_MILK, VARIABLE = 0, MAX_SELECTABLE = 10, REMOVE;
    ArrayList<String> COFFEE = new ArrayList<>();
    ArrayList<String> MOJITO = new ArrayList<>();
    ArrayList<String> MILK = new ArrayList<>();
    ArrayList<String> ICE = new ArrayList<>();
    ArrayList<String> COFFEE1 = new ArrayList<>();
    ArrayList<String> MOJITO1 = new ArrayList<>();
    ArrayList<String> MILK1 = new ArrayList<>();
    ArrayList<String> ICE1 = new ArrayList<>();

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);
        findID();
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        dbHelper = new DBHelper(this, null, 1);
        Intent intent = getIntent();
        CODE = intent.getStringExtra("CODE");
        //
        renew = dbHelper.fetchMember(CODE, firstName, lastName, mobile, dob);
        if (renew) {
            firstName.setEnabled(false);
            lastName.setEnabled(false);
            mobile.setEnabled(false);
            dob.setEnabled(false);
        }
        //
        Date now = new Date();
        joinDate = new SimpleDateFormat("dd-MM-yyyy").format(now);
        //
        coffee.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        milk_shakes.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        ice_tea.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mojito.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        //
        String[] type = new String[]{"Coffee", "MilkShakes" , "Mojito", "Ice tea"};
        //
        loadPreferences();
        //
        dbHelper.fetchMenu(type[0], COFFEE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.checkbox_lv, R.id.CTV, COFFEE);
        int sodaSize = adapter.getCount();
        coffee.setAdapter(adapter);

        dbHelper.fetchMenu(type[1], MILK);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, R.layout.checkbox_lv, R.id.CTV, MILK);
        int milkSize = adapter3.getCount();
        milk_shakes.setAdapter(adapter3);

        dbHelper.fetchMenu(type[2], MOJITO);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.checkbox_lv, R.id.CTV, MOJITO);
        int mojitoSize = adapter2.getCount();
        mojito.setAdapter(adapter2);

        dbHelper.fetchMenu(type[3], ICE);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, R.layout.checkbox_lv, R.id.CTV, ICE);
        int iceSize = adapter4.getCount();
        ice_tea.setAdapter(adapter4);


        ViewGroup.LayoutParams params = coffee.getLayoutParams();
        params.height = (int) (sodaSize * 125);
        coffee.setLayoutParams(params);
        coffee.requestLayout();


        ViewGroup.LayoutParams params1 = mojito.getLayoutParams();
        params1.height = (int) (mojitoSize * 125);
        mojito.setLayoutParams(params1);
        mojito.requestLayout();

        ViewGroup.LayoutParams params2 = milk_shakes.getLayoutParams();
        params2.height = (int) (milkSize * 125);
        milk_shakes.setLayoutParams(params2);
        milk_shakes.requestLayout();

        ViewGroup.LayoutParams params4 = ice_tea.getLayoutParams();
        params4.height = (int) (iceSize * 125);
        ice_tea.setLayoutParams(params4);
        ice_tea.requestLayout();


        coffee.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) coffee.getItemAtPosition(position);
            if (COFFEE1.contains(selected)) {
                if (selected.contains("PREMIUM")) {
                    COFFEE1.remove(selected);
                    MAX_SELECTABLE = MAX_SELECTABLE + REMOVE;
                    Toast.makeText(this, REMOVE+"", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(this, MAX_SELECTABLE + "", Toast.LENGTH_SHORT).show();
                } else {
                    COFFEE1.remove(selected);
                }
                coffee.setItemChecked(position, false);
                VARIABLE--;
            } else if (COFFEE1.size() < MAX_SELECTABLE_SODA && VARIABLE < MAX_SELECTABLE) {
                if (selected.contains("PREMIUM")) {
                    if (VARIABLE >= MAX_SELECTABLE - REMOVE) {
                        coffee.setItemChecked(position, false);
                    } else {
                        COFFEE1.add(selected);
                        MAX_SELECTABLE = MAX_SELECTABLE - REMOVE;
//                        Toast.makeText(this, MAX_SELECTABLE + "", Toast.LENGTH_SHORT).show();
                        coffee.setItemChecked(position, true);
                        VARIABLE++;
                    }
                } else {
                    COFFEE1.add(selected);
                    coffee.setItemChecked(position, true);
                    VARIABLE++;
                }
            } else {
                soda_and_sharbhat.setError("You reached maximum limit");
                new Handler().postDelayed(() -> soda_and_sharbhat.setError(null), 3000);
                coffee.setItemChecked(position, false);
            }
        });
        mojito.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) mojito.getItemAtPosition(position);
            if (MOJITO1.contains(selected)) {
                if (selected.contains("PREMIUM")) {
                    MOJITO1.remove(selected);
                    MAX_SELECTABLE = MAX_SELECTABLE + REMOVE;
//                    Toast.makeText(this, MAX_SELECTABLE + "", Toast.LENGTH_SHORT).show();
                } else {
                    MOJITO1.remove(selected);
                }
                mojito.setItemChecked(position, false);
                VARIABLE--;
            } else if (MOJITO1.size() < MAX_SELECTABLE_MOJI && VARIABLE < MAX_SELECTABLE) {
                if (selected.contains("PREMIUM")) {
                    if (VARIABLE >= MAX_SELECTABLE - REMOVE) {
                        mojito.setItemChecked(position, false);
                    } else {
                        MOJITO1.add(selected);
                        MAX_SELECTABLE = MAX_SELECTABLE - REMOVE;
//                        Toast.makeText(this, MAX_SELECTABLE + "", Toast.LENGTH_SHORT).show();
                        mojito.setItemChecked(position, true);
                        VARIABLE++;
                    }
                } else {
                    MOJITO1.add(selected);
                    mojito.setItemChecked(position, true);
                    VARIABLE++;
                }
            } else {
                Moji.setError("You reached maximum limit");
                new Handler().postDelayed(() -> Moji.setError(null), 3000);
                mojito.setItemChecked(position, false);
            }
        });
        milk_shakes.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) milk_shakes.getItemAtPosition(position);
            if (MILK1.contains(selected)) {
                if (selected.contains("PREMIUM")) {
                    MILK1.remove(selected);
                    MAX_SELECTABLE = MAX_SELECTABLE + REMOVE;
//                    Toast.makeText(this, MAX_SELECTABLE + "", Toast.LENGTH_SHORT).show();
                } else {
                    MILK1.remove(selected);
                }
                milk_shakes.setItemChecked(position, false);
                VARIABLE--;
            } else if (MILK1.size() < MAX_SELECTABLE_MILK && VARIABLE < MAX_SELECTABLE) {
                if (selected.contains("PREMIUM")) {
                    if (VARIABLE >= MAX_SELECTABLE - REMOVE) {
                        milk_shakes.setItemChecked(position, false);
                    } else {
                        MILK1.add(selected);
                        MAX_SELECTABLE = MAX_SELECTABLE - REMOVE;
//                        Toast.makeText(this, MAX_SELECTABLE + "", Toast.LENGTH_SHORT).show();
                        milk_shakes.setItemChecked(position, true);
                        VARIABLE++;
                    }
                } else {
                    MILK1.add(selected);
                    milk_shakes.setItemChecked(position, true);
                    VARIABLE++;
                }
            } else {
                shakes.setError("You reached maximum limit");
                new Handler().postDelayed(() -> shakes.setError(null), 3000);
                milk_shakes.setItemChecked(position, false);
            }
        });

        ice_tea.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) ice_tea.getItemAtPosition(position);
            if (ICE1.contains(selected)) {
                if (selected.contains("PREMIUM")) {
                    ICE1.remove(selected);
                    MAX_SELECTABLE = MAX_SELECTABLE + REMOVE;
//                    Toast.makeText(this, MAX_SELECTABLE + "", Toast.LENGTH_SHORT).show();
                } else {
                    ICE1.remove(selected);
                }
                ice_tea.setItemChecked(position, false);
                VARIABLE--;
            } else if (ICE1.size() < MAX_SELECTABLE_ICE && VARIABLE < MAX_SELECTABLE) {
                if (selected.contains("PREMIUM")) {
                    if (VARIABLE >= MAX_SELECTABLE - REMOVE) {
                        ice_tea.setItemChecked(position, false);
                    } else {
                        ICE1.add(selected);
                        MAX_SELECTABLE = MAX_SELECTABLE - REMOVE;
//                        Toast.makeText(this, MAX_SELECTABLE + "", Toast.LENGTH_SHORT).show();
                        ice_tea.setItemChecked(position, true);
                        VARIABLE++;
                    }
                } else {
                    ICE1.add(selected);
                    ice_tea.setItemChecked(position, true);
                    VARIABLE++;
                }
            } else {
                iceTea.setError("You reached maximum limit");
                new Handler().postDelayed(() -> iceTea.setError(null), 3000);
                ice_tea.setItemChecked(position, false);
            }
        });

        ArrayList<String> drinks = new ArrayList<>();
        SmsManager mySmsManager = SmsManager.getDefault();
        submit.setOnClickListener(v -> {
            if (firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || mobile.getText().toString().equals("") || dob.getText().toString().equals("") || mobile.getText().toString().length() < 10 || mobile.getText().toString().length() > 10) {
                if (firstName.getText().toString().equals("")) {
                    firstName.setError("Name is Required");
                }
                if (lastName.getText().toString().equals("")) {
                    lastName.setError("Last name is Required");
                }
                if (mobile.getText().toString().equals("") || mobile.getText().toString().length() < 10 || mobile.getText().toString().length() > 10) {
                    mobile.setError("Wrong number");
                }
                if (dob.getText().toString().equals("")) {
                    dob.setError("enter birth date");
                }
            } else if (VARIABLE < MAX_SELECTABLE) {
                Toast.makeText(UserSelectionActivity.this, "Select Drinks", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    if (!renew) {
                        drinks.addAll(COFFEE1);
                        drinks.addAll(MOJITO1);
                        drinks.addAll(ICE1);
                        drinks.addAll(MILK1);
                        if (dob.getText().toString().contains("/")) {
                            dbHelper.insertMember(CODE, firstName.getText().toString(),
                                    lastName.getText().toString(),
                                    Long.parseLong(mobile.getText().toString()), joinDate, dob.getText().toString().replace("/", "-"), drinks);
                        } else {
                            dbHelper.insertMember(CODE, firstName.getText().toString(),
                                    lastName.getText().toString(),
                                    Long.parseLong(mobile.getText().toString()), joinDate, dob.getText().toString(), drinks);
                        }
                        exportDB();
                        String message = "Thank you for becoming member. \nnow you have " + dbHelper.drinkNo(CODE) + " drinks.\nHappy Drinking!!";
                        mySmsManager.sendTextMessage(mobile.getText().toString(),null, message , null, null);
                    } else {
                        drinks.addAll(COFFEE1);
                        drinks.addAll(MOJITO1);
                        drinks.addAll(ICE1);
                        drinks.addAll(MILK1);
//                    Toast.makeText(this, drinks.size() + "", Toast.LENGTH_SHORT).show();
                        dbHelper.renewMembership(CODE, drinks);
                        String message = "Successfully renewed your membership.\nnow you have "+dbHelper.drinkNo(CODE)+" drinks.";
                        mySmsManager.sendTextMessage(mobile.getText().toString(),null, message , null, null);
                        exportDB();
                    }
                    onBackPressed();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("Manage", MODE_PRIVATE);
        MAX_SELECTABLE_SODA = sharedPreferences.getInt("coffee", 3);
        MAX_SELECTABLE_MOJI = sharedPreferences.getInt("moji", 3);
        MAX_SELECTABLE_ICE = sharedPreferences.getInt("ice", 2);
        MAX_SELECTABLE_MILK = sharedPreferences.getInt("milk", 2);
        REMOVE = sharedPreferences.getInt("rem",2);
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
                Toast.makeText(UserSelectionActivity.this, "does not have permission", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    public void findID() {
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        mobile = findViewById(R.id.mobile);
        dob = findViewById(R.id.dobEdit);
        submit = findViewById(R.id.SUBMIT);
        iceTea = findViewById(R.id.iceTea);
        soda_and_sharbhat = findViewById(R.id.soda);
        coffee = findViewById(R.id.sodaLv);
        Moji = findViewById(R.id.Mojito);
        mojito = findViewById(R.id.mojitoLv);
        shakes = findViewById(R.id.milk);
        milk_shakes = findViewById(R.id.milkLv);
        ice_tea = findViewById(R.id.iceTeaLv);

    }
}