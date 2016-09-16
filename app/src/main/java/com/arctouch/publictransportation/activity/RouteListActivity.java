package com.arctouch.publictransportation.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.arctouch.publictransportation.R;
import com.arctouch.publictransportation.domain.Route;
import com.arctouch.publictransportation.domain.generic.params.StopNameParam;
import com.arctouch.publictransportation.domain.generic.returns.RoutesReturn;
import com.arctouch.publictransportation.fragment.DepartureListFragment;
import com.arctouch.publictransportation.fragment.StopListFragment;
import com.arctouch.publictransportation.interfaces.VolleyListener;
import com.arctouch.publictransportation.volley.VolleySingleton;
import com.arctouch.publictransportation.volley.endpoints.RoutesVolley;

import java.util.List;

public class RouteListActivity extends AppCompatActivity implements VolleyListener<RoutesReturn> {
    private View mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mEmptyList;

    private boolean mMultiplePane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        EditText mTextSearch = (EditText) findViewById(R.id.textSearch);
        mTextSearch.setOnEditorActionListener(searchAction);

        this.mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.mEmptyList = (TextView) findViewById(R.id.emptyList);

        this.mRecyclerView = findViewById(R.id.routeList);
        assert mRecyclerView != null;

        // Only show in large-screen layouts
        if (findViewById(R.id.stopListContainer) != null) {
            mMultiplePane = true;
        }
    }

    private TextView.OnEditorActionListener searchAction = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                loadRoutes(v.getText().toString());
                return true;
            }
            return false;
        }
    };

    private void loadRoutes(String search) {
        this.mProgressBar.setVisibility(View.VISIBLE);
        this.mEmptyList.setVisibility(View.GONE);
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        RoutesVolley routesVolley = new RoutesVolley(this, new StopNameParam(search), this);
        requestQueue.add(routesVolley.getmRequest());
    }

    @Override
    public void onVolleySuccess(RoutesReturn routesReturn) {
        this.mProgressBar.setVisibility(View.GONE);
        this.mEmptyList.setVisibility(routesReturn.getRows().size() > 0 ? View.GONE : View.VISIBLE);
        setupRecyclerView((RecyclerView) this.mRecyclerView, routesReturn);
    }

    @Override
    public void onVolleyError(String error) {
        this.mProgressBar.setVisibility(View.GONE);
        this.mEmptyList.setVisibility(View.GONE);
        AlertDialog dialog = createErrorDialog(error);
        dialog.show();
    }

    private AlertDialog createErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(R.string.exception_retrieve_values_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, RoutesReturn routesReturn) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(routesReturn.getRows()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Route> mRoutes;

        public SimpleItemRecyclerViewAdapter(List<Route> items) {
            mRoutes = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.content_route_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mRoute = mRoutes.get(position);
            holder.mShortName.setText(mRoutes.get(position).getShortName());
            holder.mLongName.setText(mRoutes.get(position).getLongName());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMultiplePane) {
                        Bundle arguments = new Bundle();
                        arguments.putSerializable(StopListFragment.ROUTE, holder.mRoute);

                        StopListFragment stopListFragment = new StopListFragment();
                        stopListFragment.setArguments(arguments);

                        DepartureListFragment departureListFragment = new DepartureListFragment();
                        departureListFragment.setArguments(arguments);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.stopListContainer, stopListFragment)
                                .replace(R.id.departureListContainer, departureListFragment)
                                .commit();

                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, StopListActivity.class);
                        intent.putExtra(StopListFragment.ROUTE, holder.mRoute);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mRoutes.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public Route mRoute;
            public final View mView;
            public final TextView mShortName;
            public final TextView mLongName;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mShortName = (TextView) view.findViewById(R.id.sequence);
                mLongName = (TextView) view.findViewById(R.id.name);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mLongName.getText() + "'";
            }
        }
    }

}
