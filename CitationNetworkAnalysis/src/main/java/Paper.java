import com.google.gson.Gson;

import java.util.List;

public class Paper {
    private List<String> references;
    private String id;
    private String title;
    private int year;
    private int n_citation;
    private String publisher;

    public void setData(String jsonLine) {
        Gson gson = new Gson();
    }

    public List<String> getReferences() {
        return references;
    }

    public boolean containsKeyWord(String keyword) {
        return title.toLowerCase().contains((keyword.toLowerCase()));
    }

    public Long getId() {
        return Long.parseLong(id);
    }

    @Override
    public String toString() {
        return
                "id: " + id
                        + "\ntitle: " + title
                        + "\nyear: " + year
                        + "\nn_citation: " + n_citation
                        + "\npublisher: " + publisher
                        + "\n";
    }
}