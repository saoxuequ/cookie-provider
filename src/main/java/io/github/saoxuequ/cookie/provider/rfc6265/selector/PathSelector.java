package io.github.saoxuequ.cookie.provider.rfc6265.selector;

import io.github.saoxuequ.cookie.provider.CookieCriterion;
import io.github.saoxuequ.cookie.provider.rfc6265.core.Cookie;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PathSelector implements CookieSelector {
    @Override
    public List<Cookie> select(List<Cookie> cookies, CookieCriterion cookieCriterion) {
        if (cookies == null) {
            return Collections.emptyList();
        }
        return cookies.stream()
                        .filter(c -> matchPath(c, cookieCriterion))
                        .collect(Collectors.toList());
    }

    private boolean matchPath(Cookie cookie, CookieCriterion cookieCriterion) {
        String cookiePath = cookie.getPath() == null ? "/" : cookie.getPath().toLowerCase().trim();
        String criterionPath = cookieCriterion.getPath().trim();

        if (cookiePath.length() > 1 && cookiePath.endsWith("/")) {
            cookiePath = cookiePath.substring(0, cookiePath.length() - 1);
        }
        if (criterionPath.startsWith(cookiePath)) {
            if (cookiePath.equals("/")) {
                return true;
            }
            if (criterionPath.length() == cookiePath.length()) {
                return true;
            }
            if (criterionPath.charAt(cookiePath.length()) == '/') {
                return true;
            }
        }
        return false;
    }
}
