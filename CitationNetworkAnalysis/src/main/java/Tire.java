import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class Tire {
    private Map<Long, Paper> paperMap;
    private List<Long> allPapers;
    private Map<Long, Long> currentPapers;
    private static String keyword;

    public Tire(String keyword) {
        this.keyword = keyword;
        paperMap = new ConcurrentHashMap<Long, Paper>();
        allPapers = new ArrayList<Long>();
        currentPapers = new ConcurrentHashMap<Long, Long>();
    }

    public void setPaperMap(Map<Long, Paper> paperMap) {
        this.paperMap = paperMap;
    }

    public void setAllPapers(List<Long> allPapers) {
        this.allPapers = allPapers;
    }

    public void setCurrentPapers(Map<Long, Long> currentPapers) {
        this.currentPapers = currentPapers;
    }

    private void createPaper(String line) {
        Gson gson = new Gson();
        Paper paper = gson.fromJson(line, Paper.class);
        paper.setData(line);
        Long id = paper.getId();
        if (paper.containsKeyWord(keyword)) {
            currentPapers.put(id, id);
        }
        allPapers.add(id);
        paperMap.put(id, paper);
    }

    public void getPapers(List<String> lines) throws IOException {
        lines.parallelStream().forEach(line -> createPaper(line));
    }

    private boolean checkReferences(Long id) {
        // check if the references of this paper contain an ID in the currentPapers list
        List<String> references = paperMap.get(id).getReferences();

        if (references != null) {
            return references.parallelStream().anyMatch(obj -> currentPapers.containsKey(Long.parseLong(obj.trim())));
        } else
            return false;
    }

    public ConcurrentMap<Long, Long> getNextTiers() {
        List<Long> newPapers = allPapers.parallelStream().filter(p -> checkReferences(p)).sorted().collect(Collectors.toList());
        return newPapers.parallelStream().collect(Collectors.toConcurrentMap(p -> p, p -> p));
    }

    public void print() {
        String res = "";
        if (havePaper())
            currentPapers.keySet().forEach( id -> System.out.println( paperMap.get(id).toString() ) );
        else {
            System.out.println("No result found" + "\n");
        }
    }

    public boolean havePaper() {
        return !(currentPapers == null || currentPapers.isEmpty());
    }
}
