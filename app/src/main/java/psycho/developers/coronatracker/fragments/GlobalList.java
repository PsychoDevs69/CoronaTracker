package psycho.developers.coronatracker.fragments;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import psycho.developers.coronatracker.R;
import psycho.developers.coronatracker.Utils.SessionConfig;
import psycho.developers.coronatracker.adapters.GLobalDataAdapter;
import psycho.developers.coronatracker.model.GlobalDataModel;

public class GlobalList extends Fragment {

    private View view;

    private RequestQueue requestQueue;
    private String currentDataURL = "https://covid2019-api.herokuapp.com/v2/current";
    private RecyclerView recyclerView;
    private List<GlobalDataModel> list = new ArrayList<>();
    private List<GlobalDataModel> cachedList = new ArrayList<>();
    private GLobalDataAdapter adapter;
    private LinearLayout loadingLayout;
    public SwipeRefreshLayout swipeRefreshLayout;

    private double confirmedTotal = 0, recoveredTotal = 0, deathTotal = 0;
    private TextView confirmed_main, deaths_main, recovered_main, updatedOnText, countrySafeTextView;

    private MaterialCardView searchCard;
    private TextInputEditText searchEditText;

    private SessionConfig sessionConfig;

    private LinearLayout ownCardLayout;
    private TextView ownCardLocation, ownCardDeaths, ownCardConfirmed, ownCardRecovered;

    public GlobalList() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_global_list, container, false);

        requestQueue = Volley.newRequestQueue(view.getContext());

        recyclerView = view.findViewById(R.id.globalDataRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        adapter = new GLobalDataAdapter(view.getContext(), list);
        sessionConfig = new SessionConfig(view.getContext());

        recovered_main = view.findViewById(R.id.recoveredTextView_main);
        deaths_main = view.findViewById(R.id.deathsTextView_main);
        confirmed_main = view.findViewById(R.id.confirmedCaseTextView_main);
        updatedOnText = view.findViewById(R.id.updatesOnTextView);
        countrySafeTextView = view.findViewById(R.id.countrySafeTextView);

        confirmed_main.setText(sessionConfig.getConfirmedCases());
        recovered_main.setText(sessionConfig.getRecoveredCases());
        deaths_main.setText(sessionConfig.getDeathCases());

        loadingLayout = view.findViewById(R.id.loadingLayout);
        loadingLayout.setVisibility(View.VISIBLE);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshGlobalList);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        searchCard = view.findViewById(R.id.searchCard);
        searchEditText = view.findViewById(R.id.searchEditText);
        searchCard.setVisibility(View.GONE);

        ownCardLayout = view.findViewById(R.id.ownCountryTollLayout);
        ownCardLayout.setVisibility(View.GONE);
        ownCardLocation = view.findViewById(R.id.ownCardLocationNameTextView);
        ownCardConfirmed = view.findViewById(R.id.ownCardConfirmedCaseTextView);
        ownCardRecovered = view.findViewById(R.id.ownCardRecoveredTextView);
        ownCardDeaths = view.findViewById(R.id.ownCarddeathsTextView);

        onClickListeners();

        getCurrentData();

        return view;
    }

    private void onClickListeners() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCurrentData();
            }
        });
    }

    public void getCurrentData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, currentDataURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("currentData", "onResponse: " + response);

                confirmedTotal = 0;
                deathTotal = 0;
                recoveredTotal = 0;

                if (list.size() > 0) {
                    list.clear();
                }

                if (cachedList.size() > 0) {
                    cachedList.clear();
                }

                boolean countryFound = false;
                try {
                    JSONObject main = new JSONObject(response);
                    JSONArray dataArray = main.getJSONArray("data");

                    for (int i = 0; i < dataArray.length(); i++) {

                        JSONObject object = dataArray.getJSONObject(i);

                        GlobalDataModel model = new GlobalDataModel(object.getString("location"), object.getDouble("confirmed"),
                                object.getDouble("recovered"), object.getDouble("deaths"));

                        list.add(model);
                        cachedList.add(model);

                        confirmedTotal += object.getDouble("confirmed");
                        deathTotal += object.getDouble("deaths");
                        recoveredTotal += object.getDouble("recovered");


                        if (sessionConfig.getCountry().toLowerCase().equals(object.getString("location").toLowerCase())) {
                            ownCardLayout.setVisibility(View.VISIBLE);
                            countryFound = true;
                            ownCardLocation.setText(object.getString("location"));
                            ownCardConfirmed.setText(object.getString("confirmed"));
                            ownCardRecovered.setText(object.getString("recovered"));
                            ownCardDeaths.setText(object.getString("deaths"));
                        }

                    }

                    recyclerView.setAdapter(adapter);

                    updatedOnText.setText(String.format("Updated on : %s", main.getString("dt")));

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    loadingLayout.setVisibility(View.GONE);

                    String confirmedCount = new DecimalFormat("#").format(confirmedTotal);
                    String recoveredCount = new DecimalFormat("#").format(recoveredTotal);
                    String deathCount = new DecimalFormat("#").format(deathTotal);

                    confirmed_main.setText(confirmedCount);
                    recovered_main.setText(recoveredCount);
                    deaths_main.setText(deathCount);

                    sessionConfig.setConfirmedCases(confirmedCount);
                    sessionConfig.setRecoveredCases(recoveredCount);
                    sessionConfig.setDeathCases(deathCount);

                    if (!countryFound){
                        ownCardLayout.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("currentData", "onErrorResponse: " + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }

    public void initSearchCountry() {

        if (searchCard.getVisibility() == View.VISIBLE) {
            searchEditText.setText("");
            filterList("");
            searchCard.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        } else {
            if (loadingLayout.getVisibility() == View.VISIBLE) {
                Toast.makeText(view.getContext(), "Please wait, loading list.", Toast.LENGTH_SHORT).show();
            } else {
                searchCard.setVisibility(View.VISIBLE);
                searchEditText.requestFocus();
                searchEditText.setText("");
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
                }
                searchEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        filterList(charSequence.toString().trim().toLowerCase());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        }
    }

    private void filterList(String countryName) {

        countrySafeTextView.setVisibility(View.GONE);

        if (list.size() > 0) {
            list.clear();
        }

        if (countryName.isEmpty()) {
            list.addAll(cachedList);
        } else {
            for (int i = 0; i < cachedList.size(); i++) {
                GlobalDataModel model = cachedList.get(i);
                if (model.getLocationName().toLowerCase().contains(countryName)) {
                    list.add(model);
                }
            }
        }

        if (list.size() == 0) {
            countrySafeTextView.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }
}
