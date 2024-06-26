package util;

import java.io.File;
import java.text.SimpleDateFormat;

public class FileUtils {

    public static long getFileSizeInMB(File file) {
        return file.length() / (1024 * 1024);
    }

    public static String getLastModified(File file) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return sdf.format(file.lastModified());
    }
}
