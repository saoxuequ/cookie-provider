package io.github.saoxuequ.cookie.provider.rfc6265.selector;

import io.github.saoxuequ.cookie.provider.CookieCriterion;
import io.github.saoxuequ.cookie.provider.rfc6265.core.Cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 去重
 */
public class DistinctSelector implements CookieSelector {
    @Override
    public List<Cookie> select(List<Cookie> cookies, CookieCriterion cookieCriterion) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        cookies.forEach(cookie -> {
            Cookie c = cookieMap.get(cookie.getName());
            if (c == null) {
                cookieMap.put(cookie.getName(), cookie);
            } else if (isPriority(cookie, c)) {
                cookieMap.put(cookie.getName(), cookie);
            }
        });
        return new ArrayList<>(cookieMap.values());
    }

    private boolean isPriority(Cookie c1, Cookie c2) {
        String domain1 = c1.getDomain() == null ? "" : c1.getDomain();
        String domain2 = c2.getDomain() == null ? "" : c2.getDomain();
        if (domain1.length() > domain2.length()) {
            return true;
        } else if (domain1.length() < domain2.length()) {
            return false;
        }

        String path1 = normalizePath(c1);
        String path2 = normalizePath(c2);
        if (path1.length() > path2.length()) {
            return true;
        } else {
            return false;
        }
    }

    private String normalizePath(final Cookie cookie) {
        String path = cookie.getPath();
        if (path == null) {
            path = "/";
        }
        if (!path.endsWith("/")) {
            path = path + '/';
        }
        return path;
    }
}
