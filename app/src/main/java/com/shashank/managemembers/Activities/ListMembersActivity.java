package com.shashank.managemembers.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shashank.managemembers.DB.DBHelper;
import com.shashank.managemembers.R;
import com.shashank.managemembers.models.members;
import com.shashank.managemembers.Adapter.membersAdapter;

import java.util.ArrayList;

public class ListMembersActivity extends AppCompatActivity {
    private ListView ListMembers;
    private DBHelper dbHelper;
    private com.shashank.managemembers.Adapter.membersAdapter membersAdapter;
    private TextView empty;

    /**
     * {@inheritDoc}
     * <p>
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        empty = findViewById(R.id.emptyTv);
        ListMembers = findViewById(R.id.List);
        dbHelper = new DBHelper(this, null, 1);
        ArrayList<members> arrayList = dbHelper.listAll();
        membersAdapter = new membersAdapter(ListMembersActivity.this, arrayList);
        ListMembers.setEmptyView(empty);
        ListMembers.setAdapter(membersAdapter);
        ListMembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                members members = (members) ListMembers.getAdapter().getItem(position);
                String memberCode = members.getMemberCode();
                Intent intent = new Intent(ListMembersActivity.this, HistoryActivity.class);
                intent.putExtra("code", memberCode);
                startActivity(intent);
            }
        });
        if (membersAdapter.isEmpty()) {
            empty.setText("Currently there are no members");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_members);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_person);
        MenuItem settings = menu.findItem(R.id.settings);
        settings.setVisible(false);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setQueryHint("Name/Mobile");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<members> arrayList = new ArrayList<>();
                for (members m : dbHelper.listAll()) {
                    if (newText.length() > 0) {
                        if (Character.isDigit(newText.charAt(0))) {
                            if (m.getMobile().startsWith(newText)) {
                                arrayList.add(m);
                            }
                        } else if (m.getName().toLowerCase().contains(newText.toLowerCase())) {
                            arrayList.add(m);
                        }
                    }
                    if (newText.equals("")) {
                        ListMembers.setAdapter(membersAdapter);
                    } else {
                        membersAdapter membersAdapter1 = new membersAdapter(ListMembersActivity.this, arrayList);
                        ListMembers.setAdapter(membersAdapter1);
                    }
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}

