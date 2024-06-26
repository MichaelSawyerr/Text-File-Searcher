package util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Cache {
    private final Set<String> cache = Collections.synchronizedSet(new HashSet<>());

    public boolean contains(String filePath) {
        return cache.contains(filePath);
    }

    public void add(String filePath) {
        cache.add(filePath);
    }
}
