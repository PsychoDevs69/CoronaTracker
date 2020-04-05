package psycho.developers.coronatracker.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import psycho.developers.coronatracker.R;
import psycho.developers.coronatracker.Utils.SessionConfig;
import psycho.developers.coronatracker.model.GlobalDataModel;

public class ShowCountryData extends AppWidgetProvider {

    private String currentDataURL = "https://corona.lmao.ninja/countries";
    SessionConfig sessionConfig;
    RequestQueue requestQueue;
    TextView country, confirmed, deaths, recovered;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = ShowCountryDataConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.show_country_data_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);


       // getCurrentData();
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            ShowCountryDataConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        sessionConfig = new SessionConfig(context);
        requestQueue = Volley.newRequestQueue(context);
        View view = LayoutInflater.from(context).inflate(R.layout.show_country_data_widget, null);
        TextView confirmed;
        confirmed = view.findViewById(R.id.confirmedCaseTextView);
        confirmed.setText("0212");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /*public void getCurrentData() {

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
                    //getIndianData();
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
            JSONArray dataArray = new JSONArray(data);

            totalDataSize = dataArray.length();

            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject object = dataArray.getJSONObject(i);

                if (!object.getString("country").equalsIgnoreCase("World") && !object.getString("country").isEmpty()) {

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

                        if (sessionConfig.getCountry().equalsIgnoreCase("India")) {
                            criticalCases.setText(decimalFormat.format(criticalCount));
                            casesPerMillion.setText(decimalFormat.format(casesPerMillionCount));
                        } else {
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
            }
            for (int i = 0; i < 10; i++) {
                GlobalDataModel dataModel = cachedList.get(i);
                list.add(dataModel);
                totalDataLoaded++;
            }


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

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CACHED_DATA", "parseAndSetData:");
            if (!sessionConfig.getCachedData().equals("none")) {
                parseAndSetData(sessionConfig.getCachedData());
            }
        }
    }*/
}

