package psycho.developers.coronatracker.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import psycho.developers.coronatracker.R;
import psycho.developers.coronatracker.Utils.SessionConfig;
import psycho.developers.coronatracker.adapters.StateWiseListAdapter;
import psycho.developers.coronatracker.model.GlobalDataModel;
import psycho.developers.coronatracker.model.StateWiseDataModel;

public class IndiaStateWise extends AppCompatActivity {

    private RequestQueue requestQueue;
    //private String stateWiseURL = "https://api.rootnet.in/covid19-in/stats/latest";
    private String stateWiseURL = "https://api.rootnet.in/covid19-in/unofficial/covid19india.org/statewise";
    private String stateWiseHelplineURL = "https://api.rootnet.in/covid19-in/contacts";
    private RecyclerView recyclerView;
    private StateWiseListAdapter adapter;
    private List<StateWiseDataModel> list = new ArrayList<>();
    private List<StateWiseDataModel> finalList = new ArrayList<>();
    private List<StateWiseDataModel> cachedList = new ArrayList<>();
    private LinearLayout loading;

    SessionConfig sessionConfig;

    MaterialCardView nationalContactCard;
    TextView normalNumber, tollFreeNumber, nationalEmail, nationalTwitter, nationalFacebook, nationalViewMore;
    LinearLayout nationalMoreData;
    boolean expanded = false;

    private MaterialCardView searchCard;
    private TextInputEditText searchEditText;
    private FloatingActionButton searchFAB;
    private TextView countrySafeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_india_state_wise);

        requestQueue = Volley.newRequestQueue(IndiaStateWise.this);
        sessionConfig = new SessionConfig(IndiaStateWise.this);

        recyclerView = findViewById(R.id.stateWiseDataRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(IndiaStateWise.this, RecyclerView.VERTICAL, false));

        adapter = new StateWiseListAdapter(IndiaStateWise.this, finalList);

        loading = findViewById(R.id.loadingLayoutState);

        nationalContactCard = findViewById(R.id.nationalContactCard);
        normalNumber = findViewById(R.id.nationalNumberTextView);
        tollFreeNumber = findViewById(R.id.tollFreeTextView);
        nationalEmail = findViewById(R.id.nationalEmailTextView);
        nationalTwitter = findViewById(R.id.nationalTwitterTextView);
        nationalFacebook = findViewById(R.id.nationalFacebookTextView);
        nationalViewMore = findViewById(R.id.viewMoreText);
        nationalMoreData = findViewById(R.id.moreContactsLayout);

        searchCard = findViewById(R.id.searchCard);
        searchEditText = findViewById(R.id.searchEditText);
        searchFAB = findViewById(R.id.searchFAB);
        countrySafeTextView = findViewById(R.id.countrySafeTextView);

        searchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSearchCountry();
            }
        });

        nationalContactCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (expanded) {
                    TransitionManager.beginDelayedTransition(nationalContactCard, new AutoTransition());
                    nationalMoreData.setVisibility(View.GONE);
                    nationalViewMore.setVisibility(View.VISIBLE);
                    expanded = false;
                } else {
                    TransitionManager.beginDelayedTransition(nationalContactCard, new AutoTransition());
                    nationalViewMore.setVisibility(View.GONE);
                    nationalMoreData.setVisibility(View.VISIBLE);
                    expanded = true;
                }
            }
        });


        getStateWiseData();
    }

    public void getStateWiseData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, stateWiseURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("currentData", "onResponse: " + response);

                try {

                    sessionConfig.setStateData(response);

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
                if (!sessionConfig.getStateData().equals("none")) {
                    parseAndSetData(sessionConfig.getStateData());
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

    public void getStateHelplineData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, stateWiseHelplineURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("currentData", "onResponse: " + response);

                sessionConfig.setStateContactData(response);

                parseAndAddHelpLineData(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("currentData", "onErrorResponse: " + error);
                if (!sessionConfig.getStateContactData().equals("none")) {
                    parseAndAddHelpLineData(sessionConfig.getStateContactData());
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

    private void parseAndAddHelpLineData(String response) {

        if (cachedList.size() > 0) {
            cachedList.clear();
        }

        try {

            JSONObject parent = new JSONObject(response).getJSONObject("data")
                    .getJSONObject("contacts");

            normalNumber.setText(parent.getJSONObject("primary").getString("number"));
            tollFreeNumber.setText(parent.getJSONObject("primary").getString("number-tollfree"));
            nationalEmail.setText(parent.getJSONObject("primary").getString("email"));
            nationalTwitter.setText(parent.getJSONObject("primary").getString("twitter"));
            nationalFacebook.setText(parent.getJSONObject("primary").getString("facebook"));

            JSONArray dataArray = parent.getJSONArray("regional");

            String helpline = "";

            StateWiseDataModel old = null;
            JSONObject object = null;
            boolean found = true;

            for (int j = 0; j < dataArray.length(); j++) {
                for (int i = 0; i < list.size(); i++) {

                    old = list.get(i);
                    object = dataArray.getJSONObject(j);

                    if (old.getName().equals(object.getString("loc"))) {
                        helpline = object.getString("number");
                        found = true;
                        break;
                    } else {
                        found = false;
                    }
                }
                if (found) {
                    Objects.requireNonNull(old).setHelpline(helpline);
                    finalList.add(old);
                } else {
                    StateWiseDataModel model = new StateWiseDataModel(object.getString("loc"), object.getString("number"),
                            0, old.getTotalIndia(), 0, 0, 0);
                    finalList.add(model);
                }
            }

            cachedList.addAll(finalList);

            loading.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            Log.e("VOLLEY_EXCEPTION", "onResponse: ", e);
        }
    }

    private void parseAndSetData(String response) {

        try {

            JSONArray dataArray = new JSONObject(response).getJSONObject("data").getJSONArray("statewise");

            double totalIndia = new JSONObject(response).getJSONObject("data").getJSONObject("total").getDouble("confirmed");
            String name = "", helpline = "";
            double recovered = 0, confirmed = 0, deaths = 0, active= 0;

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject object = dataArray.getJSONObject(i);

                try {
                    name = object.getString("state");
                } catch (Exception e) {
                    Log.e("STATE_CRASH", i + "parseAndSetData: " + e);
                }

                try {
                    recovered = object.getDouble("recovered");
                } catch (Exception e) {
                    Log.e("STATE_CRASH", i + "parseAndSetData: " + e);
                }

                try {
                    active = object.getDouble("active");
                } catch (Exception e) {
                    Log.e("STATE_CRASH", i + "parseAndSetData: " + e);
                }

                try {
                    confirmed = object.getDouble("confirmed");
                } catch (Exception e) {
                    Log.e("STATE_CRASH", i + "parseAndSetData: " + e);
                }

                try {
                    deaths = object.getDouble("deaths");
                } catch (Exception e) {
                    Log.e("STATE_CRASH", i + "parseAndSetData: " + e);
                }


                StateWiseDataModel model = new StateWiseDataModel(name,helpline, confirmed, totalIndia, recovered, deaths, active);
                list.add(model);

            }

            getStateHelplineData();
            //recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initSearchCountry() {

        if (searchCard.getVisibility() == View.VISIBLE) {
            searchEditText.setText("");
            filterList("");
            searchCard.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        } else {
            if (loading.getVisibility() == View.VISIBLE) {
                Toast.makeText(this, "Please wait, loading list.", Toast.LENGTH_SHORT).show();
            } else {
                searchCard.setVisibility(View.VISIBLE);
                searchEditText.requestFocus();
                searchEditText.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

        if (finalList.size() > 0) {
            finalList.clear();
        }

        if (countryName.isEmpty()) {
            finalList.addAll(cachedList);
        } else {

            for (int i = 0; i < cachedList.size(); i++) {
                StateWiseDataModel model = cachedList.get(i);

                if (model.getName().toLowerCase().contains(countryName)) {

                    finalList.add(model);
                }
            }
        }

        if (finalList.size() == 0) {
            countrySafeTextView.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }
}
