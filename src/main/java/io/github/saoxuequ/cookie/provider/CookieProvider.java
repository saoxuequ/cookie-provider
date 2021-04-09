package io.github.saoxuequ.cookie.provider;

import java.util.List;

public interface CookieProvider {
    List<Cookie> provide(CookieCriterion cookieCriterion);
}
