package io.github.saoxuequ.cookie.provider.utils;

import io.github.saoxuequ.cookie.provider.config.ConfigurationProvider;
import io.github.saoxuequ.cookie.provider.rfc6265.selector.CookieSelectors;
import io.github.saoxuequ.cookie.provider.rfc6265.chrome.CachedChromeCookieProvider;
import io.github.saoxuequ.cookie.provider.rfc6265.chrome.OsxSecurityConfigurationProvider;
import io.github.saoxuequ.cookie.provider.rfc6265.chrome.osx.DefaultSqliteCookieStore;
import io.github.saoxuequ.cookie.provider.rfc6265.chrome.osx.OsxChromeCookieCipher;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

public class OsxChromeCookieProviders {

    private static final String PASSWORD_KEY = "Chrome";
    private static final String JDBC_URL_TEMPLATE = "jdbc:sqlite:" +
            "/Users/{0}/Library/Application Support/Google/Chrome/Default/Cookies";
    private static final String USER_NAME_KEY = "user.name";

    private static final ConfigurationProvider passwordStore = new OsxSecurityConfigurationProvider();

    public static CachedChromeCookieProvider newOsxChromeCookieProviderBySystemEnv() throws Exception {
        String password = passwordStore.get(PASSWORD_KEY);
        String userName = System.getProperty(USER_NAME_KEY);
        Preconditions.checkArgument(StringUtils.isNotEmpty(password),
                "获取password为空请检查配置或者直接使用new OsxChromeCookieRepository()");
        Preconditions.checkArgument(StringUtils.isNotEmpty(userName),
                "获取userName为空请检查配置或者直接使用new OsxChromeCookieRepository()");
        String jdbcUrl = MessageFormat.format(JDBC_URL_TEMPLATE, userName);
        return new CachedChromeCookieProvider(
                new DefaultSqliteCookieStore(jdbcUrl, new OsxChromeCookieCipher(password)),
                CookieSelectors.newSimpleSelector(),
                5);
    }

}
