package io.github.saoxuequ.cookie.provider.rfc6265.chrome.osx;

import io.github.saoxuequ.cookie.provider.cipher.Cipher;
import io.github.saoxuequ.cookie.provider.exception.CipherException;
import io.github.saoxuequ.cookie.provider.exception.CipherRuntimeException;
import io.github.saoxuequ.cookie.provider.rfc6265.chrome.ChromeCookie;
import io.github.saoxuequ.cookie.provider.rfc6265.chrome.ReadOnlyCookieStore;
import io.github.saoxuequ.cookie.provider.rfc6265.core.Cookie;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.*;

public class DefaultSqliteCookieStore implements ReadOnlyCookieStore {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("load org.sqlite.JDBC for DefaultSqliteCookieStore error.", e);
        }
    }

    private final String jdbcURL;
    private final Cipher cipher;

    public DefaultSqliteCookieStore(String jdbcURL, Cipher cipher) {
        this.jdbcURL = jdbcURL;
        this.cipher = cipher;
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL);
    }

    private List<Cookie> selectAllCookies() throws SQLException, CipherException {
        List<Cookie> cookies = new LinkedList<>();
        try (
                Connection c = openConnection();
                Statement s = c.createStatement();
                ResultSet resultSet = s.executeQuery("select * from cookies");
        ) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String domain = resultSet.getString("host_key");
                String val = decryptVal(domain, resultSet.getBytes("encrypted_value"));
                String expiryTime = resultSet.getString("expires_utc");
                String path = resultSet.getString("path");
                String creationTime = resultSet.getTimestamp("creation_utc").toString();
                String lastAccessTime = resultSet.getString("last_access_utc");
                Boolean persistent = getOrDefault(resultSet.getBoolean("is_persistent"), true);
                Boolean secureOnly = getOrDefault(resultSet.getBoolean("is_secure"), false);
                Boolean httpOnly = getOrDefault(resultSet.getBoolean("is_httponly"), false);
                Cookie cookie = new ChromeCookie(name, val, null, domain, path, null, null,
                        persistent, false, secureOnly, httpOnly);

                cookies.add(cookie);
            }
        }
        return Collections.unmodifiableList(cookies);
    }

    private byte[] sha256(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(str.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T getOrDefault(T val, T defaultVal) {
        if (Objects.isNull(val)) {
            return defaultVal;
        }
        return val;
    }

    private String decryptVal(String domain, byte[] encryptedByte) throws CipherException {
        if (ArrayUtils.isEmpty(encryptedByte) || encryptedByte.length < 3) {
            return StringUtils.EMPTY;
        }

        // 加密后增加了v10或者v11前缀需要去掉
        byte[] value = Arrays.copyOfRange(encryptedByte, 3, encryptedByte.length);
        value = cipher.decrypt(value);
        byte[] domainSha = sha256(domain);
        if (startWith(value, domainSha)) {
            value = Arrays.copyOfRange(value, domainSha.length, encryptedByte.length);
        }

        return new String(value).trim();
    }

    private boolean startWith(byte[] arr, byte[] prefix) {
        if (arr == null || prefix == null) {
            return arr == null && prefix == null;
        }
        if (prefix.length > arr.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (prefix[i] != arr[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Cookie> queryAll() {
        try {
            return selectAllCookies();
        } catch (SQLException sql) {
            throw new RuntimeException(sql);
        } catch (CipherException e) {
            throw new CipherRuntimeException(e);
        }
    }
}
