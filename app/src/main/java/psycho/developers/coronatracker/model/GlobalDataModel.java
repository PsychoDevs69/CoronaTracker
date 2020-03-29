package psycho.developers.coronatracker.model;

public class GlobalDataModel {

    String locationName;
    double confirmed, recovered, deaths, todayCases, todayDeaths, activeCases, criticalCases, casesPerMillion;

    public GlobalDataModel(String locationName, double confirmed, double recovered, double deaths,
                           double todayCases, double todayDeaths, double activeCases, double criticalCases,
                           double casesPerMillion) {
        this.locationName = locationName;
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.deaths = deaths;
        this.todayCases = todayCases;
        this.todayDeaths = todayDeaths;
        this.activeCases = activeCases;
        this.criticalCases = criticalCases;
        this.casesPerMillion = casesPerMillion;
    }

    public double getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(double todayCases) {
        this.todayCases = todayCases;
    }

    public double getTodayDeaths() {
        return todayDeaths;
    }

    public void setTodayDeaths(double todayDeaths) {
        this.todayDeaths = todayDeaths;
    }

    public double getActiveCases() {
        return activeCases;
    }

    public void setActiveCases(double activeCases) {
        this.activeCases = activeCases;
    }

    public double getCriticalCases() {
        return criticalCases;
    }

    public void setCriticalCases(double criticalCases) {
        this.criticalCases = criticalCases;
    }

    public double getCasesPerMillion() {
        return casesPerMillion;
    }

    public void setCasesPerMillion(double casesPerMillion) {
        this.casesPerMillion = casesPerMillion;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(double confirmed) {
        this.confirmed = confirmed;
    }

    public double getRecovered() {
        return recovered;
    }

    public void setRecovered(double recovered) {
        this.recovered = recovered;
    }

    public double getDeaths() {
        return deaths;
    }

    public void setDeaths(double deaths) {
        this.deaths = deaths;
    }
}
