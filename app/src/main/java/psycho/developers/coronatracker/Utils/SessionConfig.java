package psycho.developers.coronatracker.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionConfig {

    private Context context;
    private SharedPreferences sharedPreferences;
    private String initSharedPref = "SharedPref_coronaTracker_init";
    private String confirmedCaseSharedPref = "SharedPref_coronaTracker_confirmed";
    private String recoveredCaseSharedPref = "SharedPref_coronaTracker_recovered";
    private String deathCaseSharedPref = "SharedPref_coronaTracker_death";
    private String countrySharedPref = "SharedPref_coronaTracker_country";
    private String shareLinkSharedPref = "SharedPref_coronaTracker_shareLink";

    public SessionConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(initSharedPref, Context.MODE_PRIVATE);
    }

    public String getConfirmedCases() {
        return sharedPreferences.getString(confirmedCaseSharedPref, "000");
    }

    public void setConfirmedCases(String confirmedCases) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(confirmedCaseSharedPref, confirmedCases);
        editor.apply();
    }

    public String getRecoveredCases() {
        return sharedPreferences.getString(recoveredCaseSharedPref, "00");
    }

    public void setRecoveredCases(String recoveredCases) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(recoveredCaseSharedPref, recoveredCases);
        editor.apply();
    }

    public String getDeathCases() {
        return sharedPreferences.getString(deathCaseSharedPref, "000");
    }

    public void setDeathCases(String deathCases) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(deathCaseSharedPref, deathCases);
        editor.apply();
    }

    public String getCountry() {
        return sharedPreferences.getString(countrySharedPref, "India");
    }

    public void setCountry(String country) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(countrySharedPref, country);
        editor.apply();
    }

    public String getShareLink() {
        return sharedPreferences.getString(shareLinkSharedPref, "https://apkpure.com/corona-tracker/psycho.developers.coronatracker");
    }

    public void setShareLink(String shareLink) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(shareLinkSharedPref, shareLink);
        editor.apply();
    }
}
