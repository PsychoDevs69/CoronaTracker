package psycho.developers.coronatracker.model;

public class StateWiseDataModel {

    String name, helpline;
    double confirmed, totalIndia, recovered, deaths, active;

    public StateWiseDataModel(String name, String helpline, double confirmed, double totalIndia, double recovered, double deaths, double active) {
        this.name = name;
        this.helpline = helpline;
        this.confirmed = confirmed;
        this.totalIndia = totalIndia;
        this.recovered = recovered;
        this.deaths = deaths;
        this.active = active;
    }

    public double getActive() {
        return active;
    }

    public void setActive(double active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHelpline() {
        return helpline;
    }

    public void setHelpline(String helpline) {
        this.helpline = helpline;
    }

    public double getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(double confirmed) {
        this.confirmed = confirmed;
    }

    public double getTotalIndia() {
        return totalIndia;
    }

    public void setTotalIndia(double totalIndia) {
        this.totalIndia = totalIndia;
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
