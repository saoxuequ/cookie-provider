package io.github.saoxuequ.cookie.provider.rfc6265.chrome;

import io.github.saoxuequ.cookie.provider.rfc6265.core.Cookie;

import java.util.List;

public interface ReadOnlyCookieStore {

    List<Cookie> queryAll();
}
