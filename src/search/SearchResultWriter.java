package search;

import util.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchResultWriter {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    //Writers search results to an output file asynchronously
    public void writeResults(List<String> results, String outputFile) {
        executor.submit(() -> {
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                for (String result : results) {
                    writer.println(result);
                }
                Logger.info("Search results written to: " + outputFile);
            } catch (IOException e) {
                Logger.error("Error writing results to file: " + outputFile);
            }
        });
        executor.shutdown();
    }
}
