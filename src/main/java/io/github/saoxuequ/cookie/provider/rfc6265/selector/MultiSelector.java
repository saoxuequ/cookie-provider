package io.github.saoxuequ.cookie.provider.rfc6265.selector;

import io.github.saoxuequ.cookie.provider.CookieCriterion;
import io.github.saoxuequ.cookie.provider.rfc6265.core.Cookie;
import io.github.saoxuequ.cookie.provider.utils.Preconditions;

import java.util.Collections;
import java.util.List;

public class MultiSelector implements CookieSelector {

    private final List<CookieSelector> selectors;

    /**
     *
     * @param selectors not null
     */
    public MultiSelector(List<CookieSelector> selectors) {
        Preconditions.checkArgument(selectors != null);
        this.selectors = selectors;
    }

    @Override
    public List<Cookie> select(List<Cookie> cookies, CookieCriterion cookieCriterion) {
        if (cookies == null) {
            return Collections.emptyList();
        }
        for (int i = 0; i < selectors.size(); i++) {
            cookies = selectors.get(i).select(cookies, cookieCriterion);
        }
        return cookies;
    }
}
