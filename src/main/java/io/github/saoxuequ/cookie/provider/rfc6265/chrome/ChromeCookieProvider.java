package io.github.saoxuequ.cookie.provider.rfc6265.chrome;

import io.github.saoxuequ.cookie.provider.Cookie;
import io.github.saoxuequ.cookie.provider.CookieCriterion;
import io.github.saoxuequ.cookie.provider.CookieProvider;
import io.github.saoxuequ.cookie.provider.rfc6265.selector.CookieSelector;

import java.util.List;
import java.util.stream.Collectors;

public class ChromeCookieProvider implements CookieProvider {

    private final ReadOnlyCookieStore cookieStore;
    private final CookieSelector selector;

    public ChromeCookieProvider(ReadOnlyCookieStore cookieStore,
                                CookieSelector selector) {
        this.cookieStore = cookieStore;
        this.selector = selector;
    }

    @Override
    public List<Cookie> provide(CookieCriterion cookieCriterion) {
        List<io.github.saoxuequ.cookie.provider.rfc6265.core.Cookie> cookies = cookieStore.queryAll();
        return selector.select(cookies, cookieCriterion)
                .stream()
                .map(c -> new Cookie(c.getName(), c.getValue()))
                .collect(Collectors.toList());
    }
}
