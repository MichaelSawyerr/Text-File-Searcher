package search;

import util.Cache;
import util.FileUtils;
import util.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

public class FileSearcher {
    private final Cache cache;
    private final long maxFileSizeMB;

    public FileSearcher(Cache cache, long maxFileSizeMB) {
        this.cache = cache;
        this.maxFileSizeMB = maxFileSizeMB;
    }

    //Searches for keyword specifically in the Files content
    public boolean searchFile(File file, String keyword, boolean caseInsensitive) {
        if (cache.contains(file.getAbsolutePath())) {
            Logger.info("File already searched (from cache): " + file.getAbsolutePath());
            return false;
        }

        if (FileUtils.getFileSizeInMB(file) > maxFileSizeMB) {
            Logger.info("File skipped due to size limit: " + file.getAbsolutePath());
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (caseInsensitive) {
                    if (line.toLowerCase().contains(keyword.toLowerCase())) {
                        Logger.info("Keyword found in file: " + file.getAbsolutePath());
                        cache.add(file.getAbsolutePath());
                        return true;
                    }
                } else {
                    if (line.contains(keyword)) {
                        Logger.info("Keyword found in file: " + file.getAbsolutePath());
                        cache.add(file.getAbsolutePath());
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            Logger.error("Error reading file: " + file.getAbsolutePath());
        }
        return false;
    }
}
