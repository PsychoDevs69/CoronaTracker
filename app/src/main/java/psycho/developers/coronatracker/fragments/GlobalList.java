package psycho.developers.coronatracker.fragments;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.util.Objects;

import psycho.developers.coronatracker.R;
import psycho.developers.coronatracker.Utils.SessionConfig;
import psycho.developers.coronatracker.adapters.GLobalDataAdapter;
import psycho.developers.coronatracker.model.GlobalDataModel;

public class GlobalList extends Fragment {

    //private String currentDataURL = "https://covid2019-api.herokuapp.com/v2/current";

    private View view;

    private RequestQueue requestQueue;
    private String currentDataURL = "https://corona.lmao.ninja/countries";
    private RecyclerView recyclerView;
    private boolean isLoading = false;
    private int totalDataSize = 0;
    private int totalDataLoaded = 0;
    private List<GlobalDataModel> list = new ArrayList<>();
    private List<GlobalDataModel> cachedList = new ArrayList<>();
    private GLobalDataAdapter adapter;
    private LinearLayout loadingLayout, recyclerLoadingLayout;
    public SwipeRefreshLayout swipeRefreshLayout;

    private double confirmedTotal = 0, recoveredTotal = 0, deathTotal = 0;
    private TextView confirmed_main, deaths_main, recovered_main, countrySafeTextView,
            activeCases, todayDeaths, todayCases, criticalCases, casesPerMillion, viewMoreText;
    private boolean expanded = false;
    private boolean countryFound = false;
    private MaterialCardView ownCountryCard;
    private LinearLayout moreDataLayout;

    private MaterialCardView searchCard;
    private TextInputEditText searchEditText;

    private SessionConfig sessionConfig;

    private LinearLayout ownCardLayout;
    private TextView ownCardLocation, ownCardDeaths, ownCardConfirmed, ownCardRecovered;

    private DecimalFormat decimalFormat = new DecimalFormat("#");

    private TextView minimizedOwnCountryName, minimizedOwnCountryConfirmed, minimizedTotalConfirmed;
    private RelativeLayout minimizedTotal, minimizedOwnCountry, completeTotal, completeOwnCountry;
    private int totalVisibleCards = 0;

    public GlobalList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_global_list, container, false);

        requestQueue = Volley.newRequestQueue(view.getContext());

        recyclerView = view.findViewById(R.id.globalDataRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        adapter = new GLobalDataAdapter(view.getContext(), list);
        sessionConfig = new SessionConfig(view.getContext());

        recovered_main = view.findViewById(R.id.recoveredTextView_main);
        deaths_main = view.findViewById(R.id.deathsTextView_main);
        confirmed_main = view.findViewById(R.id.confirmedCaseTextView_main);
        countrySafeTextView = view.findViewById(R.id.countrySafeTextView);
        recyclerLoadingLayout = view.findViewById(R.id.RecyclerLoadingLayout);

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

        activeCases = view.findViewById(R.id.activeCases_expandedCard);
        todayDeaths = view.findViewById(R.id.deathsToday_expandedCard);
        todayCases = view.findViewById(R.id.casesToday_expandedCard);
        criticalCases = view.findViewById(R.id.criticalCases_expandedCard);
        casesPerMillion = view.findViewById(R.id.casesPerMillion_expandedCard);
        viewMoreText = view.findViewById(R.id.viewMoreText);
        ownCountryCard = view.findViewById(R.id.ownCountryCard);
        moreDataLayout = view.findViewById(R.id.moreDataLayout);

        minimizedOwnCountryName = view.findViewById(R.id.locationNameTextView_ownCountryMinimized);
        minimizedOwnCountryConfirmed = view.findViewById(R.id.confirmedCaseTextView_ownCountryMinimized);
        minimizedTotalConfirmed = view.findViewById(R.id.confirmedCaseTextView_TotalMinimized);

        minimizedTotal = view.findViewById(R.id.minimizedDataLayout_Total);
        minimizedOwnCountry = view.findViewById(R.id.minimizedDataLayout_ownCountry);
        completeTotal = view.findViewById(R.id.completeDataCardTotalCases);
        completeOwnCountry = view.findViewById(R.id.completeOwnCountryLayout);

        onClickListeners();

        getCurrentData();

        return view;
    }

    private void loadMore(int lastCompletelyVisibleItemPosition) {

        isLoading = true;
        recyclerLoadingLayout.setVisibility(View.VISIBLE);

        if (lastCompletelyVisibleItemPosition < totalDataSize) {

            if ((lastCompletelyVisibleItemPosition + 10) < totalDataSize) {

                for (int i = totalDataLoaded; i < (totalDataLoaded + 10); i++) {

                    GlobalDataModel dataModel = cachedList.get(i);

                    list.add(dataModel);
                }
                totalDataLoaded += 10;
                recyclerLoadingLayout.setVisibility(View.GONE);
                isLoading = false;
            } else {


                for (int i = totalDataLoaded; i < totalDataSize; i++) {
                    GlobalDataModel dataModel = cachedList.get(i);
                    list.add(dataModel);
                }
                totalDataLoaded = totalDataSize;
                recyclerLoadingLayout.setVisibility(View.GONE);
                isLoading = false;
            }
        }
    }

    private void onClickListeners() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCurrentData();
            }
        });

        ownCountryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expanded) {
                    TransitionManager.beginDelayedTransition(ownCountryCard, new AutoTransition());
                    moreDataLayout.setVisibility(View.GONE);
                    viewMoreText.setVisibility(View.VISIBLE);
                    expanded = false;
                } else {
                    TransitionManager.beginDelayedTransition(ownCountryCard, new AutoTransition());
                    viewMoreText.setVisibility(View.GONE);
                    moreDataLayout.setVisibility(View.VISIBLE);
                    expanded = true;
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (totalVisibleCards == 0) {
                    totalVisibleCards = Objects.requireNonNull(linearLayoutManager).findLastCompletelyVisibleItemPosition() -
                            Objects.requireNonNull(linearLayoutManager).findFirstCompletelyVisibleItemPosition();
                    totalVisibleCards +=1;
                }

                /*EXPAND AND MINIMIZE CARD*/
                if (Objects.requireNonNull(linearLayoutManager).findLastCompletelyVisibleItemPosition() > totalVisibleCards) {
                    completeTotal.setVisibility(View.GONE);
                    minimizedTotal.setVisibility(View.VISIBLE);
                    completeOwnCountry.setVisibility(View.GONE);
                    minimizedOwnCountry.setVisibility(View.VISIBLE);

                } else {
                    completeTotal.setVisibility(View.VISIBLE);
                    minimizedTotal.setVisibility(View.GONE);
                    completeOwnCountry.setVisibility(View.VISIBLE);
                    minimizedOwnCountry.setVisibility(View.GONE);
                }

                /*LOAD MORE DATA ON SCROLL*/
                if (!isLoading) {
                    if (Objects.requireNonNull(linearLayoutManager).findLastCompletelyVisibleItemPosition() == list.size() - 2
                            && totalDataLoaded != totalDataSize) {

                        loadMore(linearLayoutManager.findLastCompletelyVisibleItemPosition());

                    }
                }
            }

        });
    }

    public void getCurrentData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, currentDataURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("currentData", "onResponse: " + response);

                try {

                    sessionConfig.setCachedData(response);

                    parseAndSetData(response);

                } catch (Exception e) {
                    Log.e("VOLLEY_EXCEPTION", "onResponse: ", e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("currentData", "onErrorResponse: " + error);
                Log.e("CACHED_DATA", "onErrorResponse: ");
                if (!sessionConfig.getCachedData().equals("none")) {
                    parseAndSetData(sessionConfig.getCachedData());
                }
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

    private void parseAndSetData(String data) {

        confirmedTotal = 0;
        deathTotal = 0;
        recoveredTotal = 0;

        if (list.size() > 0) {
            list.clear();
        }

        if (cachedList.size() > 0) {
            cachedList.clear();
        }

        if (totalDataLoaded > 0) {
            totalDataLoaded = 0;
        }


        try {
            JSONArray dataArray = new JSONArray(data + "787878787878787878");

            totalDataSize = dataArray.length();

            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject object = dataArray.getJSONObject(i);

                if (object.getString("country") != null && !object.getString("country").isEmpty()) {

                    double cases = 0, recovered = 0, deaths = 0, todayCasesCount = 0,
                            todayDeathsCount = 0, activeCount = 0, criticalCount = 0, casesPerMillionCount = 0;

                    try {
                        cases = object.getDouble("cases");
                    } catch (Exception e) {
                        Log.e("CASES", "parseAndSetData: " + i);
                    }

                    try {
                        recovered = object.getDouble("recovered");
                    } catch (Exception e) {
                        Log.e("RECOVERED", "parseAndSetData: " + i);
                    }

                    try {
                        deaths = object.getDouble("deaths");
                    } catch (Exception e) {
                        Log.e("DEATHS", "parseAndSetData: " + i);
                    }

                    try {
                        todayCasesCount = object.getDouble("todayCases");
                    } catch (Exception e) {
                        Log.e("TODAY_CASES", "parseAndSetData: " + i);
                    }

                    try {
                        todayDeathsCount = object.getDouble("todayDeaths");
                    } catch (Exception e) {
                        Log.e("TODAY_DEATH", "parseAndSetData: " + i);
                    }

                    try {
                        activeCount = object.getDouble("active");
                    } catch (Exception e) {
                        Log.e("ACTIVE", "parseAndSetData: " + i);
                    }

                    try {
                        criticalCount = object.getDouble("critical");
                    } catch (Exception e) {
                        Log.e("CRITICAL", "parseAndSetData: " + i);
                    }

                    try {
                        casesPerMillionCount = object.getDouble("casesPerOneMillion");
                    } catch (Exception e) {
                        Log.e("MILLION", "parseAndSetData: " + i);
                    }


                    GlobalDataModel model = new GlobalDataModel(object.getString("country"), cases,
                            recovered, deaths, todayCasesCount, todayDeathsCount,
                            activeCount, criticalCount, casesPerMillionCount);

                    cachedList.add(model);

                    confirmedTotal += cases;
                    deathTotal += deaths;
                    recoveredTotal += recovered;


                    if (sessionConfig.getCountry().toLowerCase().equals(object.getString("country").toLowerCase())) {
                        ownCardLayout.setVisibility(View.VISIBLE);
                        countryFound = true;
                        ownCardLocation.setText(object.getString("country"));
                        minimizedOwnCountryName.setText(object.getString("country"));
                        ownCardConfirmed.setText(decimalFormat.format(cases));
                        minimizedOwnCountryConfirmed.setText(decimalFormat.format(cases));
                        ownCardRecovered.setText(decimalFormat.format(recovered));
                        ownCardDeaths.setText(decimalFormat.format(deaths));

                        activeCases.setText(decimalFormat.format(activeCount));
                        todayDeaths.setText(decimalFormat.format(todayDeathsCount));
                        todayCases.setText(decimalFormat.format(todayCasesCount));
                        criticalCases.setText(decimalFormat.format(criticalCount));
                        casesPerMillion.setText(decimalFormat.format(casesPerMillionCount));
                    }
                }
            }
            for (int i = 0; i < 10; i++) {
                GlobalDataModel dataModel = cachedList.get(i);
                list.add(dataModel);
                totalDataLoaded++;
            }

            recyclerView.setAdapter(adapter);


            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            loadingLayout.setVisibility(View.GONE);

            String confirmedCount = decimalFormat.format(confirmedTotal);
            String recoveredCount = decimalFormat.format(recoveredTotal);
            String deathCount = decimalFormat.format(deathTotal);

            confirmed_main.setText(confirmedCount);
            minimizedTotalConfirmed.setText(confirmedCount);
            recovered_main.setText(recoveredCount);
            deaths_main.setText(deathCount);

            sessionConfig.setConfirmedCases(confirmedCount);
            sessionConfig.setRecoveredCases(recoveredCount);
            sessionConfig.setDeathCases(deathCount);

            if (!countryFound) {
                ownCardLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CACHED_DATA", "parseAndSetData:");
            if (!sessionConfig.getCachedData().equals("none")) {
                parseAndSetData(sessionConfig.getCachedData());
            }
        }
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
            for (int i = 0; i < 10; i++) {
                GlobalDataModel dataModel = cachedList.get(i);
                list.add(dataModel);
            }
            totalDataLoaded = 10;
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
