package com.shashank.managemembers.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.shashank.managemembers.DB.DBHelper;
import com.shashank.managemembers.R;
import com.shashank.managemembers.Adapter.historyAdapter;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        String CODE = getIntent().getStringExtra("code");
        DBHelper dbHelper = new DBHelper(this,null,1);
        ListView historyListView = findViewById(R.id.historyLV);
        TextView emptyView = findViewById(R.id.empty);
        historyListView.setEmptyView(emptyView);
        historyAdapter historyAdapter = new historyAdapter(this,dbHelper.getHistory(CODE));
        if (historyAdapter.isEmpty()){
            emptyView.setText("This person has no buying history");
        }
        historyListView.setAdapter(historyAdapter);
    }
}