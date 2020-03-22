package psycho.developers.coronatracker.model;

public class PrecautionsModel {
    public String title,content,whyContent;

    public PrecautionsModel() {
    }

    public PrecautionsModel(String title, String content, String whyContent) {
        this.title = title;
        this.content = content;
        this.whyContent = whyContent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWhyContent() {
        return whyContent;
    }

    public void setWhyContent(String whyContent) {
        this.whyContent = whyContent;
    }
}
