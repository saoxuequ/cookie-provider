package io.github.saoxuequ.cookie.provider.rfc6265.selector;

import io.github.saoxuequ.cookie.provider.rfc6265.core.Cookie;
import io.github.saoxuequ.cookie.provider.CookieCriterion;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DomainSelector implements CookieSelector {
    @Override
    public List<Cookie> select(List<Cookie> cookies, CookieCriterion cookieCriterion) {
        if (cookies == null) {
            return Collections.emptyList();
        }
        return cookies.stream()
                        .filter(c -> matchHost(c, cookieCriterion))
                        .collect(Collectors.toList());
    }

    private boolean matchHost(Cookie cookie, CookieCriterion cookieCriterion) {
        if (cookie.getDomain() == null) {
            return false;
        }
        String domain = cookie.getDomain().toLowerCase().trim();
        String host = cookieCriterion.getHost().toLowerCase().trim();
        if (domain.startsWith(".")) {
            domain = domain.substring(1);
        }
        if (host.equals(domain)) {
            return true;
        }
        if (host.endsWith(domain)) {
            final int prefix = host.length() - domain.length();
            if (prefix > 1 && host.charAt(prefix - 1) == '.') {
                return true;
            }
        }
        return false;
    }
}
