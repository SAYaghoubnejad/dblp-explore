import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CitationNetworkAnalysis {
    private String filePath;
    private static String keyword;
    private int tiresNumber;
    private int pageSize = 10000;
    private int readPaperLimit = 100000;


    CitationNetworkAnalysis() throws IOException {
        filePath = "D:\\Uni\\dblp.v11\\dblp_papers_v11.txt";

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a keyword the search based on (only one word): ");
        keyword = scanner.nextLine().replace(" ", "");
        System.out.print("Enter the number of tiers: ");
        tiresNumber = scanner.nextInt();
        scanner.close();
        Tire tires = new Tire(keyword);
        System.out.println("The keyword: " + keyword + "\n");

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        List<String> lines;
        boolean isEOF = false;
        int readPaperCounter = 0;
        while (!isEOF && readPaperCounter < readPaperLimit) {
            lines = new ArrayList<String>();
            while (lines.size() < pageSize) {
                if ((line = br.readLine()) == null) {
                    isEOF = true;
                    break;
                } else
                    lines.add(line);
            }
            readPaperCounter += pageSize;
            System.out.println(readPaperCounter + " paper was loaded");
            tires.getPapers(lines);
        }
        br.close();

        if (!tires.havePaper()) {
            System.out.println("No papers Found, keyword: " + keyword);
            return;
        }
        System.out.println("Tier#1");
        tires.print();
        for (int i = 1; i < tiresNumber; i++) {
            System.out.println("Tier#" + (i + 1));
            tires.setCurrentPapers(tires.getNextTiers());
            tires.print();
        }
    }
}
