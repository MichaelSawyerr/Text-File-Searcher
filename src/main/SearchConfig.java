package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SearchConfig {
    private final Properties properties = new Properties();


    public SearchConfig(String configFilePath) throws IOException {
        properties.load(new FileInputStream(configFilePath));
    }

    public String getDirectory() {
        return properties.getProperty("directory");
    }

    public String getKeyword() {
        return properties.getProperty("keyword");
    }

    public boolean isCaseInsensitive() {
        return Boolean.parseBoolean(properties.getProperty("caseInsensitive"));
    }

    public String getOutputFile() {
        return properties.getProperty("outputFile");
    }

    public String getLogFile() {
        return properties.getProperty("logFile");
    }

    public String getLogLevel() {
        return properties.getProperty("logLevel");
    }

    public long getMaxFileSizeMB() {
        return Long.parseLong(properties.getProperty("maxFileSizeMB"));
    }
}
