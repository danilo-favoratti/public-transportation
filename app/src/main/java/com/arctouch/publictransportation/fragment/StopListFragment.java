package com.arctouch.publictransportation.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.arctouch.publictransportation.R;
import com.arctouch.publictransportation.activity.DepartureListActivity;
import com.arctouch.publictransportation.domain.Route;
import com.arctouch.publictransportation.domain.Stop;
import com.arctouch.publictransportation.domain.generic.params.RouteIdParam;
import com.arctouch.publictransportation.domain.generic.returns.StopsReturn;
import com.arctouch.publictransportation.interfaces.VolleyListener;
import com.arctouch.publictransportation.volley.VolleySingleton;
import com.arctouch.publictransportation.volley.endpoints.StopsVolley;

import java.util.List;

public class StopListFragment extends Fragment implements VolleyListener<StopsReturn> {
    public static final String ROUTE = "route";
    public static final String SHOW_ROUTE_DETAILS = "show_route_details";

    private View mRootView;
    private View mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mEmptyList;
    private Button mSeeDepartures;

    private boolean mShowRouteDetails;

    private Route mRoute;

    public StopListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ROUTE)) {
            mShowRouteDetails = getArguments().containsKey(SHOW_ROUTE_DETAILS) &&
                    getArguments().getBoolean(SHOW_ROUTE_DETAILS);

            mRoute = (Route) getArguments().getSerializable((ROUTE));
        } else {
            throw new IllegalArgumentException("StopListFragment needs an argument type Route.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.mRootView = inflater.inflate(R.layout.layout_stop_list, container, false);

        this.mRecyclerView = mRootView.findViewById(R.id.stopList);
        this.mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        this.mEmptyList = (TextView) mRootView.findViewById(R.id.emptyList);
        this.mSeeDepartures = (Button) mRootView.findViewById(R.id.seeDepartures);

        TextView routeName = (TextView) mRootView.findViewById(R.id.routeName);
        View seeDepartures = (View) mRootView.findViewById(R.id.seeDepartures);

        routeName.setVisibility(mShowRouteDetails ? View.VISIBLE : View.GONE);
        seeDepartures.setVisibility(mShowRouteDetails ? View.VISIBLE : View.GONE);

        if (mShowRouteDetails) {
            routeName.setText(mRoute.getLongName());
        }

        this.mSeeDepartures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, DepartureListActivity.class);
                intent.putExtra(StopListFragment.ROUTE, mRoute);
                context.startActivity(intent);
            }
        });

        loadStops();

        return mRootView;
    }

    private void loadStops() {
        this.mProgressBar.setVisibility(View.VISIBLE);
        this.mEmptyList.setVisibility(View.GONE);
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getActivity()).getRequestQueue();
        StopsVolley stopsVolley = new StopsVolley(this.getContext(), new RouteIdParam(mRoute.getId()), this);
        requestQueue.add(stopsVolley.getmRequest());
    }

    @Override
    public void onVolleySuccess(StopsReturn stopsReturn) {
        this.mProgressBar.setVisibility(View.GONE);
        this.mEmptyList.setVisibility(stopsReturn.getRows().size() > 0 ? View.GONE : View.VISIBLE);
        setupRecyclerView((RecyclerView) this.mRecyclerView, stopsReturn.getRows());
    }

    @Override
    public void onVolleyError(String error) {
        this.mProgressBar.setVisibility(View.GONE);
        this.mEmptyList.setVisibility(View.GONE);
        AlertDialog dialog = createErrorDialog(error);
        dialog.show();
    }

    private AlertDialog createErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage(message)
                .setTitle(R.string.exception_retrieve_values_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Stop> stops) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(stops));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Stop> mStops;

        public SimpleItemRecyclerViewAdapter(List<Stop> items) {
            mStops = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.content_stop_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mStop = mStops.get(position);
            holder.mSequence.setText(mStops.get(position).getSequence().toString());
            holder.mName.setText(mStops.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return mStops.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public Stop mStop;
            public final View mView;
            public final TextView mSequence;
            public final TextView mName;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mSequence = (TextView) view.findViewById(R.id.sequence);
                mName = (TextView) view.findViewById(R.id.name);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mName.getText() + "'";
            }
        }
    }

}
