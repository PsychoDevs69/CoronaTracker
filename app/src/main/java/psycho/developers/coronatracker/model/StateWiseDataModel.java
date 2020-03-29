package psycho.developers.coronatracker.model;

public class StateWiseDataModel {

    String name, helpline;
    double confirmedIndian, confirmedForeign, recovered, deaths;

    public StateWiseDataModel(String name, double confirmedIndian, double confirmedForeign, double recovered, double deaths, String helpline) {
        this.name = name;
        this.confirmedIndian = confirmedIndian;
        this.confirmedForeign = confirmedForeign;
        this.recovered = recovered;
        this.deaths = deaths;
        this.helpline = helpline;
    }

    public String getHelpline() {
        return helpline;
    }

    public void setHelpline(String helpline) {
        this.helpline = helpline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getConfirmedIndian() {
        return confirmedIndian;
    }

    public void setConfirmedIndian(double confirmedIndian) {
        this.confirmedIndian = confirmedIndian;
    }

    public double getConfirmedForeign() {
        return confirmedForeign;
    }

    public void setConfirmedForeign(double confirmedForeign) {
        this.confirmedForeign = confirmedForeign;
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
