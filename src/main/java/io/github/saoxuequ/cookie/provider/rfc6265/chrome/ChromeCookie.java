package io.github.saoxuequ.cookie.provider.rfc6265.chrome;

import io.github.saoxuequ.cookie.provider.rfc6265.core.Cookie;

import java.util.Date;

public class ChromeCookie implements Cookie {
    private final String name;
    private final String value;
    private final Date expiryTime;
    private final String domain;
    private final String path;
    private final Date creationTime;
    private final Date lastAccessTime;
    private final boolean persistent;
    private final boolean hostOnly;
    private final boolean secureOnly;
    private final boolean httpOnly;

    public ChromeCookie(String name, String value, Date expiryTime, String domain, String path, Date creationTime,
                        Date lastAccessTime, boolean persistent, boolean hostOnly, boolean secureOnly,
                        boolean httpOnly) {
        this.name = name;
        this.value = value;
        this.expiryTime = expiryTime;
        this.domain = domain;
        this.path = path;
        this.creationTime = creationTime;
        this.lastAccessTime = lastAccessTime;
        this.persistent = persistent;
        this.hostOnly = hostOnly;
        this.secureOnly = secureOnly;
        this.httpOnly = httpOnly;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public Date getExpiryTime() {
        return this.expiryTime;
    }

    @Override
    public String getDomain() {
        return this.domain;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public boolean isHttpOnly() {
        return this.httpOnly;
    }

    @Override
    public boolean isSecureOnly() {
        return this.isSecureOnly();
    }

    @Override
    public boolean isHostOnly() {
        return this.hostOnly;
    }

    @Override
    public boolean isExpired(Date date) {
        return (expiryTime != null && expiryTime.getTime() <= date.getTime());
    }
}
