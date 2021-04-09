package io.github.saoxuequ.cookie.provider.rfc6265.chrome;

import io.github.saoxuequ.cookie.provider.CookieCriterion;
import io.github.saoxuequ.cookie.provider.CookieProvider;
import io.github.saoxuequ.cookie.provider.rfc6265.core.Cookie;
import io.github.saoxuequ.cookie.provider.rfc6265.selector.CookieSelector;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class CachedChromeCookieProvider implements CookieProvider {

    private final ReadOnlyCookieStore cookieStore;
    private final CookieSelector selector;
    private final long expireSeconds;
    private final ReentrantLock lock = new ReentrantLock();
    private volatile long cachedTime;
    private volatile List<Cookie> cookies;


    public CachedChromeCookieProvider(ReadOnlyCookieStore cookieStore, CookieSelector selector, long expireSeconds) {
        this.cookieStore = cookieStore;
        this.selector = selector;
        this.expireSeconds = expireSeconds;
        this.cookies = cookieStore.queryAll();
        this.cachedTime = System.currentTimeMillis();
    }

    @Override
    public List<io.github.saoxuequ.cookie.provider.Cookie> provide(CookieCriterion cookieCriterion) {
        if (System.currentTimeMillis() - cachedTime > expireSeconds) {
            if (lock.tryLock()) {
                try {
                    this.cookies = cookieStore.queryAll();
                } finally {
                    lock.unlock();
                }
            }
        }
        return select(cookieCriterion);
    }

    private List<io.github.saoxuequ.cookie.provider.Cookie> select(CookieCriterion cookieCriterion) {
        return selector.select(cookies, cookieCriterion)
                .stream()
                .map(c -> new io.github.saoxuequ.cookie.provider.Cookie(c.getName(), c.getValue()))
                .collect(Collectors.toList());
    }
}
