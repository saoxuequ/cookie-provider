package io.github.saoxuequ.cookie.provider;

import io.github.saoxuequ.cookie.provider.utils.Preconditions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CookieCriterion {
    private final String host;
    private final String path;
    private final Map<String, String> ext = new ConcurrentHashMap<>();

    public CookieCriterion(String host, String path) {
        Preconditions.checkArgument(host != null);
        Preconditions.checkArgument(path != null);
        this.host = host;
        this.path = path;
    }

    /**
     *
     * @return notNull
     */
    public String getHost() {
        return host;
    }

    /**
     *
     * @return notNull
     */
    public String getPath() {
        return path;
    }

    public CookieCriterion put(String key, String val) {
        ext.put(key, val);
        return this;
    }

    public String get(String key) {
        return ext.get(key);
    }
}
