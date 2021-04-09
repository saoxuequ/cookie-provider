package io.github.saoxuequ.cookie.provider.rfc6265.core;

import java.util.Date;

/**
 * RFC 6265
 */
public interface Cookie {

    String getName();
    String getValue();
    Date getExpiryTime();
    String getDomain();
    String getPath();

    boolean isHttpOnly();
    boolean isSecureOnly();
    boolean isHostOnly();

    boolean isExpired(Date date);
}
