package psycho.developers.coronatracker.model;

public class GlobalDataModel {

    String locationName;
    double confirmed, recovered, deaths;

    public GlobalDataModel(String locationName, double confirmed, double recovered, double deaths) {
        this.locationName = locationName;
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.deaths = deaths;
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
