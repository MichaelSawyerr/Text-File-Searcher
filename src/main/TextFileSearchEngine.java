package main;

import search.DirectorySearcher;
import search.FileSearcher;
import search.SearchResultWriter;
import util.Cache;
import util.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TextFileSearchEngine {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java main.TextFileSearchEngine <config-file>"); //checks if config file is provided
            return;
        }

        try { //Initializes Logging
            SearchConfig config = new SearchConfig(args[0]);
            Logger.initialize(config.getLogFile(), config.getLogLevel());

            File directory = new File(config.getDirectory());
            String keyword = config.getKeyword();
            boolean caseInsensitive = config.isCaseInsensitive();
            String outputFile = config.getOutputFile();
            long maxFileSizeMB = config.getMaxFileSizeMB();

            if (!directory.isDirectory()) {
                System.out.println("The specified path is not a directory.");
                return;
            }

            Logger.info("Starting search in directory: " + directory.getAbsolutePath());
            Logger.info("Keyword: " + keyword);
            Logger.info("Case Insensitive: " + caseInsensitive);
            Logger.info("Max File Size (MB): " + maxFileSizeMB);

            //Search Components Initializer
            Cache cache = new Cache();
            FileSearcher fileSearcher = new FileSearcher(cache, maxFileSizeMB);
            DirectorySearcher directorySearcher = new DirectorySearcher(fileSearcher, cache, maxFileSizeMB);

            //Search Process
            List<String> resultFiles = directorySearcher.search(directory, keyword, caseInsensitive);


            SearchResultWriter resultWriter = new SearchResultWriter();
            resultWriter.writeResults(resultFiles, outputFile);
        //Exception Handler
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Logger.close();
        }
    }
}
