package io.github.saoxuequ.cookie.provider.rfc6265.selector;

import io.github.saoxuequ.cookie.provider.rfc6265.core.Cookie;
import io.github.saoxuequ.cookie.provider.CookieCriterion;

import java.util.List;

public interface CookieSelector {

    List<Cookie> select(List<Cookie> cookies, CookieCriterion cookieCriterion);
}
