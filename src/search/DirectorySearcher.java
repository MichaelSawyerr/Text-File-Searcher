package search;

import util.Cache;
import util.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DirectorySearcher {
    private final FileSearcher fileSearcher;
    private final ExecutorService executor;
    private final CompletionService<List<String>> completionService;
    private final Cache cache;
    private final long maxFileSizeMB;


    public DirectorySearcher(FileSearcher fileSearcher, Cache cache, long maxFileSizeMB) {
        this.fileSearcher = fileSearcher;
        this.cache = cache;
        this.maxFileSizeMB = maxFileSizeMB;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); //Fixed Thread pool
        this.completionService = new ExecutorCompletionService<>(executor);
    }
    //Starts the search process
    public List<String> search(File directory, String keyword, boolean caseInsensitive) {
        List<String> resultFiles = new ArrayList<>();
        completionService.submit(() -> searchDirectory(directory, keyword, caseInsensitive, resultFiles)); //initial task counter
        int tasks = 1;

        while (tasks > 0) {
            try {
                Future<List<String>> future = completionService.take();
                future.get();
                tasks--;
            } catch (InterruptedException | ExecutionException e) {
                Logger.error("Error in directory search: " + e.getMessage());
            }
        }

        executor.shutdown();
        Logger.info("Search completed.");
        return resultFiles;
    }

    //Traverses through directories for the keyword
    private List<String> searchDirectory(File directory, String keyword, boolean caseInsensitive, List<String> resultFiles) {
        File[] files = directory.listFiles();
        if (files != null) {
            List<Future<List<String>>> futures = new ArrayList<>();
            for (File file : files) {
                if (file.isDirectory()) {
                    futures.add(completionService.submit(() -> searchDirectory(file, keyword, caseInsensitive, resultFiles)));
                } else if (file.isFile() && file.getName().endsWith(".txt")) {
                    if (fileSearcher.searchFile(file, keyword, caseInsensitive)) {
                        synchronized (resultFiles) {
                            resultFiles.add(file.getAbsolutePath());
                        }
                    }
                }
            }

            for (Future<List<String>> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    Logger.error("Error in directory search: " + e.getMessage());
                }
            }
        }
        Logger.info("Finished searching directory: " + directory.getAbsolutePath());
        return resultFiles;
    }
}
