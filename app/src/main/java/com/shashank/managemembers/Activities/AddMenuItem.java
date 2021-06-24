package com.shashank.managemembers.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.shashank.managemembers.DB.DBHelper;
import com.shashank.managemembers.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class AddMenuItem extends AppCompatActivity {

    DBHelper dbHelper;
    ArrayList<String> arrayList;
    ArrayAdapter<String> menuAdapter;
    SwipeMenuListView listView;
    @Override
    protected void onResume() {
        super.onResume();
        dbHelper = new DBHelper(this, null, 1);
        arrayList = new ArrayList<>();
        dbHelper.listMenu(arrayList);
        menuAdapter = new ArrayAdapter<>(this, R.layout.textview, R.id.lvTv, arrayList);
        listView.setAdapter(menuAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        listView = (SwipeMenuListView) findViewById(R.id.listView);
        Spinner spinner = findViewById(R.id.spinner);
        Button button = findViewById(R.id.addItem);
        EditText newItem = findViewById(R.id.newItem);
        String[] type = new String[]{"Coffee","MilkShakes","Mojito","Ice tea"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddMenuItem.this, R.layout.textview, R.id.lvTv,type);
        spinner.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!newItem.getText().toString().equals("")) {
                    try {
                        dbHelper.insertMenu(spinner.getSelectedItem().toString(), newItem.getText().toString().toUpperCase());
                        arrayList.clear();
                        dbHelper.listMenu(arrayList);
                        menuAdapter = new ArrayAdapter<>(AddMenuItem.this, R.layout.textview, R.id.lvTv, arrayList);
                        listView.setAdapter(menuAdapter);
                        newItem.setText("");
                    }catch (Exception e) {
                        Toast.makeText(AddMenuItem.this, "item is already in the menu", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddMenuItem.this, "type something...", Toast.LENGTH_SHORT).show();
                }
                exportDB();
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (index == 0) {// open
                    String[] item = listView.getAdapter().getItem(position).toString().split(": ");
                    dbHelper.removeItem(item[2]);
                    arrayList.clear();
                    dbHelper.listMenu(arrayList);
                    menuAdapter = new ArrayAdapter<>(AddMenuItem.this, R.layout.textview, R.id.lvTv, arrayList);
                    listView.setAdapter(menuAdapter);
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_person);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setQueryHint("Category");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                (AddMenuItem.this).menuAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(AddMenuItem.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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
                Toast.makeText(AddMenuItem.this, "does not have permission", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}