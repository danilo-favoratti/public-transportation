package com.arctouch.publictransportation.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.arctouch.publictransportation.R;
import com.arctouch.publictransportation.domain.Departure;
import com.arctouch.publictransportation.domain.Route;
import com.arctouch.publictransportation.domain.generic.params.RouteIdParam;
import com.arctouch.publictransportation.domain.generic.returns.DeparturesReturn;
import com.arctouch.publictransportation.interfaces.VolleyListener;
import com.arctouch.publictransportation.volley.VolleySingleton;
import com.arctouch.publictransportation.volley.endpoints.DeparturesVolley;

import java.util.List;

public class DepartureListFragment extends Fragment implements VolleyListener<DeparturesReturn> {
    public static final String ROUTE = "route";
    public static final String SHOW_ROUTE_DETAILS = "show_route_details";

    private View mRootView;
    private View mRecyclerViewWeekday;
    private View mRecyclerViewSaturday;
    private View mRecyclerViewSunday;
    private ProgressBar mProgressBar;
    private TextView mEmptyList;

    private Route mRoute;
    private boolean mShowRouteDetails;

    public DepartureListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ROUTE)) {
            mRoute = (Route) getArguments().getSerializable((ROUTE));

            mShowRouteDetails = getArguments().containsKey(SHOW_ROUTE_DETAILS) &&
                    getArguments().getBoolean(SHOW_ROUTE_DETAILS);

        } else {
            throw new IllegalArgumentException("DepartureListFragment needs an argument type Route.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.mRootView = inflater.inflate(R.layout.layout_departure_list, container, false);
        this.mRecyclerViewWeekday = mRootView.findViewById(R.id.departureListWeekday);
        this.mRecyclerViewSaturday = mRootView.findViewById(R.id.departureListSaturday);
        this.mRecyclerViewSunday = mRootView.findViewById(R.id.departureListSunday);
        this.mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        this.mEmptyList = (TextView) mRootView.findViewById(R.id.emptyList);

        TextView routeName = (TextView) mRootView.findViewById(R.id.routeName);
        routeName.setVisibility(mShowRouteDetails ? View.VISIBLE : View.GONE);
        if (mShowRouteDetails) {
            routeName.setText(mRoute.getLongName());
        }

        loadDepartures();

        return mRootView;
    }

    private void loadDepartures() {
        this.mProgressBar.setVisibility(View.VISIBLE);
        this.mEmptyList.setVisibility(View.GONE);
        RequestQueue requestQueue = VolleySingleton.getInstance(this.getActivity()).getRequestQueue();
        DeparturesVolley departuresVolley = new DeparturesVolley(this.getContext(), new RouteIdParam(mRoute.getId()), this);
        requestQueue.add(departuresVolley.getmRequest());
    }

    @Override
    public void onVolleySuccess(DeparturesReturn departuresReturn) {
        this.mProgressBar.setVisibility(View.GONE);
        this.mEmptyList.setVisibility(departuresReturn.getRows().size() > 0 ? View.GONE : View.VISIBLE);

        setupRecyclerView((RecyclerView) this.mRecyclerViewWeekday, departuresReturn.getRowsWeekday());
        setupRecyclerView((RecyclerView) this.mRecyclerViewSaturday, departuresReturn.getRowsSaturday());
        setupRecyclerView((RecyclerView) this.mRecyclerViewSunday, departuresReturn.getRowsSunday());
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

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Departure> departures) {
        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(departures));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Departure> mDepartures;

        public SimpleItemRecyclerViewAdapter(List<Departure> items) {
            mDepartures = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.content_departure_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mDeparture = mDepartures.get(position);
            holder.mTime.setText(mDepartures.get(position).getTime());
        }

        @Override
        public int getItemCount() {
            return mDepartures.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public Departure mDeparture;
            public final TextView mTime;

            public ViewHolder(View view) {
                super(view);
                mTime = (TextView) view;
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTime.getText() + "'";
            }
        }
    }

}
