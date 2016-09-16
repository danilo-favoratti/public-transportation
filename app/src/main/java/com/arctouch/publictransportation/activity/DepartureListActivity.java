package com.arctouch.publictransportation.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.arctouch.publictransportation.R;
import com.arctouch.publictransportation.domain.Route;
import com.arctouch.publictransportation.fragment.DepartureListFragment;

public class DepartureListActivity extends AppCompatActivity {

    private Route mRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departure_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRoute = (Route) getIntent().getSerializableExtra(DepartureListFragment.ROUTE);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(DepartureListFragment.ROUTE, mRoute);
            arguments.putBoolean(DepartureListFragment.SHOW_ROUTE_DETAILS, true);

            DepartureListFragment fragment = new DepartureListFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.departureListContainer, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
